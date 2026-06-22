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
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Окно создания и ввода параметров модели Дракона.
 */
public class EnterDragonWindow {
    private Stage stage;
    private final String BG_COLOR = "#2a3950";
    private final int WIDTH = 350;
    private final int HEIGHT = 600;
    private Dragon value;
    private Coordinates cords;
    private Person killer;

    private TextField nameInput;
    private TextField ageInput;
    private TextField weightInput;
    private TextField speakInput;
    private TextField colorInput;

    public EnterDragonWindow(Stage stage) {
        this.stage = stage;
        stage.setResizable(false);

        stage.titleProperty().bind(LocalizationManager.createStringBinding("gui.dragon.title"));
        stage.setScene(createEnterDragonScene());
    }

    public EnterDragonWindow(Stage stage, Dragon initialDragon) {
        this(stage);
        if (initialDragon != null) {
            this.cords = initialDragon.getCoordinates();
            this.killer = initialDragon.getKiller();

            nameInput.setText(initialDragon.getName());
            ageInput.setText(String.valueOf(initialDragon.getAge()));
            weightInput.setText(initialDragon.getWeight() != null ? String.valueOf(initialDragon.getWeight()) : "");
            speakInput.setText(String.valueOf(initialDragon.getSpeaking()));
            colorInput.setText(initialDragon.getColor() != null ? initialDragon.getColor().toString() : "");
        }
    }

    public Scene createEnterDragonScene() {
        Font bigFont = Font.loadFont(getClass().getResourceAsStream("resources/fonts/aktifo.ttf"), 20);
        Font simpleFont = Font.loadFont(getClass().getResourceAsStream("resources/fonts/aktifo.ttf"), 15);
        VBox root = new VBox();
        root.setStyle("-fx-background-color: " + BG_COLOR + ";");

        Label title = new Label();
        Label nameLabel = new Label();
        Label ageLabel = new Label();
        Label weightLabel = new Label();
        Label speakLabel = new Label();
        Label colorLabel = new Label();

        title.textProperty().bind(LocalizationManager.createStringBinding("gui.dragon.title"));
        nameLabel.textProperty().bind(LocalizationManager.createStringBinding("gui.dragon.name"));
        ageLabel.textProperty().bind(LocalizationManager.createStringBinding("gui.dragon.age"));
        weightLabel.textProperty().bind(LocalizationManager.createStringBinding("gui.dragon.weight"));
        speakLabel.textProperty().bind(LocalizationManager.createStringBinding("gui.dragon.speaking"));
        colorLabel.textProperty().bind(LocalizationManager.createStringBinding("gui.dragon.color"));

        title.setFont(bigFont);
        ageLabel.setFont(simpleFont);
        weightLabel.setFont(simpleFont);
        speakLabel.setFont(simpleFont);
        nameLabel.setFont(simpleFont);
        colorLabel.setFont(simpleFont);
        title.setTextFill(Color.WHITE);
        ageLabel.setTextFill(Color.WHITE);
        weightLabel.setTextFill(Color.WHITE);
        speakLabel.setTextFill(Color.WHITE);
        nameLabel.setTextFill(Color.WHITE);
        colorLabel.setTextFill(Color.WHITE);

        nameInput = new TextField();
        nameInput.setFont(simpleFont);
        ageInput = new TextField();
        ageInput.setFont(simpleFont);
        weightInput = new TextField();
        weightInput.setFont(simpleFont);
        speakInput = new TextField();
        speakInput.setFont(simpleFont);
        colorInput = new TextField();
        colorInput.setFont(simpleFont);

        Button executeBtn = new Button();
        executeBtn.textProperty().bind(LocalizationManager.createStringBinding("gui.dragon.btn.enter"));
        executeBtn.setFont(simpleFont);

        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(simpleFont);
        HBox errorBox = new HBox(errorLabel);
        errorBox.setAlignment(Pos.CENTER);

        Button cordsInput = new Button();
        cordsInput.textProperty().bind(LocalizationManager.createStringBinding("gui.dragon.btn.coordinates"));
        cordsInput.setFont(simpleFont);
        cordsInput.setOnAction(e -> {
            Stage cordsInputStage = new Stage();
            cordsInputStage.initModality(Modality.APPLICATION_MODAL);
            EnterCoordinatesWindow ecw = new EnterCoordinatesWindow(cordsInputStage, this.cords);
            this.cords = ecw.showAndGetCoordinates();
        });

        Button killerInput = new Button();
        killerInput.textProperty().bind(LocalizationManager.createStringBinding("gui.dragon.btn.killer"));
        killerInput.setFont(simpleFont);
        killerInput.setOnAction(e -> {
            Stage personInputStage = new Stage();
            personInputStage.initModality(Modality.APPLICATION_MODAL);
            EnterPersonWindow epw = new EnterPersonWindow(personInputStage, this.killer);
            this.killer = epw.showAndGetPerson();
        });

        root.getChildren().addAll(title, nameLabel, nameInput, cordsInput, ageLabel, ageInput, weightLabel, weightInput, speakLabel, speakInput, colorLabel, colorInput, killerInput, errorBox, executeBtn);

        nameInput.setMaxWidth(125);
        ageInput.setMaxWidth(125);
        weightInput.setMaxWidth(125);
        speakInput.setMaxWidth(125);
        colorInput.setMaxWidth(125);

        errorBox.setPadding(new Insets(20, 0, 0, 0));
        VBox.setMargin(title, new Insets(20, 0, 0, 20));
        VBox.setMargin(ageLabel, new Insets(20, 0, 0, 20));
        VBox.setMargin(weightLabel, new Insets(20, 0, 0, 20));
        VBox.setMargin(speakLabel, new Insets(20, 0, 0, 20));
        VBox.setMargin(nameLabel, new Insets(20, 0, 0, 20));
        VBox.setMargin(colorLabel, new Insets(20, 0, 0, 20));

        VBox.setMargin(nameInput, new Insets(5, 0, 0, 20));
        VBox.setMargin(ageInput, new Insets(5, 0, 0, 20));
        VBox.setMargin(weightInput, new Insets(5, 0, 0, 20));
        VBox.setMargin(speakInput, new Insets(5, 0, 0, 20));
        VBox.setMargin(colorInput, new Insets(5, 0, 0, 20));

        VBox.setMargin(cordsInput, new Insets(20, 0, 0, 20));
        VBox.setMargin(killerInput, new Insets(20, 0, 0, 20));

        VBox.setMargin(executeBtn, new Insets(20, 0, 0, 250));

        executeBtn.setOnAction(e -> {
            try {
                String name = nameInput.getText().trim();
                Coordinates coordinates = cords;
                Long age = Long.parseLong(ageInput.getText());
                Integer weight = Integer.parseInt(weightInput.getText());
                Boolean speaking = Boolean.parseBoolean(speakInput.getText());
                models.Color color = models.Color.valueOf(colorInput.getText().trim().toUpperCase());
                Person killer = this.killer;
                Dragon dragon = new Dragon(-1, name, coordinates, new Date(), age, weight, speaking, color, killer);
                dragon.validate();
                value = dragon;
                stage.close();
            } catch (Exception ex) {
                errorLabel.setText(LocalizationManager.getLocalizedMessage("error.incorrect_format"));
            }
        });
        return new Scene(root, WIDTH, HEIGHT);
    }

    public Dragon showAndGetDragon() {
        stage.showAndWait();
        return value;
    }
}