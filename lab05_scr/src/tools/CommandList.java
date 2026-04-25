package tools;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import commands.Command;

import java.util.ArrayList;

/**
 * Представляет список команд.
 */
@JacksonXmlRootElement(localName = "commands")
public class CommandList {
    /**
     * Коллекция команд.
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "command")
    private ArrayList<Command> commands;

    /**
     * Создание экземпляра {@code CommandList}
     */
    public CommandList(ArrayList<Command> list) {
        this.commands = list;
    }

    public CommandList() {
        this.commands = new ArrayList<Command>();
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }

    public void setCommands(ArrayList<Command> list) {
        commands = list;
    }
}
