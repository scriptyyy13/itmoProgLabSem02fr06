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
     * Создание экземпляра {@code Reader} для стандартного потока ввода.
     */
    public Reader() {
        inputReader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Создание экземпляра {@code Reader} для стандартного потока ввода.
     */
    public Reader(String inputText) {
        inputReader = new BufferedReader(new StringReader(inputText));
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
