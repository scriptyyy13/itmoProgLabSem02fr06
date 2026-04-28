import clientCommands.ClientCommandManager;
import network.UDPClient;
import utils.OutputManager;
import utils.Reader;
import clientCommands.ClientCommandManager.*;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;

public class Main {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 8080;

        if (args.length > 0) { // возможность передать адрес и порт как аргумент при запуске в формате ADDRESS:PORT
            String[] parts = args[0].split(":");
            host = parts[0];
            if (parts.length > 1) {
                try {
                    port = Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    OutputManager.errPrintln("Порт должен быть числом! Используется дефолтный: 8080");
                    port = 8080;
                }
            }
        }

        Reader consoleReader = new Reader();
        OutputManager.println("Добро пожаловать в клиентское приложение!");
        OutputManager.printf("Используется сервер: %s, порт: %d\n", host, port);

        try {
            UDPClient udpClient = new UDPClient(host, port);
            ClientCommandManager commandManager = new ClientCommandManager(consoleReader, udpClient);

            OutputManager.println("Введите команду (или 'help' для списка доступных команд):");
            commandManager.start();

        } catch (UnknownHostException e) {
            OutputManager.errPrintln("Критическая ошибка: Не удалось определить адрес хоста: " + host);
        } catch (SocketException e) {
            OutputManager.errPrintln("Критическая ошибка: Проблема с сетевым сокетом: " + e.getMessage());
        } catch (NoSuchElementException e) {
            OutputManager.errPrintln("\nВвод прерван\n Завершение работы.");
        } catch (Exception e) {
            OutputManager.errPrintln("Критическая ошибка в работе клиента: " + e.getMessage());
        } finally {
            OutputManager.println("Клиент остановлен.");
        }
    }
}