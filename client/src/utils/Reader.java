package utils;

import java.io.*;

/**
 * Класс, реализующий построчное чтение данных из потоков ввода.
 */
public class Reader {
    /**
     * Реализует чтение текста.
     */
    private BufferedReader inputReader;

    /**
     * Создание экземпляра {@code utils.Reader} для стандартного потока ввода.
     */
    public Reader() {
        inputReader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Создание экземпляра {@code utils.Reader} для переданной строки текста.
     */
    public Reader(String inputText) {
        inputReader = new BufferedReader(new StringReader(inputText));
    }

    /**
     * Создание экземпляра {@code utils.Reader} на основе существующего BufferedReader.
     */
    public Reader(BufferedReader bufferedReader) {
        this.inputReader = bufferedReader;
    }

    /**
     * Читает строку ввода.
     *
     * @return вводимая строка.
     */
    public String getLine() {
        try {
            return inputReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("error.read.line_failed", e);
        }
    }
}