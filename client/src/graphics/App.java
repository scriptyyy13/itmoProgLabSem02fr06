package graphics;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Java & Drakonchiki®");
        AuthorizationWindow authorizationWindow = new AuthorizationWindow(stage);
        authorizationWindow.show();
    }
}
