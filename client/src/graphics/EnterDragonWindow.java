package graphics;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
    private final int WIDTH = 650;
    private final int HEIGHT = 600;
    private Dragon value;
    private Coordinates cords;
    private Person killer;

    private TextField nameInput;
    private TextField ageInput;
    private TextField weightInput;
    private TextField speakInput;
    private ComboBox<String> colorInput;

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
            colorInput.setValue(initialDragon.getColor() != null ? initialDragon.getColor().toString() : "");
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
        ageLabel.textProperty().bind(LocalizationManager.createStringBinding("gui.dragon.age").concat(Bindings.createStringBinding(() -> "(age > 0)")));
        weightLabel.textProperty().bind(LocalizationManager.createStringBinding("gui.dragon.weight").concat(Bindings.createStringBinding(() -> "(0 < w < 30)")));
        speakLabel.textProperty().bind(LocalizationManager.createStringBinding("gui.dragon.speaking"));
        colorLabel.textProperty().bind(LocalizationManager.createStringBinding("gui.dragon.color").concat(Bindings.createStringBinding(() -> "(0 < w < 30)")));

        title.setFont(bigFont);
        title.setTextFill(Color.WHITE);

        Label[] labels = {nameLabel, ageLabel, weightLabel, speakLabel, colorLabel};
        for (Label l : labels) {
            l.setFont(simpleFont);
            l.setTextFill(Color.WHITE);
        }

        nameInput = new TextField();
        ageInput = new TextField();
        weightInput = new TextField();
        speakInput = new TextField();
        colorInput = new ComboBox<>();
        colorInput.getItems().addAll("RED","BLUE","GREEN","YELLOW","BLACK");

        TextField[] inputs = {nameInput, ageInput, weightInput, speakInput};
        for (TextField tf : inputs) {
            tf.setFont(simpleFont);
            tf.setMaxWidth(300);
        }

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

        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(simpleFont);
        HBox errorBox = new HBox(errorLabel);
        errorBox.setAlignment(Pos.CENTER);

        Button executeBtn = new Button();
        executeBtn.textProperty().bind(LocalizationManager.createStringBinding("gui.dragon.btn.enter"));
        executeBtn.setFont(simpleFont);

        javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        HBox bottomBox = new HBox(spacer, executeBtn);
        bottomBox.setPadding(new Insets(20, 20, 20, 20));

        root.getChildren().addAll(title, nameLabel, nameInput, cordsInput, ageLabel, ageInput,
                weightLabel, weightInput, speakLabel, speakInput,
                colorLabel, colorInput, killerInput, errorBox, bottomBox);

        VBox.setMargin(title, new Insets(20, 0, 0, 20));
        VBox.setMargin(cordsInput, new Insets(20, 0, 0, 20));
        VBox.setMargin(killerInput, new Insets(20, 0, 0, 20));

        for (int i = 0; i < inputs.length; i++) {
            VBox.setMargin(labels[i], new Insets(20, 0, 0, 20));
            VBox.setMargin(inputs[i], new Insets(5, 0, 0, 20));
        }

        executeBtn.setOnAction(e -> {
            try {
                String name = nameInput.getText().trim();
                Long age = Long.parseLong(ageInput.getText());
                Integer weight = Integer.parseInt(weightInput.getText());
                Boolean speaking = Boolean.parseBoolean(speakInput.getText());
                models.Color color = models.Color.valueOf(colorInput.getValue().trim().toUpperCase());
                Dragon dragon = new Dragon(-1, name, this.cords, new Date(), age, weight, speaking, color, this.killer);
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