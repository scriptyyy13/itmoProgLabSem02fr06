package graphics;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import sharedTools.Arg;
import sharedTools.DragonTableRow;
import models.Dragon;
import network.Response;
import utils.ConfigManager;
import clientMainFiles.Main;

import java.util.*;

public class MainWindow extends BaseWindow {
    private final String BG_COLOR = "#2a3950";
    public Canvas visual;
    public TextArea output;
    public Button[] buttons;

    private Pane vizPane; // прозрачный слой для кружков поверх канваса
    // кружки драконов
    private final Map<Long, Circle> activeCircles = new HashMap<>();

    public static final String[] buttonsNames = {"Add", "AddIfMin", "AddIfMax", "Update", "RemoveById", "Clear", "RemoveHead", "AverageOfAge", "UniqueWeight", "Show"};
    public static final String[] adminButtonsNames = {"balancer_status", "add_server", "remove_server"};

    public MainWindow(Stage stage) {
        super(stage);
        this.stage.setResizable(false);
        initScene();
        startBackgroundUpdate(); // запуск таймера автоматического фонового обновления
    }

    @Override
    protected Region buildUI() {
        Font bigFont = Font.loadFont(getClass().getResourceAsStream("resources/fonts/aktifo.ttf"), 20);
        Font simpleFont = Font.loadFont(getClass().getResourceAsStream("resources/fonts/aktifo.ttf"), 15);

        BorderPane root = new BorderPane();
        root.setPrefSize(WIDTH, HEIGHT - 25);
        root.setStyle("-fx-background-color: " + BG_COLOR + ";");
        HBox topbar = new HBox();

        Label welcome = new Label();
        welcome.textProperty().bind(
                LocalizationManager.createStringBinding("gui.main.welcome").concat(", " + ConfigManager.login + "!")
        );

        Label textAreaLabel = new Label();
        textAreaLabel.textProperty().bind(LocalizationManager.createStringBinding("gui.main.output_label"));

        HBox.setMargin(welcome, new Insets(15, 0, 0, 15));
        HBox.setMargin(textAreaLabel, new Insets(15, 0, 0, 730));
        topbar.getChildren().addAll(welcome, textAreaLabel);
        welcome.setAlignment(Pos.TOP_LEFT);
        textAreaLabel.setAlignment(Pos.TOP_RIGHT);
        welcome.setFont(bigFont);
        welcome.setTextFill(Color.WHITE);
        textAreaLabel.setFont(bigFont);
        textAreaLabel.setTextFill(Color.WHITE);

        VBox userButtonsArea = new VBox(15);
        buttons = new Button[13];

        for (int i = 0; i < 10; i++) {
            buttons[i] = new Button();
            String localizationKey = "gui.main.btn." + buttonsNames[i].toLowerCase();
            buttons[i].textProperty().bind(LocalizationManager.createStringBinding(localizationKey));

            userButtonsArea.getChildren().add(buttons[i]);
            buttons[i].setPrefWidth(250);
            buttons[i].setFont(simpleFont);
        }

        if ("admin".equals(ConfigManager.role)) {
            for (int i = 10; i < 13; i++) {
                buttons[i] = new Button();
                String localizationKey = "gui.main.btn." + adminButtonsNames[i - 10].toLowerCase();
                buttons[i].textProperty().bind(LocalizationManager.createStringBinding(localizationKey));

                userButtonsArea.getChildren().add(buttons[i]);
                buttons[i].setPrefWidth(250);
                buttons[i].setFont(simpleFont);
            }
        }

        userButtonsArea.setPadding(new Insets(25, 0, 0, 20));

        var vizArea = createVizArea(600, 600, 0, 100, 0, 100);
        vizPane = (Pane) vizArea.getChildren().get(1);
        visual = (Canvas) vizArea.getChildren().get(0);
        vizArea.setAlignment(Pos.TOP_LEFT);
        vizArea.setPadding(new Insets(25, 0, 0, 60));

        HBox center = new HBox();
        TextArea textArea = new TextArea();
        output = textArea;
        center.getChildren().addAll(vizArea, textArea);
        HBox.setMargin(textArea, new Insets(25, 0, 0, 25));
        textArea.setMaxWidth(275);
        textArea.setFont(simpleFont);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setMaxHeight(600);

        root.setLeft(userButtonsArea);
        root.setTop(topbar);
        root.setCenter(center);

        return root;
    }

    private StackPane createVizArea(double width, double height, double minx, double maxx, double miny, double maxy) {
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, width, height);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.0);

        for (int i = 1; i < 4; i++) {
            double x = width / 4 * i;
            gc.strokeLine(x, 0, x, height);
        }
        for (int i = 1; i < 4; i++) {
            double y = height / 4 * i;
            gc.strokeLine(0, y, width, y);
        }
        Pane vizArea = new Pane();
        vizArea.setMaxSize(width, height);
        vizArea.setPrefSize(width, height);

        StackPane gridContainer = new StackPane();
        gridContainer.getChildren().addAll(canvas, vizArea);
        return gridContainer;
    }

    /**
     * Инициализация фонового обновления данных раз в 2 секунды.
     */
    private void startBackgroundUpdate() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> fetchAndRenderDragons()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void fetchAndRenderDragons() {
        Platform.runLater(() -> {
            Response response = Main.getClientCore().executeCommand("show", new Arg[0]);
            if (response != null && response.isSuccess()) {
                updateVisualField(response.getData());
            }
        });
    }

    private void updateVisualField(String csvData) {
        if (csvData == null || csvData.trim().isEmpty()) {
            activeCircles.values().forEach(c -> vizPane.getChildren().remove(c));
            activeCircles.clear();
            return;
        }

        try {
            List<Dragon> currentDragons = Dragon.loadDragonsFromCSV(csvData);
            Set<Long> incomingIds = new HashSet<>();

            for (Dragon d : currentDragons) {
                DragonTableRow rowData = Dragon.mapToTableRow(d);
                long id = rowData.getId();
                incomingIds.add(id);

                double cx = Math.min(rowData.getX(), 600);
                double cy = Math.min(rowData.getY(), 600);

                String tooltipText =
                        LocalizationManager.getLocalizedMessage("gui.dragon.tooltip.id") + rowData.getId() + "\n" +
                                LocalizationManager.getLocalizedMessage("gui.dragon.tooltip.name") + rowData.getName() + "\n" +
                                LocalizationManager.getLocalizedMessage("gui.dragon.tooltip.age") + rowData.getAge() + "\n" +
                                LocalizationManager.getLocalizedMessage("gui.dragon.tooltip.color") + rowData.getColor() + "\n" +
                                LocalizationManager.getLocalizedMessage("gui.dragon.tooltip.creator") + rowData.getCreatorId();

                Color fillOwnerColor = generateColorFromId(rowData.getCreatorId());

                if (!activeCircles.containsKey(id)) {
                    // анимация появления
                    Circle circle = new Circle(cx, cy, rowData.getWeight());
                    circle.setFill(fillOwnerColor);
                    circle.setStroke(Color.BLACK);
                    circle.setStrokeWidth(1.0);
                    circle.setOpacity(0.0);

                    // редактирование при нажатии
                    circle.setOnMouseClicked(event -> {
                        Stage editStage = new Stage();
                        editStage.initModality(Modality.APPLICATION_MODAL);
                        EditDragonWindow edw = new EditDragonWindow(editStage, rowData, fillOwnerColor);
                        edw.showAndWait();
                    });

                    javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(tooltipText);
                    tooltip.setFont(Font.font("Arial", 13));
                    tooltip.setShowDelay(Duration.millis(200));
                    javafx.scene.control.Tooltip.install(circle, tooltip);

                    vizPane.getChildren().add(circle);
                    activeCircles.put(id, circle);

                    FadeTransition ft = new FadeTransition(Duration.millis(500), circle);
                    ft.setToValue(1.0);
                    ft.play();
                } else {
                    // если дракон уже был на карте, обновляем его позицию
                    Circle c = activeCircles.get(id);
                    c.setCenterX(cx);
                    c.setCenterY(cy);

                    // перезначение слушателя
                    c.setOnMouseClicked(event -> {
                        Stage editStage = new Stage();
                        editStage.initModality(Modality.APPLICATION_MODAL);
                        EditDragonWindow edw = new EditDragonWindow(editStage, rowData, fillOwnerColor);
                        edw.showAndWait();
                    });

                    javafx.scene.control.Tooltip updatedTooltip = new javafx.scene.control.Tooltip(tooltipText);
                    updatedTooltip.setFont(Font.font("Arial", 13));
                    updatedTooltip.setShowDelay(Duration.millis(200));
                    javafx.scene.control.Tooltip.install(c, updatedTooltip);
                }
            }

            // анимация исчезновения
            Iterator<Map.Entry<Long, Circle>> it = activeCircles.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Long, Circle> entry = it.next();
                if (!incomingIds.contains(entry.getKey())) {
                    Circle c = entry.getValue();
                    FadeTransition ft = new FadeTransition(Duration.millis(500), c);
                    ft.setToValue(0.0);
                    ft.setOnFinished(evt -> vizPane.getChildren().remove(c));
                    ft.play();
                    it.remove();
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * Генерация фиксированного цвета по уникальному ID создателя
     */
    public static Color generateColorFromId(long creatorId) {
        Random r = new Random(creatorId);
        return Color.rgb(r.nextInt(180) + 40, r.nextInt(180) + 40, r.nextInt(180) + 40);
    }

    /**
     * Находит и возвращает глобальный MenuBar окна для динамической модификации в Main.
     * Ищет компонент MenuBar внутри корневого контейнера сцены.
     *
     * @return Объект MenuBar или null, если компонент не найден.
     */
    public MenuBar getGlobalMenuBar() {
        if (stage.getScene() != null && stage.getScene().getRoot() instanceof Pane) {
            Pane rootPane = (Pane) stage.getScene().getRoot();
            for (var node : rootPane.getChildren()) {
                if (node instanceof MenuBar) {
                    return (MenuBar) node;
                }
            }
        }
        return null;
    }
}