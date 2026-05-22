package serverCommands;


import commands.CommandRequest;
import serverTools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code remove_head}.
 */
public class RemoveHead extends Command {

    public RemoveHead(CommandRequest cmd, CollectionManager collection) {
        super(cmd, collection);
    }
    /**
     * Исполнение команды в коллекции
     * @return результат выполнения команды
     */
    public String execute() {
        return getCollectionManager().removeHead();
    }

}