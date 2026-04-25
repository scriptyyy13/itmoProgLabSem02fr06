package commands;

import CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code help}.
 */
public class Help extends Command {
    /**
     * Создание экземпляра команды {@code help}.
     *
     * @param manager {@link CollectionManager}, в котором будет исполнена команда.
     */
    public Help(CollectionManager manager) {
        super(manager);
    }

    public Help() {
    }

    /**
     * Запуск соответствующего метода в {@link CollectionManager}.
     */
    public void execute() {
        getManager().help();
    }
}
