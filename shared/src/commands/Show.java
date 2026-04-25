package commands;

import CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code show}.
 */
public class Show extends Command {
    /**
     * Создание экземпляра команды {@code show}.
     *
     * @param manager {@link CollectionManager}, в котором будет исполнена команда.
     */
    public Show(CollectionManager manager) {
        super(manager);
    }

    public Show() {
    }

    /**
     * Запуск соответствующего метода в {@link CollectionManager}.
     */
    public void execute() {
        getManager().show();
    }
}