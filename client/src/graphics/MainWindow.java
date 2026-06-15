package graphics;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainWindow {
    private Stage stage;

    public MainWindow(Stage stage){
        this.stage = stage;
    }

    private Scene createMainScene(){
        Font titleFont = Font.loadFont(getClass().getResourceAsStream("fonts/aktifo.ttf"),90);
        Font buttonFont = Font.loadFont(getClass().getResourceAsStream("fonts/aktifo.ttf"),20);
        Font fieldFont = Font.loadFont(getClass().getResourceAsStream("fonts/aktifo.ttf"),17);


        BorderPane root = new BorderPane();
        HBox topbar = new HBox();
        Label welcome = new Label("Добро пожаловать, Username");

        return new Scene(root);
    }
    public void show(){
        stage.setScene(createMainScene());
        stage.setResizable(false);
        stage.show();
    }
}
