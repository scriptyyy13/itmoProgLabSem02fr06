package serverCommands;

import commands.CommandRequest;
import commands.UpdateRequest;
import models.Dragon;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code update}.
 */
public class Update extends Command {

    public Update(CommandRequest cmd, CollectionManager collection) {
        super(cmd,collection);
    }

    public String execute(){
        return getCollectionManager().update((long)getArgs()[0].getValue(), (Dragon) getArgs()[0].getValue());
    }
}