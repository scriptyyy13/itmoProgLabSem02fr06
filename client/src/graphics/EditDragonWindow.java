package graphics;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;
import sharedTools.Arg;
import sharedTools.DragonTableRow;
import utils.ConfigManager;
import clientMainFiles.Main;

import java.util.Date;

/**
 * Окно детальной информации и покомпонентного редактирования дракона.
 */
public class EditDragonWindow {
    private final Stage stage;
    private final String BG_COLOR = "#2a3950";
    private final int WIDTH = 650;
    private final int HEIGHT = 500;

    private long id;
    private long creatorId;
    private String name;
    private Coordinates coordinates;
    private Date creationDate;
    private long age;
    private Integer weight;
    private Boolean speaking;
    private models.Color color;
    private Person killer;

    private final Color ownerColor;
    private final DragonTableRow initialRow;

    private Label nameVal, ageVal, weightVal, speakVal, colorVal, killerVal, coordsVal;

    public EditDragonWindow(Stage stage, DragonTableRow initialRow, Color ownerColor) {
        this.stage = stage;
        this.initialRow = initialRow;
        this.ownerColor = ownerColor;
        stage.setResizable(false);

        stage.titleProperty().bind(LocalizationManager.createStringBinding("gui.edit.title"));
        stage.setScene(createEditScene());
    }

    private void loadDataFromRow() {
        this.id = initialRow.getId();
        this.creatorId = initialRow.getCreatorId();
        this.name = initialRow.getName();
        this.creationDate = initialRow.getCreationDate() != null ? initialRow.getCreationDate() : new Date();
        this.age = initialRow.getAge();
        this.weight = initialRow.getWeight();
        this.speaking = initialRow.getSpeaking();

        try {
            this.color = models.Color.valueOf(initialRow.getColor().toUpperCase());
        } catch (Exception e) {
            this.color = null;
        }

        this.coordinates = new Coordinates(initialRow.getX(), initialRow.getY());

        if (initialRow.getKillerName() != null) {
            Location loc = null;
            if (initialRow.getLocX() != null || initialRow.getLocY() != null || initialRow.getLocZ() != null) {
                loc = new Location(initialRow.getLocX(), initialRow.getLocY(), initialRow.getLocZ(), initialRow.getLocName());
            }
            Country nat = null;
            if (initialRow.getKillerNat() != null) {
                try {
                    nat = Country.valueOf(initialRow.getKillerNat().toUpperCase());
                } catch (Exception e) {
                }
            }
            this.killer = new Person(initialRow.getKillerName(), initialRow.getKillerBirthday(), initialRow.getKillerPassport(), nat, loc);
        } else {
            this.killer = null;
        }
    }

    public Scene createEditScene() {
        Font bigFont = Font.loadFont(getClass().getResourceAsStream("resources/fonts/aktifo.ttf"), 32);
        Font simpleFont = Font.loadFont(getClass().getResourceAsStream("resources/fonts/aktifo.ttf"), 16);
        Font idFont = Font.loadFont(getClass().getResourceAsStream("resources/fonts/aktifo.ttf"), 20);

        VBox root = new VBox(15);
        root.setStyle("-fx-background-color: " + BG_COLOR + ";");
        root.setPadding(new Insets(25));

        HBox topHeader = new HBox();
        topHeader.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label();
        titleLabel.textProperty().bind(LocalizationManager.createStringBinding("gui.edit.header"));
        titleLabel.setFont(bigFont);
        titleLabel.setTextFill(Color.WHITE);

        VBox idAndColorBox = new VBox(8);
        idAndColorBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(idAndColorBox, javafx.scene.layout.Priority.ALWAYS);

        Circle colorCircle = new Circle(14, ownerColor);
        colorCircle.setStroke(Color.BLACK);
        colorCircle.setStrokeWidth(1);

        Label idLabel = new Label();
        idLabel.setFont(idFont);
        idLabel.setTextFill(Color.WHITE);

        idAndColorBox.getChildren().addAll(colorCircle, idLabel);
        topHeader.getChildren().addAll(titleLabel, idAndColorBox);

        VBox mainContent = new VBox(10);
        mainContent.setPadding(new Insets(10, 0, 0, 10));

        nameVal = createValueLabel("", simpleFont);
        coordsVal = createValueLabel("", simpleFont);
        ageVal = createValueLabel("", simpleFont);
        weightVal = createValueLabel("", simpleFont);
        speakVal = createValueLabel("", simpleFont);
        colorVal = createValueLabel("", simpleFont);
        killerVal = createValueLabel("", simpleFont);

        HBox rowName = createFormRow(nameVal, "gui.edit.btn.modify", simpleFont, this::editBaseFields);
        HBox rowCoords = createFormRow(coordsVal, "gui.edit.btn.modify", simpleFont, this::editCoordinates);
        HBox rowAge = createFormRow(ageVal, "gui.edit.btn.modify", simpleFont, this::editBaseFields);
        HBox rowWeight = createFormRow(weightVal, "gui.edit.btn.modify", simpleFont, this::editBaseFields);
        HBox rowSpeak = createFormRow(speakVal, "gui.edit.btn.modify", simpleFont, this::editBaseFields);
        HBox rowColor = createFormRow(colorVal, "gui.edit.btn.modify", simpleFont, this::editBaseFields);
        HBox rowKiller = createFormRow(killerVal, "gui.edit.btn.modify", simpleFont, this::editKiller);

        mainContent.getChildren().addAll(rowName, rowCoords, rowAge, rowWeight, rowSpeak, rowColor, rowKiller);

        HBox bottomBar = new HBox();
        bottomBar.setAlignment(Pos.BOTTOM_RIGHT);

        Button deleteBtn = new Button();
        deleteBtn.textProperty().bind(LocalizationManager.createStringBinding("gui.edit.btn.delete"));
        deleteBtn.setFont(simpleFont);
        deleteBtn.setPrefWidth(180);
        deleteBtn.setStyle("-fx-background-color: #5b6d85; -fx-text-fill: white; -fx-cursor: hand;");
        deleteBtn.setOnAction(e -> {
            Main.getClientCore().executeCommand("remove_by_id", new Arg[]{new Arg(String.valueOf(id))});
            stage.close();
        });
        bottomBar.getChildren().add(deleteBtn);

        stage.setOnShowing(windowEvent -> {
            loadDataFromRow();
            idLabel.setText("ID: " + id);
            refreshLabelsText();

            boolean hasAccess = String.valueOf(creatorId).equals(ConfigManager.login) || "admin".equals(ConfigManager.role);
            deleteBtn.setDisable(!hasAccess);
            mainContent.getChildren().forEach(node -> {
                if (node instanceof HBox) {
                    ((HBox) node).getChildren().stream()
                            .filter(child -> child instanceof Button)
                            .forEach(btn -> btn.setDisable(!hasAccess));
                }
            });
        });

        root.getChildren().addAll(topHeader, mainContent, bottomBar);
        return new Scene(root, WIDTH, HEIGHT);
    }

    private Label createValueLabel(String text, Font font) {
        Label label = new Label(text);
        label.setFont(font);
        label.setTextFill(Color.WHITE);
        return label;
    }

    private HBox createFormRow(Label label, String btnKey, Font font, Runnable action) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        Button btn = new Button();
        btn.textProperty().bind(LocalizationManager.createStringBinding(btnKey));
        btn.setFont(font);
        btn.setPrefWidth(180);
        btn.setStyle("-fx-background-color: #5b6d85; -fx-text-fill: white; -fx-cursor: hand;");
        btn.setOnAction(e -> action.run());

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        hbox.getChildren().addAll(label, spacer, btn);
        return hbox;
    }

    /**
     * Метод обновления содержимого меток на основе текущих значений и локали.
     */
    private void refreshLabelsText() {
        String msgYes = LocalizationManager.getLocalizedMessage("gui.edit.yes");
        String msgNo = LocalizationManager.getLocalizedMessage("gui.edit.no");
        String msgNone = LocalizationManager.getLocalizedMessage("gui.edit.not_specified");
        String msgNoKiller = LocalizationManager.getLocalizedMessage("gui.edit.no_killer");

        nameVal.setText(LocalizationManager.getLocalizedMessage("gui.dragon.name") + ": " + name);
        coordsVal.setText(LocalizationManager.getLocalizedMessage("gui.edit.coordinates") + ": X=" + coordinates.getX() + ", Y=" + coordinates.getY());
        ageVal.setText(LocalizationManager.getLocalizedMessage("gui.dragon.age") + ": " + age);
        weightVal.setText(LocalizationManager.getLocalizedMessage("gui.dragon.weight") + ": " + (weight != null ? weight : msgNone));
        speakVal.setText(LocalizationManager.getLocalizedMessage("gui.dragon.speaking") + ": " + (speaking ? msgYes : msgNo));
        colorVal.setText(LocalizationManager.getLocalizedMessage("gui.dragon.color") + ": " + (color != null ? color : msgNone));
        killerVal.setText(LocalizationManager.getLocalizedMessage("gui.edit.killer") + ": " + (killer != null ? killer.getName() : msgNoKiller));
    }

    private void editBaseFields() {
        Stage inputStage = new Stage();
        inputStage.initModality(Modality.APPLICATION_MODAL);

        Dragon oldDragon = new Dragon(this.id, this.name, this.coordinates, this.creationDate, this.age, this.weight, this.speaking, this.color, this.killer);
        EnterDragonWindow edw = new EnterDragonWindow(inputStage, oldDragon);

        Dragon result = edw.showAndGetDragon();
        if (result != null) {
            Dragon pack = new Dragon(
                    this.id,
                    result.getName(),
                    result.getCoordinates() != null ? result.getCoordinates() : this.coordinates,
                    this.creationDate,
                    result.getAge(),
                    result.getWeight(),
                    result.getSpeaking(),
                    result.getColor(),
                    result.getKiller() != null ? result.getKiller() : this.killer
            );
            pack.setCreatorId(this.creatorId);
            sendUpdate(pack);
        }
    }

    private void editCoordinates() {
        Stage coordsStage = new Stage();
        coordsStage.initModality(Modality.APPLICATION_MODAL);

        EnterCoordinatesWindow ecw = new EnterCoordinatesWindow(coordsStage, this.coordinates);

        Coordinates newCords = ecw.showAndGetCoordinates();
        if (newCords != null) {
            Dragon pack = new Dragon(this.id, this.name, newCords, this.creationDate, this.age, this.weight, this.speaking, this.color, this.killer);
            pack.setCreatorId(this.creatorId);
            sendUpdate(pack);
        }
    }

    private void editKiller() {
        Stage killerStage = new Stage();
        killerStage.initModality(Modality.APPLICATION_MODAL);
        EnterPersonWindow epw = new EnterPersonWindow(killerStage, this.killer);

        Person newKiller = epw.showAndGetPerson();
        if (newKiller != null) {
            Dragon pack = new Dragon(this.id, this.name, this.coordinates, this.creationDate, this.age, this.weight, this.speaking, this.color, newKiller);
            pack.setCreatorId(this.creatorId);
            sendUpdate(pack);
        }
    }

    private void sendUpdate(Dragon fullDragon) {
        var response = Main.getClientCore().executeCommand("update", new Arg[]{
                new Arg(String.valueOf(fullDragon.getId())),
                new Arg(fullDragon)
        });

        if (response != null && response.isSuccess()) {
            this.name = fullDragon.getName();
            this.coordinates = fullDragon.getCoordinates();
            this.age = fullDragon.getAge();
            this.weight = fullDragon.getWeight();
            this.speaking = fullDragon.getSpeaking();
            this.color = fullDragon.getColor();
            this.killer = fullDragon.getKiller();

            refreshLabelsText();
        }
    }

    public void showAndWait() {
        stage.showAndWait();
    }
}