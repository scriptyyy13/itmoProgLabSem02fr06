package commands;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import exceptions.InvalidInputException;
import tools.Arg;
import tools.CollectionManager;

import java.io.Serializable;

/**
 * Абстрактный класс-предок всех команд.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type"
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = Add.class, name = "Add"),
        @JsonSubTypes.Type(value = AddIfMax.class, name = "AddIfMax"),
        @JsonSubTypes.Type(value = AddIfMin.class, name = "AddIfMin"),
        @JsonSubTypes.Type(value = Clear.class, name = "Clear"),
        @JsonSubTypes.Type(value = ExecuteScript.class, name = "ExecuteScript"),
        @JsonSubTypes.Type(value = RemoveById.class, name = "RemoveById"),
        @JsonSubTypes.Type(value = RemoveHead.class, name = "RemoveHead"),
        @JsonSubTypes.Type(value = Update.class, name = "Update"),
})

public abstract class Command implements Serializable {
    @JsonIgnore
    /**
     * {@link tools.CollectionManager}, в котором будет исполнена команда.
     */
    private CollectionManager manager;
    /**
     * Массив аргументов команды.
     */
    private Arg[] args;

    /**
     * Создание экземпляра команды
     *
     * @param manager {@link tools.CollectionManager}, в котором будет исполнена команда.
     */
    public Command(CollectionManager manager) {
        this.manager = manager;
    }

    public Command() {
    }

    /**
     * Исполнение команды.
     */
    public void execute() {
    }

    /**
     * Валидация аргументов команды.
     *
     * @throws InvalidInputException исключение, выбрасываемое в случае неуспешной валидации.
     */
    public void validate() throws InvalidInputException {
    }

    public CollectionManager getManager() {
        return manager;
    }

    public Arg[] getArgs() {
        return args;
    }

    public void setArgs(Arg... args) {
        this.args = args;
    }

    public void setManager(CollectionManager manager) {
        this.manager = manager;
    }
}
