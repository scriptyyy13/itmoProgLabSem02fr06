package commands;

import exceptions.EmptyDequeException;
import exceptions.InvalidInputException;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code remove_head}.
 */
public class RemoveHead extends Command {
    /**
     * Создание экземпляра команды {@code remove_head}.
     *
     * @param manager {@link tools.CollectionManager}, в котором будет исполнена команда.
     */
    public RemoveHead(CollectionManager manager) {
        super(manager);
    }

    public RemoveHead() {
    }

    /**
     * Запуск соответствующего метода в {@link tools.CollectionManager}.
     */
    public void execute() {
        getManager().removeHead();
    }
}