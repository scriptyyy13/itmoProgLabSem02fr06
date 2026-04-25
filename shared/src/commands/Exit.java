package commands;

import CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code exit}.
 */
public class Exit extends Command {
    /**
     * Создание экземпляра команды {@code exit}.
     *
     * @param manager {@link CollectionManager}, в котором будет исполнена команда.
     */
    public Exit(CollectionManager manager) {
        super(manager);
    }

    public Exit() {
    }

    /**
     * Запуск соответствующего метода в {@link CollectionManager}.
     */
    public void execute() {
        getManager().exit();
    }
}