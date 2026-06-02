package serverCommands;

import commands.CommandRequest;
import exceptions.TokenException;
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
    /**
     * ID пользователя, отправившего команду.
     */
    private Long executorId = -1L;
    /**
     * Требование авторизации для выполнения команды.
     */
    public Boolean requiresAuth = true;

    public Command(CommandRequest commandRequest, CollectionManager collectionManager) {
        args = commandRequest.getArgs();
        this.collectionManager = collectionManager;
    }

    /**
     * Исполнение команды в коллекции
     * @return результат выполнения команды
     */
    public String execute() throws TokenException {
        return null;
    }

    public Arg[] getArgs() {
        return args;
    }

    public Long getExecutorId() {
        return executorId;
    }

    public Long setExecutorId(Long id) {
        return this.executorId = id;
    }

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }
}
