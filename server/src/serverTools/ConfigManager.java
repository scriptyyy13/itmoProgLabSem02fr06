package serverTools;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Класс отвечающий за работу с конфигом.
 */
public class ConfigManager {
    /**
     * Порт сервера.
     */
    public static Integer port = 8001;
    /**
     * @deprecated Путь до файла-коллекции.
     */
    @Deprecated
    public static String collectionFile = "collection.xml";
    /**
     * Размер пакета с {@code Message}
     */
    public static Integer messageBufferCapacity = 65535;
    /**
     * Размер пакета с {@code Command}
     */
    public static Integer commandsBufferCapacity = 1024;
    /**
     * Ссылка на базу данных.
     */
    public static String dbUrl = "jdbc:postgresql://localhost:5432/studs";
    /**
     * Логин для базы данных.
     */
    public static String dbLogin = "postgres";
    /**
     * Пароль для базы данных.
     */
    public static String dbPassword = "admin";
    /**
     * Кол-во потоков для получения команд для обработки.
     */
    public static Integer readingPoolCapacity = 4;
    /**
     * Кол-во потоков для отправки ответа по работе команды.
     */
    public static Integer sendingPoolCapacity = 4;
    /**
     * Размер буфера для получаемых на обработку команд.
     */
    public static Integer requestBufferCapacity = 1000;
    /**
     * Размер буфера для ответа по работе команды.
     */
    public static Integer resultBufferCapacity = 1000;
    /**
     * Кол-во потоков для выполнения команд.
     */
    public static Integer workingPoolCapacity = 4;
    /**
     * Секретный ключ для генерации токенов на сервере.
     */
    public static String tokenSecretKey = "THE_MOST_SECRET_SECRET";

    /**
     * Сканирование файла конфига
     *
     * @param filePath путь до файла коллекции.
     */
    public static void scanConfig(String filePath) {
        File file = new File(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("=", 2);
                if (parts.length < 2) continue;

                String paramName = parts[0].trim();
                String paramValue = parts[1].trim();

                if (paramName.equals("PORT")) {
                    port = Integer.parseInt(paramValue);
                } else if (paramName.equals("COLLECTION_FILE")) {
                    collectionFile = paramValue;
                } else if (paramName.equals("MESSAGE_BUFFER_CAPACITY")) {
                    messageBufferCapacity = Integer.parseInt(paramValue);
                } else if (paramName.equals("COMMANDS_BUFFER_CAPACITY")) {
                    commandsBufferCapacity = Integer.parseInt(paramValue);
                } else if (paramName.equals("DB_URL")) {
                    dbUrl = paramValue;
                } else if (paramName.equals("DB_LOGIN")) {
                    dbLogin = paramValue;
                } else if (paramName.equals("DB_PASSWORD")) {
                    dbPassword = paramValue;
                } else if (paramName.equals("READING_POOL_CAPACITY")) {
                    readingPoolCapacity = Integer.parseInt(paramValue);
                } else if (paramName.equals("SENDING_POOL_CAPACITY")) {
                    sendingPoolCapacity = Integer.parseInt(paramValue);
                } else if (paramName.equals("REQUEST_BUFFER_CAPACITY")) {
                    requestBufferCapacity = Integer.parseInt(paramValue);
                } else if (paramName.equals("RESULT_BUFFER_CAPACITY")) {
                    resultBufferCapacity = Integer.parseInt(paramValue);
                } else if (paramName.equals("WORKING_POOL_CAPACITY")) {
                    workingPoolCapacity = Integer.parseInt(paramValue);
                } else if (paramName.equals("TOKEN_SECRET_KEY")) {
                    tokenSecretKey = paramValue;
                }
            }
            System.out.println("Файл конфигурации был успешно считан");
        } catch (Exception e) {
            System.out.println("Ошибка при чтении файла конфигурации " + filePath + ": " + e.getMessage());
        }
    }
}
