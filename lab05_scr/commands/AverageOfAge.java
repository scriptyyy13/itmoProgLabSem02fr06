package commands;

import exceptions.EmptyDequeException;
import exceptions.InvalidInputException;
import models.Dragon;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code average_of_age}.
 */
public class AverageOfAge extends Command {
    /**
     * Создание экземпляра команды {@code average_of_age }.
     * @param manager {@link tools.CollectionManager}, в котором будет исполнена команда.
     */
    public AverageOfAge(CollectionManager manager) {
        super(manager);
    }

    public AverageOfAge() {}

    /**
     * Запуск соответствующего метода в {@link tools.CollectionManager}.
     */
    public void execute() {
        getManager().averageOfAge();
    }
}