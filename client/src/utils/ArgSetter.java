package utils;

import commands.*;
import sharedTools.Arg;

import java.util.Arrays;

/**
 * Устанавливает аргументы команды.
 */
public class ArgSetter {

    /**
     * Заполняет команду необходимыми аргументами.
     *
     * @param command объект запроса команды.
     * @param args    базовые строковые аргументы из строки ввода.
     * @param reader  источник данных (строка из UI).
     */
    public static void setArgs(CommandRequest command, Arg[] args, Reader reader) {
        if (command instanceof AddRequest || command instanceof AddIfMaxRequest || command instanceof AddIfMinRequest) {
            command.setArgs(new Arg(InputManager.inputDragon(reader)));
        } else if (command instanceof UpdateRequest) {
            command.setArgs(args[0], new Arg(InputManager.inputDragon(reader)));
        } else {
            command.setArgs(args);
        }
    }
}