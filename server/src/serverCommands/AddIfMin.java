package serverCommands;

import commands.CommandRequest;
import models.Dragon;
import serverTools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code add_if_min}.
 */
public class AddIfMin extends Command {

    public AddIfMin(CommandRequest cmd, CollectionManager collection) {
        super(cmd, collection);
    }

    /**
     * Исполнение команды в коллекции
     * @return результат выполнения команды
     */
    public String execute() {
        return getCollectionManager().addIfMin((Dragon) getArgs()[0].getValue(), this.getExecutorId());
    }
}