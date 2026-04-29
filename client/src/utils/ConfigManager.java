package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ConfigManager {
    public static String ip = "localhost";
    public static Integer port = 8085;
    public static Integer maxPacketSize = 65535;
    public static Integer serverResponseTimeout = 5000;

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
                } else if (paramName.equals("IP")) {
                    ip = paramValue;
                } else if (paramName.equals("MAX_PACKET_SIZE")) {
                    maxPacketSize = Integer.parseInt(paramValue);
                } else if (paramName.equals("TIMEOUT")) {
                    serverResponseTimeout = Integer.parseInt(paramValue);
                }
                OutputManager.println("Файл конфигурации был успешно считан");

            }
        } catch (Exception e) {
            OutputManager.errPrintln("Ошибка при чтении файла конфигурации " + filePath + ": " + e.getMessage());
        }
    }
}
