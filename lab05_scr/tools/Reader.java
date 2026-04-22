package tools;

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
     * Путь до скрипта, если чтение идёт из него.
     */
    private String scriptPath;

    /**
     * Создание экземпляра {@code Reader} для стандартного потока ввода.
     */
    public Reader() {
        inputReader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Создание экземпляра {@code Reader} для чтения из скрипта.
     */
    public Reader(String scriptPath) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(scriptPath);
        inputReader = new BufferedReader(new InputStreamReader(fileInputStream));
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
            OutputManager.println(e.getMessage());
            OutputManager.println("Не удалось прочесть строку");
        }
        return null;
    }
}
