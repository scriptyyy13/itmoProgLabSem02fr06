package clientMainFiles;

import core.ClientCore;
import javafx.application.Application;
import javafx.stage.Stage;
import network.UDPClient;
import utils.ConfigManager;

import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Main-класс клиента.
 */
public class Main extends Application {

    /**
     * Ядро клиента для работы с командами.
     */
    private static ClientCore clientCore;

    /**
     * Сетевой клиент для работы с сокетом.
     */
    private static UDPClient udpClient;

    /**
     * Точка входа в программу.
     */
    public static void main(String[] args) {
        // Считываем конфиг, если передан аргумент
        if (args.length > 0) {
            ConfigManager.scanConfig(args[0]);
        }

        try {
            // Инициализируем сетевое взаимодействие и ядро
            udpClient = new UDPClient(ConfigManager.ip, ConfigManager.port);
            clientCore = new ClientCore(udpClient);

            // Запускаем цикл интерфейса
            launch(args);

        } catch (UnknownHostException e) {
            throw new RuntimeException("error.network.unknown_host", e);
        } catch (SocketException e) {
            throw new RuntimeException("error.network.socket_failed", e);
        } catch (Exception e) {
            // Общая критическая ошибка инициализации
            throw new RuntimeException("error.internal.startup_failed", e);
        } finally {
            // Гарантированно закрываем сокет при выходе из приложения
            if (udpClient != null) {
                udpClient.close();
            }
        }
    }

    /**
     * Инициализация и запуск графического интерфейса.
     *
     * @param stage главное окно приложения (предоставляется JavaFX).
     */
    @Override
    public void start(Stage stage) throws Exception {
        // сюда графику
    }

    /**
     * Возвращает экземпляр ядра клиента для использования в графических окнах.
     *
     * @return Объект ClientCore.
     */
    public static ClientCore getClientCore() {
        return clientCore;
    }
}