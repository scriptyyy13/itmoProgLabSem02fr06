package serverCommands;

import commands.CommandRequest;
import sharedTools.Arg;
import serverTools.CollectionManager;


/**
 *  Абстрактный класс предок всех команд.
 */
public abstract class Command {
    /**
     * Коллекция, в которой исполняется команда.
     */
    private CollectionManager collectionManager;
    /**
     * Масссив аргументы команды.
     */
    private Arg[] args;

    public Command(CommandRequest commandRequest, CollectionManager collectionManager) {
        args = commandRequest.getArgs();
        this.collectionManager = collectionManager;
    }

    /**
     * Исполнение команды в коллекции
     * @return результат выполнения команды
     */
    public String execute() {
        return null;
    }

    public Arg[] getArgs() {
        return args;
    }

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }
}
