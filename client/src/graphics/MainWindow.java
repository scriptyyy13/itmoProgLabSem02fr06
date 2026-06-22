package graphics;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import utils.ConfigManager;

public class MainWindow extends BaseWindow {
    private final String BG_COLOR = "#2a3950";
    public Canvas visual;
    public TextArea output;
    public Button[] buttons;

    public static final String[] buttonsNames = {"Add", "AddIfMin", "AddIfMax", "Update", "RemoveById", "Clear", "RemoveHead", "AverageOfAge", "UniqueWeight", "Show"};
    public static final String[] adminButtonsNames = {"balancer_status", "add_server", "remove_server"};

    public MainWindow(Stage stage) {
        super(stage);
        this.stage.setResizable(false);
        initScene();
    }

    @Override
    protected Region buildUI() { // Переопределяем buildUI
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
        visual = (Canvas) vizArea.getChildren().get(1);
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
        gc.getCanvas().getGraphicsContext2D().setFill(Color.WHITE);
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
        StackPane gridContainer = new StackPane();
        gridContainer.getChildren().addAll(vizArea, canvas);
        return gridContainer;
    }
}