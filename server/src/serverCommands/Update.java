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
        super(cmd, collection);
    }

    public String execute() {
        String idString = getArgs()[0].getValue().toString();
        long id = Long.parseLong(idString);
        Dragon updatedDragon = (Dragon) getArgs()[1].getValue();
        return getCollectionManager().update(id, updatedDragon);
    }
}