package commands;

import tools.CollectionManager;


/**
 * Класс, отвечающий за сохрание экземпляра команды {@code clear}.
 */
public class Clear extends Command {
    /**
     * Создание экземпляра команды {@code clear}.
     *
     * @param manager {@link tools.CollectionManager}, в котором будет исполнена команда.
     */
    public Clear(CollectionManager manager) {
        super(manager);
    }

    public Clear() {
    }

    /**
     * Запуск соответствующего метода в {@link tools.CollectionManager}.
     */
    public void execute() {
        getManager().clear();
    }
}