package serverCommands;

import commands.AddIfMaxRequest;
import commands.CommandRequest;
import models.Dragon;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code add_if_max}.
 */
public class AddIfMax extends Command {

    public AddIfMax(CommandRequest cmd, CollectionManager collection) {
        super(cmd,collection);
    }

    public String execute(){
        return getCollectionManager().addIfMax((Dragon)getArgs()[0].getValue());
    }
}