package serverCommands;

import commands.CommandRequest;
import models.Dragon;
import serverTools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code update}.
 */
public class Update extends Command {

    public Update(CommandRequest cmd, CollectionManager collection) {
        super(cmd, collection);
    }
    /**
     * Исполнение команды в коллекции
     * @return результат выполнения команды
     */
    public String execute() {
        String idString = getArgs()[0].getValue().toString();
        long id = Long.parseLong(idString);
        Dragon updatedDragon = (Dragon) getArgs()[1].getValue();
        return getCollectionManager().update(id, updatedDragon, this.getExecutorId());
    }
}