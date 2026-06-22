package graphics;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import models.Coordinates;

/**
 * Окно ввода координат для создания модели дракона.
 */
public class EnterCoordinatesWindow {
    private Stage stage;
    private final String BG_COLOR = "#2a3950";
    private final int WIDTH = 250;
    private final int HEIGHT = 300;
    private Coordinates value;

    public EnterCoordinatesWindow(Stage stage) {
        this.stage = stage;
        stage.setResizable(false);

        stage.titleProperty().bind(LocalizationManager.createStringBinding("gui.coords.title"));
        stage.setScene(createEnterCoordinatesScene());
    }

    public Scene createEnterCoordinatesScene() {
        Font bigFont = Font.loadFont(getClass().getResourceAsStream("resources/fonts/aktifo.ttf"), 20);
        Font simpleFont = Font.loadFont(getClass().getResourceAsStream("resources/fonts/aktifo.ttf"), 15);
        VBox root = new VBox();
        root.setStyle("-fx-background-color: " + BG_COLOR + ";");

        Label title = new Label();
        Label xLabel = new Label();
        Label yLabel = new Label();

        title.textProperty().bind(LocalizationManager.createStringBinding("gui.coords.title"));
        xLabel.textProperty().bind(LocalizationManager.createStringBinding("gui.coords.input_x"));
        yLabel.textProperty().bind(LocalizationManager.createStringBinding("gui.coords.input_y"));

        title.setFont(bigFont);
        xLabel.setFont(simpleFont);
        yLabel.setFont(simpleFont);
        title.setTextFill(Color.WHITE);
        xLabel.setTextFill(Color.WHITE);
        yLabel.setTextFill(Color.WHITE);

        TextField xInput = new TextField();
        xInput.setFont(simpleFont);
        TextField yInput = new TextField();
        yInput.setFont(simpleFont);

        Button executeBtn = new Button();
        executeBtn.textProperty().bind(LocalizationManager.createStringBinding("gui.coords.btn.enter"));
        executeBtn.setFont(simpleFont);

        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(simpleFont);
        HBox errorBox = new HBox(errorLabel);
        errorBox.setAlignment(Pos.CENTER);
        root.getChildren().addAll(title, xLabel, xInput, yLabel, yInput, errorBox, executeBtn);

        xInput.setMaxWidth(125);
        yInput.setMaxWidth(125);

        errorBox.setPadding(new Insets(20, 0, 0, 0));
        VBox.setMargin(title, new Insets(20, 0, 0, 20));
        VBox.setMargin(xLabel, new Insets(20, 0, 0, 20));
        VBox.setMargin(yLabel, new Insets(20, 0, 0, 20));
        VBox.setMargin(xInput, new Insets(5, 0, 0, 20));
        VBox.setMargin(yInput, new Insets(5, 0, 0, 20));
        VBox.setMargin(executeBtn, new Insets(20, 0, 0, 150));

        executeBtn.setOnAction(e -> {
            try {
                Float x = Float.parseFloat(xInput.getText());
                Double y = Double.parseDouble(yInput.getText());
                Coordinates cords = new Coordinates(x, y);
                cords.validate();
                value = cords;
                stage.close();
            } catch (Exception ex) {
                errorLabel.setText(LocalizationManager.getLocalizedMessage("error.incorrect_format"));
            }
        });
        return new Scene(root, WIDTH, HEIGHT);
    }

    public Coordinates showAndGetCoordinates() {
        stage.showAndWait();
        return value;
    }
}