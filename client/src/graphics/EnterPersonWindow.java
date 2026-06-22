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
import models.Country;
import models.Location;
import models.Person;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Окно ввода параметров создателя/убийцы дракона (Person).
 */
public class EnterPersonWindow {
    private Stage stage;
    private final String BG_COLOR = "#2a3950";
    private final int WIDTH = 350;
    private final int HEIGHT = 500;
    private Person value;
    private Location location;

    private TextField nameInput;
    private TextField birthInput;
    private TextField passInput;
    private TextField nationInput;

    public EnterPersonWindow(Stage stage) {
        this.stage = stage;
        stage.setResizable(false);

        stage.titleProperty().bind(LocalizationManager.createStringBinding("gui.person.title"));
        stage.setScene(createEnterPersonScene());
    }

    public EnterPersonWindow(Stage stage, Person initialPerson) {
        this(stage);
        if (initialPerson != null) {
            this.location = initialPerson.getLocation();

            nameInput.setText(initialPerson.getName());
            if (initialPerson.getBirthday() != null) {
                birthInput.setText(new SimpleDateFormat("dd.MM.yyyy").format(initialPerson.getBirthday()));
            }
            passInput.setText(initialPerson.getPassportID());
            nationInput.setText(initialPerson.getNationality() != null ? initialPerson.getNationality().toString() : "");
        }
    }

    public Scene createEnterPersonScene() {
        Font bigFont = Font.loadFont(getClass().getResourceAsStream("resources/fonts/aktifo.ttf"), 20);
        Font simpleFont = Font.loadFont(getClass().getResourceAsStream("resources/fonts/aktifo.ttf"), 15);
        VBox root = new VBox();
        root.setStyle("-fx-background-color: " + BG_COLOR + ";");

        Label title = new Label();
        Label nameLabel = new Label();
        Label birthLabel = new Label();
        Label passLabel = new Label();
        Label nationLabel = new Label();

        title.textProperty().bind(LocalizationManager.createStringBinding("gui.person.title"));
        nameLabel.textProperty().bind(LocalizationManager.createStringBinding("gui.person.input_name"));
        birthLabel.textProperty().bind(LocalizationManager.createStringBinding("gui.person.input_birthday"));
        passLabel.textProperty().bind(LocalizationManager.createStringBinding("gui.person.input_passport"));
        nationLabel.textProperty().bind(LocalizationManager.createStringBinding("gui.person.input_nationality"));

        title.setFont(bigFont);
        birthLabel.setFont(simpleFont);
        passLabel.setFont(simpleFont);
        nationLabel.setFont(simpleFont);
        nameLabel.setFont(simpleFont);
        title.setTextFill(Color.WHITE);
        birthLabel.setTextFill(Color.WHITE);
        passLabel.setTextFill(Color.WHITE);
        nationLabel.setTextFill(Color.WHITE);
        nameLabel.setTextFill(Color.WHITE);

        nameInput = new TextField();
        nameInput.setFont(simpleFont);
        birthInput = new TextField();
        birthInput.setFont(simpleFont);
        passInput = new TextField();
        passInput.setFont(simpleFont);
        nationInput = new TextField();
        nationInput.setFont(simpleFont);

        Button executeBtn = new Button();
        executeBtn.textProperty().bind(LocalizationManager.createStringBinding("gui.person.btn.enter"));
        executeBtn.setFont(simpleFont);

        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(simpleFont);
        HBox errorBox = new HBox(errorLabel);
        errorBox.setAlignment(Pos.CENTER);

        Button locationInput = new Button();
        locationInput.textProperty().bind(LocalizationManager.createStringBinding("gui.person.btn.location"));
        locationInput.setFont(simpleFont);
        locationInput.setOnAction(e -> {
            Stage locationInputStage = new Stage();
            locationInputStage.initModality(Modality.APPLICATION_MODAL);
            EnterLocationWindow elw = new EnterLocationWindow(locationInputStage, this.location);
            this.location = elw.showAndGetLocation();
        });

        root.getChildren().addAll(title, nameLabel, nameInput, birthLabel, birthInput, passLabel, passInput, nationLabel, nationInput, locationInput, errorBox, executeBtn);

        nameInput.setMaxWidth(125);
        birthInput.setMaxWidth(125);
        passInput.setMaxWidth(125);
        nationInput.setMaxWidth(125);

        errorBox.setPadding(new Insets(20, 0, 0, 0));
        VBox.setMargin(title, new Insets(20, 0, 0, 20));
        VBox.setMargin(birthLabel, new Insets(20, 0, 0, 20));
        VBox.setMargin(passLabel, new Insets(20, 0, 0, 20));
        VBox.setMargin(nationLabel, new Insets(20, 0, 0, 20));
        VBox.setMargin(nameLabel, new Insets(20, 0, 0, 20));

        VBox.setMargin(nameInput, new Insets(5, 0, 0, 20));
        VBox.setMargin(birthInput, new Insets(5, 0, 0, 20));
        VBox.setMargin(passInput, new Insets(5, 0, 0, 20));
        VBox.setMargin(nationInput, new Insets(5, 0, 0, 20));

        VBox.setMargin(locationInput, new Insets(20, 0, 0, 20));
        VBox.setMargin(executeBtn, new Insets(20, 0, 0, 250));

        executeBtn.setOnAction(e -> {
            try {
                String name = nameInput.getText().trim();
                Date birthday = new SimpleDateFormat("dd.MM.yyyy").parse(birthInput.getText().trim());
                String passportID = passInput.getText().trim();
                Country nationality = Country.valueOf(nationInput.getText().trim().toUpperCase());
                Location location = this.location;
                Person pers = new Person(name, birthday, passportID, nationality, location);
                pers.validate();
                value = pers;
                stage.close();
            } catch (Exception ex) {
                errorLabel.setText(LocalizationManager.getLocalizedMessage("error.incorrect_format"));
            }
        });
        return new Scene(root, WIDTH, HEIGHT);
    }

    public Person showAndGetPerson() {
        stage.showAndWait();
        return value;
    }
}