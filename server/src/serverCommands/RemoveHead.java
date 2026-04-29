package serverCommands;


import commands.CommandRequest;
import commands.RemoveHeadRequest;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code remove_head}.
 */
public class RemoveHead extends Command {

    public RemoveHead(CommandRequest cmd, CollectionManager collection) {
        super(cmd,collection);
    }

    public String execute(){
        return getCollectionManager().removeHead();
    }

}