package utils;

import clientCommands.ClientCommandType;
import commands.CommandRequest;
import exceptions.InvalidInputException;
import tools.Arg;

import java.util.Arrays;

public class CommandParser {
    public static CommandRequest parseCommand(String line, Reader currentReader) throws InvalidInputException {
        line = line.trim();

        String[] splittedStr = line.split("\\s+");
        String commandName = splittedStr[0];

        // ищем команду в энаме
        ClientCommandType type = ClientCommandType.fromString(commandName);
        if (type == null) {
            throw new InvalidInputException("Команда '" + commandName + "' не найдена.");
        }

        // создаем новый экземпляр команды
        CommandRequest command = type.create();

        // парсим аргументы
        Arg[] args = Arg.toArgList(Arrays.copyOfRange(splittedStr, 1, splittedStr.length));
        ArgSetter.setArgs(command, args, currentReader);

        // валидация перед упаковкой
        command.validate();

        return command;
    }
}