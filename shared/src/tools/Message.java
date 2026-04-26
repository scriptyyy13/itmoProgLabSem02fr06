package tools;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import commands.Command;

import java.io.Serializable;

/**
 * Класс-оболочка для передачи команд и данных по сети.
 */
public class Message implements Serializable {
    private String text;

    public Message() {
    }

    public Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}