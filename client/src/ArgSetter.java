
import commands.*;
import exceptions.InvalidInputException;
import tools.Arg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ArgSetter {

    public static void setArgs(Command command, Arg[] args, Reader reader) {
        switch (command) {
            case Add a:
                command.setArgs(new Arg(InputManager.inputDragon(reader)));
                break;
            case AddIfMax amx:
                command.setArgs(new Arg(InputManager.inputDragon(reader)));
                break;
            case AddIfMin amn:
                command.setArgs(new Arg(InputManager.inputDragon(reader)));
                break;
            case Update u:
                command.setArgs(args[0], new Arg(InputManager.inputDragon(reader)));
                break;
            case ExecuteScript ex:
                try {
                    command.setArgs(new Arg(Files.readString(Paths.get((String)args[0].getValue()))));
                } catch (IOException e) {
                    throw new InvalidInputException("Файл не найден");
                }
                break;
            default:
                command.setArgs(args);
                break;
        }
    }
}
