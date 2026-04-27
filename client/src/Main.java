import exceptions.InvalidInputException;
import java.util.NoSuchElementException;

public class Main {
    public static void main(String[] args) {
        Reader consoleReader = new Reader();
        OutputManager.println("Добро пожаловать в клиентское приложение!");

        /* тут для сети */

        ClientCommandManager commandManager = new ClientCommandManager(consoleReader);

        try {
            OutputManager.println("Введите команду (или 'help' для списка доступных команд):");
            commandManager.start();
        } catch (NoSuchElementException e) {
            OutputManager.errPrintln("\nЗавершение работы клиента.");
        } catch (Exception e) {
            OutputManager.errPrintln("Критическая ошибка в работе клиента: " + e.getMessage());
        } finally {
            OutputManager.println("Клиент остановлен.");
        }
    }
}