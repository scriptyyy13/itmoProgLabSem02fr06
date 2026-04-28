
import commands.*;
import exceptions.InvalidInputException;
import tools.Arg;
import utils.InputManager;
import utils.Reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ArgSetter {

    public static void setArgs(Command command, Arg[] args, Reader reader) {
        if (command instanceof Add || command instanceof AddIfMax || command instanceof AddIfMin) {
            command.setArgs(new Arg(InputManager.inputDragon(reader)));
        }
        else if (command instanceof Update) {
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
