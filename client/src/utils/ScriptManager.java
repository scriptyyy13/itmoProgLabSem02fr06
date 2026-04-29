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

        if (activeScripts.contains(absolutePath)) {
            OutputManager.errPrintln("Ошибка рекурсии: Скрипт " + filePath + " уже запущен!");
            return null;
        }

        activeScripts.add(absolutePath);
        List<CommandRequest> commands = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                CommandRequest cmd = CommandParser.parseCommand(line, null);

                if (cmd instanceof ExecuteScript) {
                    String[] parts = line.split("\\s+");
                    if (parts.length > 1) {
                        List<CommandRequest> nested = processScript(parts[1]);
                        if (nested != null) commands.addAll(nested);
                    }
                } else {
                    commands.add(cmd);
                }
            }
        } catch (Exception e) {
            OutputManager.errPrintln("Ошибка при чтении файла " + filePath + ": " + e.getMessage());
            return null;
        } finally {
            activeScripts.remove(absolutePath);
        }
        return commands;
    }
}