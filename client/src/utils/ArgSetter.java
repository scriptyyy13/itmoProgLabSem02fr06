package utils;

import commands.*;
import exceptions.InvalidInputException;
import tools.Arg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class ArgSetter {

    public static void setArgs(Command command, Arg[] args, Reader reader) {
        if (command instanceof AddRequest || command instanceof AddIfMaxRequest || command instanceof AddIfMinRequest) {
            InputManager.clearBuffer();
            if (args.length > 0) {
                String[] stringArgs = Arrays.stream(args).map(a -> a.getValue().toString()).toArray(String[]::new);
                InputManager.loadArgs(stringArgs);
            }
            command.setArgs(new Arg(InputManager.inputDragon(reader)));
        }
        else if (command instanceof UpdateRequest) {
            command.setArgs(args[0], new Arg(InputManager.inputDragon(reader)));
        }
        else if (command instanceof ExecuteScript) {
            try {
                String path = (String) args[0].getValue();
                command.setArgs(new Arg(Files.readString(Paths.get(path))));
            } catch (IOException e) {
                throw new InvalidInputException("Файл не найден");
            }
        }
        else {
            command.setArgs(args);
        }
    }
}
