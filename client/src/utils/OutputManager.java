package utils;

/**
 * Класс для вывода информации в консоль через единый интерфейс.
 * <p>
 * author Ыскшзеннн
 * version 1.0
 */
public class OutputManager {

    private static Boolean doPrint = true;

    /**
     * Конструктор utils.OutputManager.
     */
    private OutputManager() {
    }

    /**
     * Включить вывод.
     */
    public static void enablePrinting() {
        doPrint = true;
    }

    /**
     * Выключить вывод.
     */
    public static void disablePrinting() {
        doPrint = false;
    }

    /**
     * Выводит строку в стандартный поток.
     */
    public static void println(String message) {
        if (!doPrint) {
            return;
        }
        System.out.println(message);
    }

    /**
     * Выводит сообщение без новой строки.
     */
    public static void print(String message) {
        if (!doPrint) {
            return;
        }
        System.out.print(message);
    }

    /**
     * Выводит сообщение с форматированием.
     */
    public static void printf(String format, Object... args) {
        if (!doPrint) {
            return;
        }
        System.out.printf(format, args);
    }

    /**
     * Выводит строку в поток ошибок.
     */
    public static void errPrintln(String message) {
        if (!doPrint) {
            return;
        }
        System.err.println("Ошибка: " + message);
    }

    /**
     * Выводит сообщение в поток ошибок без новой строки.
     */
    public static void errPrint(String message) {
        if (!doPrint) {
            return;
        }
        System.err.print("Ошибка: " + message);
    }
}