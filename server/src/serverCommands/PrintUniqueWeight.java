package serverCommands;


import commands.CommandRequest;
import serverTools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code print_unique_weight}.
 */
public class PrintUniqueWeight extends Command {

    public PrintUniqueWeight(CommandRequest cmd, CollectionManager collection) {
        super(cmd, collection);
    }
    /**
     * Исполнение команды в коллекции
     * @return результат выполнения команды
     */
    public String execute() {
        return getCollectionManager().printUniqueWeight();
    }

}