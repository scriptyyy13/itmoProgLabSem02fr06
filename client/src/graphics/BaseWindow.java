package graphics;

import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Основа для всех окон.
 */
public abstract class BaseWindow {
    protected final Stage stage;
    protected final int WIDTH = 1280;
    protected final int HEIGHT = 720;

    public BaseWindow(Stage stage) {
        this.stage = stage;
    }

    /**
     * Основа для интерфейса остальных окон.
     */
    protected abstract Region buildUI();

    /**
     * Инициализация окна.
     */
    protected void initScene() {
        VBox mainLayout = new VBox();

        // Внедряем глобальное меню
        mainLayout.getChildren().add(LocalizationManager.createGlobalMenuBar());

        // Добавляем контент конкретного окна
        Region content = buildUI();
        if (content != null) {
            mainLayout.getChildren().add(content);
        }

        Scene scene = new Scene(mainLayout, WIDTH, HEIGHT);
        stage.setScene(scene);
    }

    public void show() {
        if (stage.getScene() == null) {
            initScene();
        }
        stage.show();
    }

    public void showAndWait() {
        if (stage.getScene() == null) {
            initScene();
        }
        stage.showAndWait();
    }

    public void close() {
        stage.close();
    }
}