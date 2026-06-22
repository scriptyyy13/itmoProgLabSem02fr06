package graphics;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AuthorizationWindow extends BaseWindow {
    private final String BG_COLOR = "#2a3950";
    private final String CARD_COLOR = "#43506C";

    public Button loginBtn;
    public Button regBtn;
    public TextField username;
    public TextField password;
    public Label error;

    private boolean authenticated = false;

    public AuthorizationWindow(Stage stage) {
        super(stage);
        this.stage.setResizable(false);
        initScene();
    }

    @Override
    protected Region buildUI() { // Переопределяем buildUI
        Font titleFont = Font.loadFont(getClass().getResourceAsStream("resources/fonts/aktifo.ttf"), 90);
        Font buttonFont = Font.loadFont(getClass().getResourceAsStream("resources/fonts/aktifo.ttf"), 20);
        Font fieldFont = Font.loadFont(getClass().getResourceAsStream("resources/fonts/aktifo.ttf"), 17);

        VBox root = new VBox();
        root.setPrefSize(WIDTH, HEIGHT - 25);
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: " + BG_COLOR + ";");

        Label title = new Label("Java & Drakonchiki");
        title.setFont(titleFont);
        title.setTextFill(Color.WHITE);
        VBox.setMargin(title, new Insets(70, 0, 100, 0));

        VBox card = new VBox(40);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 20;");
        card.setMaxWidth(600);
        card.setPrefHeight(300);

        HBox row = new HBox(50);
        row.setAlignment(Pos.CENTER);
        VBox buttons = new VBox(15);
        loginBtn = new Button();
        regBtn = new Button();

        loginBtn.textProperty().bind(LocalizationManager.createStringBinding("gui.auth.login_btn"));
        regBtn.textProperty().bind(LocalizationManager.createStringBinding("gui.auth.register_btn"));

        buttons.getChildren().addAll(loginBtn, regBtn);
        buttons.setAlignment(Pos.CENTER);

        error = new Label("");
        error.setFont(fieldFont);
        error.setTextFill(Color.RED);

        loginBtn.setPrefWidth(200);
        loginBtn.setPrefHeight(60);
        loginBtn.setStyle("-fx-background-radius: 5;");
        loginBtn.setFont(buttonFont);

        regBtn.setPrefWidth(200);
        regBtn.setPrefHeight(60);
        regBtn.setStyle("-fx-background-radius: 5;");
        regBtn.setFont(buttonFont);

        VBox fields = new VBox(35);
        username = new TextField();
        password = new TextField();

        username.promptTextProperty().bind(LocalizationManager.createStringBinding("gui.auth.username_prompt"));
        password.promptTextProperty().bind(LocalizationManager.createStringBinding("gui.auth.password_prompt"));

        username.setFont(fieldFont);
        password.setFont(fieldFont);
        username.setPrefHeight(40);
        password.setPrefHeight(40);

        fields.getChildren().addAll(username, password);
        row.getChildren().addAll(buttons, fields);
        card.getChildren().addAll(row, error);

        Label authors = new Label("by scriptyyy, prikolist667");
        authors.setFont(fieldFont);
        authors.setTextFill(Color.WHITE);
        VBox.setMargin(authors, new Insets(100, 0, 0, 0));
        root.getChildren().addAll(title, card, authors);

        return root;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticatedSuccess() {
        this.authenticated = true;
        close();
    }
}