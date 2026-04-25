package tools;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import commands.Command;

import java.io.Serializable;

/**
 * Класс-оболочка для передачи команд и данных по сети.
 */
public class Message implements Serializable {
    private Command command;

    public Message() {
    }

    public Message(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }
}