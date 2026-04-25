/**
 * Класс для вывода информации в консоль через единый интерфейс.
 *
 * author Ыскшзеннн
 * version 1.0
 */
public class OutputManager {
    /**
     * Конструктор OutputManager.
     */
    private OutputManager() {}

    /**
     * Выводит строку в стандартный поток.
     */
    public static void println(String message) {
        System.out.println(message);
    }

    /**
     * Выводит сообщение без новой строки.
     */
    public static void print(String message) {
        System.out.print(message);
    }

    /**
     * Выводит сообщение с форматированием.
     */
    public static void printf(String format, Object... args) {
        System.out.printf(format, args);
    }

    /**
     * Выводит строку в поток ошибок.
     */
    public static void errPrintln(String message) {
        System.err.println("Ошибка: " + message);
    }

    /**
     * Выводит сообщение в поток ошибок без новой строки.
     */
    public static void errPrint(String message) {
        System.err.print("Ошибка: " + message);
    }
}