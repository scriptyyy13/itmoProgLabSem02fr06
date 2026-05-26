package serverCommands;


import commands.CommandRequest;
import serverTools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code average_of_age}.
 */
public class AverageOfAge extends Command {

    public AverageOfAge(CommandRequest cmd, CollectionManager collection) {
        super(cmd, collection);
    }

    /**
     * Исполнение команды в коллекции
     * @return результат выполнения команды
     */
    public String execute() {
        return getCollectionManager().averageOfAge();
    }
}