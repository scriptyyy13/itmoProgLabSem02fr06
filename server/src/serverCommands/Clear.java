package serverCommands;


import commands.ClearRequest;
import commands.CommandRequest;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code clear}.
 */
public class Clear extends Command {

    public Clear(CommandRequest cmd, CollectionManager collection) {
        super(cmd,collection);
    }

    public String execute(){
        return getCollectionManager().clear();
    }
}