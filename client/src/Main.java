import clientCommands.ClientCommandManager;
import network.UDPClient;
import utils.ConfigManager;
import utils.OutputManager;
import utils.Reader;
import clientCommands.ClientCommandManager.*;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0) {
            ConfigManager.scanConfig(args[0]);
        }

        Reader consoleReader = new Reader();
        OutputManager.println("Добро пожаловать в клиентское приложение!");
        OutputManager.printf("Используется сервер: %s, порт: %d\n", ConfigManager.ip, ConfigManager.port);

        try {
            UDPClient udpClient = new UDPClient(ConfigManager.ip, ConfigManager.port);
            ClientCommandManager commandManager = new ClientCommandManager(consoleReader, udpClient);

            OutputManager.println("Введите команду (или 'help' для списка доступных команд):");
            commandManager.start();

        } catch (UnknownHostException e) {
            OutputManager.errPrintln("Критическая ошибка: Не удалось определить адрес хоста: " + ConfigManager.ip);
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