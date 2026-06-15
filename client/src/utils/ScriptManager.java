package utils;

import commands.CommandRequest;

import java.io.*;
import java.util.*;

/**
 * Класс для работы со скриптами.
 */
public class ScriptManager {

    /** Парсер команды. */
    private final CommandParser parser;

    /** Активные скрипты для предотвращения зацикливания. */
    private final Set<String> activeScripts = new HashSet<>();

    /**
     * Конструктор менеджера скриптов.
     * @param parser экземпляр парсера команд.
     */
    public ScriptManager(CommandParser parser) {
        this.parser = parser;
    }

    /**
     * Обрабатывает скрипт и преобразует его строки в список объектов запросов.
     * @param filePath путь до файла скрипта.
     * @return Список реквестов команд.
     */
    public List<CommandRequest> processScript(String filePath) {
        File file = new File(filePath);
        String absolutePath = file.getAbsolutePath();

        // Проверка на рекурсию — отдаем ключ ошибки
        if (activeScripts.contains(absolutePath)) {
            throw new RuntimeException("error.script.recursion");
        }

        activeScripts.add(absolutePath);
        List<CommandRequest> commands = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            Reader scriptReader = new Reader(bufferedReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split("\\s+");
                String commandName = parts[0];

                if (commandName.equals("execute_script")) {
                    if (parts.length > 1) {
                        List<CommandRequest> nested = processScript(parts[1]);
                        if (nested != null) {
                            commands.addAll(nested);
                        }
                    } else {
                        throw new RuntimeException("error.script.missing_path");
                    }
                } else {
                    CommandRequest cmd = parser.parseCommand(line, scriptReader);
                    if (cmd != null) {
                        commands.add(cmd);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("error.script.file_not_found", e);
        } catch (IOException e) {
            throw new RuntimeException("error.script.read_failed", e);
        } finally {
            activeScripts.remove(absolutePath);
        }

        return commands;
    }
}