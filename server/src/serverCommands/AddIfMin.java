package serverCommands;

import commands.AddIfMinRequest;
import commands.CommandRequest;
import models.Dragon;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code add_if_min}.
 */
public class AddIfMin extends Command {

    public AddIfMin(CommandRequest cmd, CollectionManager collection) {
        super(cmd,collection);
    }

    public String execute(){
        return getCollectionManager().addIfMin((Dragon)getArgs()[0].getValue());
    }
}