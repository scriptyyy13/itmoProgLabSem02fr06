package serverCommands;


import commands.CommandRequest;
import serverTools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code remove_by_id}.
 */
public class RemoveById extends Command {

    public RemoveById(CommandRequest cmd, CollectionManager collection) {
        super(cmd, collection);
    }
    /**
     * Исполнение команды в коллекции
     * @return результат выполнения команды
     */
    public String execute() {
        String argValue = getArgs()[0].getValue().toString();
        long id = Long.parseLong(argValue);
        return getCollectionManager().removeById(id, -1);
        // TODO: корректно сделать айдишник того, кто создает
    }
}