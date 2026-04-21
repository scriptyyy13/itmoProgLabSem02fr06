package commands;

import exceptions.InvalidInputException;
import models.Dragon;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code exit}.
 */
public class Exit extends Command {
    /**
     * Создание экземпляра команды {@code exit}.
     *
     * @param manager {@link tools.CollectionManager}, в котором будет исполнена команда.
     */
    public Exit(CollectionManager manager) {
        super(manager);
    }

    public Exit() {
    }

    /**
     * Запуск соответствующего метода в {@link tools.CollectionManager}.
     */
    public void execute() {
        getManager().exit();
    }
}