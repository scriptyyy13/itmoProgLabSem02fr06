package serverCommands;

import commands.CommandRequest;
import models.Dragon;
import serverTools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code add_if_max}.
 */
public class AddIfMax extends Command {

    public AddIfMax(CommandRequest cmd, CollectionManager collection) {
        super(cmd, collection);
    }

    /**
     * Исполнение команды в коллекции
     * @return результат выполнения команды
     */
    public String execute() {
        return getCollectionManager().addIfMax((Dragon) getArgs()[0].getValue(), this.getExecutorId());
    }
}