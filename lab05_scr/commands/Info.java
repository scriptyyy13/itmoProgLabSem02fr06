package commands;

import exceptions.InvalidInputException;
import models.Dragon;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code info}.
 */
public class Info extends Command {
    /**
     * Создание экземпляра команды {@code info}.
     * @param manager {@link tools.CollectionManager}, в котором будет исполнена команда.
     */
    public Info(CollectionManager manager) {
        super(manager);
    }

    public Info() {}

    /**
     * Запуск соответствующего метода в {@link tools.CollectionManager}.
     */
    public void execute() {
        getManager().info();
    }
}