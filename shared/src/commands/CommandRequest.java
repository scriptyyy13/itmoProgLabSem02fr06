package commands;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import exceptions.InvalidInputException;
import tools.Arg;

import java.io.Serializable;

/**
 * Абстрактный класс-предок всех команд.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type"
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = AddRequest.class, name = "Add"),
        @JsonSubTypes.Type(value = AddIfMaxRequest.class, name = "AddIfMax"),
        @JsonSubTypes.Type(value = AddIfMinRequest.class, name = "AddIfMin"),
        @JsonSubTypes.Type(value = ClearRequest.class, name = "Clear"),
        @JsonSubTypes.Type(value = RemoveByIdRequest.class, name = "RemoveById"),
        @JsonSubTypes.Type(value = RemoveHeadRequest.class, name = "RemoveHead"),
        @JsonSubTypes.Type(value = UpdateRequest.class, name = "Update"),
})

public abstract class CommandRequest implements Serializable {
    /**
     * Массив аргументов команды.
     */
    private Arg[] args;

    /**
     * Создание экземпляра команды
     *
     */
    public CommandRequest() {
    }


    /**
     * Валидация аргументов команды.
     *
     * @throws InvalidInputException исключение, выбрасываемое в случае неуспешной валидации.
     */
    public void validate() throws InvalidInputException {
    }

    public Arg[] getArgs() {
        return args;
    }

    public void setArgs(Arg... args) {
        this.args = args;
    }

}
