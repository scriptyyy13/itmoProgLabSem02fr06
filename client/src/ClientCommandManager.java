import commands.*;
import exceptions.InvalidInputException;
import tools.Arg;

import java.io.File;
import java.util.*;

public class ClientCommandManager {

    private final Reader consoleReader;
    private final Set<String> activeScripts = new HashSet<>(); // абсолютные пути активных скриптов

    public ClientCommandManager(Reader reader) {
        this.consoleReader = reader;
    }

    public void start() {
        while (true) {
            try {
                String str = consoleReader.getLine();
                if (str == null) break;

                // парсим и подготавливаем одну команду из консоли
                Command command = parseCommand(str, consoleReader);
                if (command == null) continue;

                if (command instanceof Exit) {
                    OutputManager.println("Завершение работы клиента...");
                    break;
                }

                if (command instanceof ExecuteScript) {
                    String[] splitted = str.trim().split("\\s+");
                    if (splitted.length < 2) throw new InvalidInputException("Не указан путь к файлу скрипта.");
                    String filePath = splitted[1];

                    // процессинг команд
                    List<Command> scriptCommands = processScript(filePath);

                    if (scriptCommands != null) {
                        OutputManager.println("Всего в скрипте: " + scriptCommands.size() + " команд.");
                        /*
                           тут крч отправку скрипта делаем
                        */
                    } else {
                        OutputManager.errPrintln("Отправка скрипта отменена из-за ошибок.");
                    }
                } else {
                    // OutputManager.println("Команда " + command.getClass().getSimpleName() + " подготовлена к отправке.");
                    /*
                           тут крч отправку делаем обычных команд
                    */
                }
            } catch (InvalidInputException e) {
                OutputManager.println(e.getMessage());
            } catch (Exception e) {
                OutputManager.println("Критическая ошибка ввода: " + e.getMessage());
            }
        }
    }

    /**
     * Универсальный метод для парсинга строки в команду
     */
    private Command parseCommand(String line, Reader currentReader) throws InvalidInputException {
        line = line.trim();
        if (line.isEmpty()) return null;

        String[] splittedStr = line.split("\\s+");
        String commandName = splittedStr[0];

        // ищем команду в энаме
        ClientCommandType type = ClientCommandType.fromString(commandName);
        if (type == null) {
            throw new InvalidInputException("Команда '" + commandName + "' не найдена.");
        }

        // создаем новый экземпляр команды
        Command command = type.create();

        // парсим аргументы
        Arg[] args = Arg.toArgList(Arrays.copyOfRange(splittedStr, 1, splittedStr.length));
        ArgSetter.setArgs(command, args, currentReader);

        // валидация перед упаковкой
        command.validate();

        return command;
    }

    /**
     * Метод для чтения и препроцессинга скрипта.
     * Возвращает список проверенных команд или null, если хотя бы одна с ошибкой.
     */
    private List<Command> processScript(String filePath) {
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