package utils;

import commands.Command;
import commands.ExecuteScript;
import exceptions.InvalidInputException;
import java.io.File;
import java.util.*;

import static utils.CommandParser.parseCommand;

public class ScriptManager {
    private final CommandParser parser;
    private final Set<String> activeScripts = new HashSet<>();

    public ScriptManager(CommandParser parser) {
        this.parser = parser;
    }

    /**
     * Метод для чтения и препроцессинга скрипта.
     * Возвращает список проверенных команд или null, если хотя бы одна с ошибкой.
     */
    public List<Command> processScript(String filePath) {
        File scriptFile = new File(filePath);
        String absolutePath = scriptFile.getAbsolutePath();

        // запрет на бесконечную рекурсию
        if (activeScripts.contains(absolutePath)) {
            OutputManager.errPrintln("Ошибка рекурсии: Скрипт '" + filePath + "' пытается вызвать сам себя!");
            return null;
        }

        activeScripts.add(absolutePath);
        List<Command> preparedCommands = new ArrayList<>();

        try {
            Reader fileReader = new Reader(filePath);
            String line;

            while ((line = fileReader.getLine()) != null) {
                Command cmd = parseCommand(line, fileReader);
                if (cmd == null) continue;

                // процессинг рекурсивного вызова
                if (cmd instanceof ExecuteScript) {
                    String[] splitted = line.trim().split("\\s+");
                    if (splitted.length < 2) {
                        OutputManager.errPrintln("Ошибка внутри скрипта: не указан файл для execute_script.");
                        return null;
                    }
                    String nestedFilePath = splitted[1];

                    List<Command> nestedCommands = processScript(nestedFilePath);
                    if (nestedCommands == null) {
                        return null; // если вложенный скрипт сломался отменяем весь верхний
                    }
                    preparedCommands.addAll(nestedCommands);
                } else {
                    preparedCommands.add(cmd);
                }
            }
        } catch (InvalidInputException e) {
            OutputManager.errPrintln("Ошибка валидации в скрипте '" + filePath + "': " + e.getMessage());
            return null; // прерывание
        } catch (Exception e) {
            OutputManager.errPrintln("Ошибка чтения скрипта '" + filePath + "': " + e.getMessage());
            return null;
        } finally {
            // удаление скрипта после того как прошлись по нему
            activeScripts.remove(absolutePath);
        }

        return preparedCommands;
    }
}