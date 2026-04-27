import commands.*;

public enum ClientCommandType {
    HELP("help"),
    INFO("info"),
    SHOW("show"),
    ADD("add"),
    UPDATE("update"),
    REMOVE_BY_ID("remove_by_id"),
    CLEAR("clear"),
    EXECUTE_SCRIPT("execute_script"),
    EXIT("exit"),
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
    public Command create() {
        return switch (this) {
            case HELP -> new Help();
            case INFO -> new Info();
            case SHOW -> new Show();
            case ADD -> new Add();
            case UPDATE -> new Update();
            case REMOVE_BY_ID -> new RemoveById();
            case CLEAR -> new Clear();
            case EXECUTE_SCRIPT -> new ExecuteScript();
            case EXIT -> new Exit();
            case REMOVE_HEAD -> new RemoveHead();
            case ADD_IF_MAX -> new AddIfMax();
            case ADD_IF_MIN -> new AddIfMin();
            case AVERAGE_OF_AGE -> new AverageOfAge();
            case FILTER_LESS_THAN_AGE -> new FilterLessThanAge();
            case PRINT_UNIQUE_WEIGHT -> new PrintUniqueWeight();
        };
    }
}