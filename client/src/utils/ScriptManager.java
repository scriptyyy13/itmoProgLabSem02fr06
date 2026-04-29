package utils;

import commands.Command;

import java.io.*;
import java.util.*;

public class ScriptManager {
    private final CommandParser parser;
    private final Set<String> activeScripts = new HashSet<>();

    public ScriptManager(CommandParser parser) {
        this.parser = parser;
    }

    public List<Command> processScript(String filePath) {
        File file = new File(filePath);
        String absolutePath = file.getAbsolutePath();

        if (activeScripts.contains(absolutePath)) {
            OutputManager.errPrintln("Ошибка рекурсии: Скрипт " + filePath + " уже запущен!");
            return null;
        }

        activeScripts.add(absolutePath);
        List<Command> commands = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.trim().split("\\s+");
                if (parts[0].equals("execute_script")) {
                    if (parts.length > 1) {
                        List<Command> nested = processScript(parts[1]);
                        if (nested != null) commands.addAll(nested);
                    }
                } else {
                    Command cmd = CommandParser.parseCommand(line, null);
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