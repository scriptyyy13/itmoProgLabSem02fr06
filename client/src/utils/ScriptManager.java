package utils;

import commands.CommandRequest;

import java.io.*;
import java.util.*;

public class ScriptManager {
    private final CommandParser parser;
    private final Set<String> activeScripts = new HashSet<>();

    public ScriptManager(CommandParser parser) {
        this.parser = parser;
    }

    public List<CommandRequest> processScript(String filePath) {
        File file = new File(filePath);
        String absolutePath = file.getAbsolutePath();

        // Проверка на рекурсию
        if (activeScripts.contains(absolutePath)) {
            OutputManager.errPrintln("Ошибка рекурсии: Скрипт " + filePath + " уже запущен!");
            return Collections.emptyList();
        }

        activeScripts.add(absolutePath);
        List<CommandRequest> commands = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            Reader scriptReader = new Reader(bufferedReader);

            String line;
            // Читаем файл построчно
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split("\\s+");
                String commandName = parts[0];

                if (commandName.equals("execute_script")) {
                    if (parts.length > 1) {
                        List<CommandRequest> nested = processScript(parts[1]);
                        if (nested != null) commands.addAll(nested);
                    } else {
                        OutputManager.errPrintln("Ошибка: В скрипте не указан путь для execute_script");
                    }
                } else {
                    CommandRequest cmd = CommandParser.parseCommand(line, scriptReader);
                    if (cmd != null) {
                        commands.add(cmd);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            OutputManager.errPrintln("Ошибка: Файл скрипта не найден: " + filePath);
        } catch (IOException e) {
            OutputManager.errPrintln("Ошибка при чтении файла " + filePath + ": " + e.getMessage());
        } finally {
            activeScripts.remove(absolutePath);
        }

        return commands;
    }
}