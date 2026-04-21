package commands;

import exceptions.InvalidInputException;
import tools.CollectionManager;
/**
 * Класс, отвечающий за сохрание экземпляра команды {@code show}.
 */
public class Show extends Command {
    /**
     * Создание экземпляра команды {@code show}.
     * @param manager {@link tools.CollectionManager}, в котором будет исполнена команда.
     */
    public Show(CollectionManager manager) {
        super(manager);
    }

    public Show() {}

    /**
     * Запуск соответствующего метода в {@link tools.CollectionManager}.
     */
    public void execute() {
        getManager().show();
    }
}