package graphics;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import models.Location;

/**
 * Окно ввода параметров локации (Location).
 */
public class EnterLocationWindow {
    private Stage stage;
    private final String BG_COLOR = "#2a3950";
    private final int WIDTH = 350;
    private final int HEIGHT = 450;
    private Location value;

    private TextField xInput;
    private TextField yInput;
    private TextField zInput;
    private TextField nameInput;

    public EnterLocationWindow(Stage stage) {
        this.stage = stage;
        stage.setResizable(false);

        stage.titleProperty().bind(LocalizationManager.createStringBinding("gui.location.title"));
        stage.setScene(createEnterLocationScene());
    }

    public EnterLocationWindow(Stage stage, Location initialLoc) {
        this(stage);
        if (initialLoc != null) {
            xInput.setText(initialLoc.getX() != null ? String.valueOf(initialLoc.getX()) : "");
            yInput.setText(initialLoc.getY() != null ? String.valueOf(initialLoc.getY()) : "");
            zInput.setText(initialLoc.getZ() != null ? String.valueOf(initialLoc.getZ()) : "");
            nameInput.setText(initialLoc.getName() != null ? initialLoc.getName() : "");
        }
    }

    public Scene createEnterLocationScene() {
        Font bigFont = Font.loadFont(getClass().getResourceAsStream("resources/fonts/aktifo.ttf"), 20);
        Font simpleFont = Font.loadFont(getClass().getResourceAsStream("resources/fonts/aktifo.ttf"), 15);
        VBox root = new VBox();
        root.setStyle("-fx-background-color: " + BG_COLOR + ";");

        Label title = new Label();
        Label xLabel = new Label();
        Label yLabel = new Label();
        Label zLabel = new Label();
        Label nameLabel = new Label();

        title.textProperty().bind(LocalizationManager.createStringBinding("gui.location.title"));
        xLabel.textProperty().bind(LocalizationManager.createStringBinding("gui.location.input_x"));
        yLabel.textProperty().bind(LocalizationManager.createStringBinding("gui.location.input_y"));
        zLabel.textProperty().bind(LocalizationManager.createStringBinding("gui.location.input_z"));
        nameLabel.textProperty().bind(LocalizationManager.createStringBinding("gui.location.input_name"));

        title.setFont(bigFont);
        xLabel.setFont(simpleFont);
        yLabel.setFont(simpleFont);
        zLabel.setFont(simpleFont);
        nameLabel.setFont(simpleFont);
        title.setTextFill(Color.WHITE);
        xLabel.setTextFill(Color.WHITE);
        yLabel.setTextFill(Color.WHITE);
        zLabel.setTextFill(Color.WHITE);
        nameLabel.setTextFill(Color.WHITE);

        xInput = new TextField();
        xInput.setFont(simpleFont);
        yInput = new TextField();
        yInput.setFont(simpleFont);
        zInput = new TextField();
        zInput.setFont(simpleFont);
        nameInput = new TextField();
        nameInput.setFont(simpleFont);

        Button executeBtn = new Button();
        executeBtn.textProperty().bind(LocalizationManager.createStringBinding("gui.location.btn.enter"));
        executeBtn.setFont(simpleFont);

        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(simpleFont);
        HBox errorBox = new HBox(errorLabel);
        errorBox.setAlignment(Pos.CENTER);
        root.getChildren().addAll(title, xLabel, xInput, yLabel, yInput, zLabel, zInput, nameLabel, nameInput, errorBox, executeBtn);

        xInput.setMaxWidth(125);
        yInput.setMaxWidth(125);
        zInput.setMaxWidth(125);
        nameInput.setMaxWidth(125);

        errorBox.setPadding(new Insets(20, 0, 0, 0));
        VBox.setMargin(title, new Insets(20, 0, 0, 20));
        VBox.setMargin(xLabel, new Insets(20, 0, 0, 20));
        VBox.setMargin(yLabel, new Insets(20, 0, 0, 20));
        VBox.setMargin(zLabel, new Insets(20, 0, 0, 20));
        VBox.setMargin(nameLabel, new Insets(20, 0, 0, 20));

        VBox.setMargin(xInput, new Insets(5, 0, 0, 20));
        VBox.setMargin(yInput, new Insets(5, 0, 0, 20));
        VBox.setMargin(zInput, new Insets(5, 0, 0, 20));
        VBox.setMargin(nameInput, new Insets(5, 0, 0, 20));

        VBox.setMargin(executeBtn, new Insets(20, 0, 0, 250));

        executeBtn.setOnAction(e -> {
            try {
                Integer x = Integer.parseInt(xInput.getText());
                Integer y = Integer.parseInt(yInput.getText());
                Integer z = Integer.parseInt(zInput.getText());
                String name = nameInput.getText().trim();
                Location loc = new Location(x, y, z, name);
                loc.validate();
                value = loc;
                stage.close();
            } catch (Exception ex) {
                errorLabel.setText(LocalizationManager.getLocalizedMessage("error.incorrect_format"));
            }
        });
        return new Scene(root, WIDTH, HEIGHT);
    }

    public Location showAndGetLocation() {
        stage.showAndWait();
        return value;
    }
}