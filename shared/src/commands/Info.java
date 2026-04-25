package commands;

import CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code info}.
 */
public class Info extends Command {
    /**
     * Создание экземпляра команды {@code info}.
     *
     * @param manager {@link CollectionManager}, в котором будет исполнена команда.
     */
    public Info(CollectionManager manager) {
        super(manager);
    }

    public Info() {
    }

    /**
     * Запуск соответствующего метода в {@link CollectionManager}.
     */
    public void execute() {
        getManager().info();
    }
}