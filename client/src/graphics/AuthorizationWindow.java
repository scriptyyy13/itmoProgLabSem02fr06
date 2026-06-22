package graphics;

import javafx.application.Application;
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

import java.text.NumberFormat;

public class AuthorizationWindow {
    private Stage stage;
    private final String BG_COLOR = "#2a3950";
    private final String CARD_COLOR = "#43506C";
    private final int WIDTH = 1280;
    private final int HEIGHT = 720;
    public Button loginBtn;
    public Button regBtn;
    public TextField username;
    public TextField password;
    public Label error;

    public AuthorizationWindow(Stage stage){

        this.stage = stage;
        this.stage.setResizable(false);
        this.stage.setScene(createLoginScene());
    }

    private Scene createLoginScene(){
        Font titleFont = Font.loadFont(getClass().getResourceAsStream("fonts/aktifo.ttf"),90);
        Font buttonFont = Font.loadFont(getClass().getResourceAsStream("fonts/aktifo.ttf"),20);
        Font fieldFont = Font.loadFont(getClass().getResourceAsStream("fonts/aktifo.ttf"),17);
        VBox root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: " + BG_COLOR + ";");
        Label title = new Label("Java & Drakonchiki");
        title.setFont(titleFont);
        title.setTextFill(Color.WHITE);
        VBox.setMargin(title, new Insets(70,0,100,0));


        VBox card = new VBox(40);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: " + CARD_COLOR+ "; -fx-background-radius: 20;");
        card.setMaxWidth(600);
        card.setPrefHeight(300);

        HBox row = new HBox(50);
        row.setAlignment(Pos.CENTER);
        VBox buttons = new VBox(15);
        loginBtn = new Button("Вход");
        regBtn = new Button("Регистрация");
        buttons.getChildren().addAll(loginBtn,regBtn);
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
        username.setPromptText("Имя пользователя");
        password.setPromptText("Пароль");
        username.setFont(fieldFont);
        password.setFont(fieldFont);
        username.setPrefHeight(40);
        password.setPrefHeight(40);

        fields.getChildren().addAll(username,password);
        row.getChildren().addAll(buttons,fields);


        card.getChildren().addAll(row,error);

        Label authors = new Label("by scripty, prikolist667");
        authors.setFont(fieldFont);
        authors.setTextFill(Color.WHITE);
        VBox.setMargin(authors, new Insets(100,0,0,0));
        root.getChildren().addAll(title,card,authors);
        return new Scene(root,WIDTH,HEIGHT);
    }

    public void showAndWait(){
        stage.showAndWait();
    }

    public void createScene(){
        stage.setResizable(false);
        stage.setScene(createLoginScene());
    }

    public void close(){
        stage.close();
    }

}
