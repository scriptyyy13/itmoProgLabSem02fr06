package serverCommands;


import commands.CommandRequest;
import commands.RemoveByIdRequest;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code remove_by_id}.
 */
public class RemoveById extends Command {

    public RemoveById(CommandRequest cmd, CollectionManager collection) {
        super(cmd, collection);
    }

    public String execute() {
        String argValue = getArgs()[0].getValue().toString();
        long id = Long.parseLong(argValue);
        return getCollectionManager().removeById(id);
    }
}