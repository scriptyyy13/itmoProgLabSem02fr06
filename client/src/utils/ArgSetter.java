package utils;

import commands.*;
import tools.Arg;

import java.util.Arrays;

public class ArgSetter {

    public static void setArgs(CommandRequest command, Arg[] args, Reader reader) {
        if (command instanceof AddRequest || command instanceof AddIfMaxRequest || command instanceof AddIfMinRequest) {
            InputManager.clearBuffer();
            if (args.length > 0) {
                String[] stringArgs = Arrays.stream(args).map(a -> a.getValue().toString()).toArray(String[]::new);
                InputManager.loadArgs(stringArgs);
            }
            command.setArgs(new Arg(InputManager.inputDragon(reader)));
        } else if (command instanceof UpdateRequest) {
            command.setArgs(args[0], new Arg(InputManager.inputDragon(reader)));
        } else {
            command.setArgs(args);
        }
    }
}
