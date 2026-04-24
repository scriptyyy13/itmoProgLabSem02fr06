package tools;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import commands.Command;

/**
 * Класс-оболочка для передачи команд и данных по сети.
 */
public class Message {
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.CLASS,
            include = JsonTypeInfo.As.PROPERTY,
            property = "commandClass"
    )
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