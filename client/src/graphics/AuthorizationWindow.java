package graphics;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AuthorizationWindow extends Application {

    @Override
    public void start(Stage stage) {
        Label label = new Label("Привет, JavaFX!");
        Button button = new Button("Нажми меня");

        button.setOnAction(e -> label.setText("Кнопка нажата!"));

        VBox root = new VBox(10);
        root.getChildren().addAll(label, button);

        Scene scene = new Scene(root, 300, 150);

        stage.setTitle("Простое JavaFX-приложение");
        stage.setScene(scene);
        stage.show();
    }

}
