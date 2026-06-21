package utils;

import clientCommands.ClientCommandType;
import commands.CommandRequest;
import sharedTools.Arg;

import java.util.Arrays;

/**
 * Создает экземпляр команды на основе текстовой строки.
 */
public class CommandParser {

    /**
     * Парсит строку и создает готовый объект CommandRequest.
     *
     * @param commandName   строка с командой и ее первыми аргументами.
     * @param args аргументы аче))
     * @return Сформированный объект запроса команды.
     */
    public CommandRequest parseCommand(String commandName, Arg[] args) {
        if (commandName == null) {
            throw new RuntimeException("error.command.null");
        }

        commandName = commandName.trim();
        if (commandName.isEmpty()) {
            throw new RuntimeException("error.command.empty");
        }


        // Ищем команду в энаме
        ClientCommandType type = ClientCommandType.fromString(commandName);
        if (type == null) {
            throw new RuntimeException("error.command.not_found");
        }

        // Создаем новый экземпляр команды
        CommandRequest command = type.create();

        command.setArgs(args);

        // Валидация перед отправкой
        try {
            command.validate();
        } catch (Exception e) {
            throw new RuntimeException("error.command.validation_failed", e);
        }

        return command;
    }

    public CommandRequest parseCommand(String line, Reader currentReader) {
        if (line == null) {
            throw new RuntimeException("error.command.null");
        }

        line = line.trim();
        if (line.isEmpty()) {
            throw new RuntimeException("error.command.empty");
        }

        String[] splittedStr = line.split("\\s+");
        String commandName = splittedStr[0];

        // Ищем команду в энаме
        ClientCommandType type = ClientCommandType.fromString(commandName);
        if (type == null) {
            throw new RuntimeException("error.command.not_found");
        }

        // Создаем новый экземпляр команды
        CommandRequest command = type.create();

        // Парсим аргументы
        Arg[] args = Arg.toArgList(Arrays.copyOfRange(splittedStr, 1, splittedStr.length));

        // Заполняем сложные аргументы
        ArgSetter.setArgs(command, args, currentReader);

        // Валидация перед отправкой
        try {
            command.validate();
        } catch (Exception e) {
            throw new RuntimeException("error.command.validation_failed", e);
        }

        return command;
    }
}