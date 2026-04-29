package serverCommands;

import commands.CommandRequest;
import models.Dragon;
import tools.CollectionManager;


/**
 * Класс, отвечающий за сохрание экземпляра команды {@code add}.
 */

public class Add extends Command {

    public Add(CommandRequest cmd, CollectionManager collection) {
        super(cmd,collection);
    }

    public String execute(){
        return getCollectionManager().add((Dragon)getArgs()[0].getValue());
    }

}
