package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ConfigManager {
    public static Integer port = 8085;
    public static String collectionFile = "collection.xml";
    public static Integer messageBufferCapacity = 65535;
    public static Integer commandsBufferCapacity = 1024;

    public static void scanConfig(String filePath) {
        File file = new File(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("=", 2);

                String paramName = parts[0];
                String paramValue = parts[1];

                if (paramName.equals("PORT")) {
                    port = Integer.parseInt(paramValue);
                } else if (paramName.equals("COLLECTION_FILE")) {
                    collectionFile = paramValue;
                } else if (paramName.equals("MESSAGE_BUFFER_CAPACITY")) {
                    messageBufferCapacity = Integer.parseInt(paramValue);
                } else if (paramName.equals("COMMANDS_BUFFER_CAPACITY")) {
                    commandsBufferCapacity = Integer.parseInt(paramValue);
                }

            }
        } catch (Exception e) {
            System.out.println("Ошибка при чтении файла " + filePath + ": " + e.getMessage());
        }
    }
}
