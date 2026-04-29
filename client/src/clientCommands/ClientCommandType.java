package clientCommands;

import commands.*;

public enum ClientCommandType {
    HELP("help"),
    INFO("info"),
    SHOW("show"),
    ADD("add"),
    UPDATE("update"),
    REMOVE_BY_ID("remove_by_id"),
    CLEAR("clear"),
    REMOVE_HEAD("remove_head"),
    ADD_IF_MAX("add_if_max"),
    ADD_IF_MIN("add_if_min"),
    AVERAGE_OF_AGE("average_of_age"),
    FILTER_LESS_THAN_AGE("filter_less_than_age"),
    PRINT_UNIQUE_WEIGHT("print_unique_speaking"); // Название из твоего HashMap

    private final String name;

    ClientCommandType(String name) {
        this.name = name;
    }

    public static ClientCommandType fromString(String text) {
        for (ClientCommandType type : values()) {
            if (type.name.equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }

    /** Метод для генерации чистого экземпляра команды для сериализации */
    public CommandRequest create() {
        return switch (this) {
            case HELP -> new HelpRequest();
            case INFO -> new InfoRequest();
            case SHOW -> new ShowRequest();
            case ADD -> new AddRequest();
            case UPDATE -> new UpdateRequest();
            case REMOVE_BY_ID -> new RemoveByIdRequest();
            case CLEAR -> new ClearRequest();
            case REMOVE_HEAD -> new RemoveHeadRequest();
            case ADD_IF_MAX -> new AddIfMaxRequest();
            case ADD_IF_MIN -> new AddIfMinRequest();
            case AVERAGE_OF_AGE -> new AverageOfAgeRequest();
            case FILTER_LESS_THAN_AGE -> new FilterLessThanAgeRequest();
            case PRINT_UNIQUE_WEIGHT -> new PrintUniqueWeightRequest();
        };
    }
}