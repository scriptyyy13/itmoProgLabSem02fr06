package commands;

import exceptions.InvalidInputException;
import models.Dragon;
import tools.CollectionManager;
import tools.OutputManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code add}.
 */

public class Add extends Command {
    /**
     * Создание экземпляра команды {@code add}.
     *
     * @param manager {@link tools.CollectionManager}, в котором будет исполнена команда.
     */
    public Add(CollectionManager manager) {
        super(manager);
    }

    public Add() {
    }

    /**
     * Запуск соответствующего метода в {@link tools.CollectionManager}.
     */
    public void execute() {
        try {
            validate();
            getManager().add((Dragon) getArgs()[0].getValue());
        } catch (InvalidInputException e) {
            OutputManager.println(e.getMessage());
        }
    }

    /**
     * Валидация аргументов команды.
     *
     * @throws InvalidInputException исключение, выбрасываемое в случае неуспешной валидации.
     */
    public void validate() throws InvalidInputException {
        if (!(getArgs()[0].getValue() instanceof Dragon)) throw new InvalidInputException("Неверный формат");
    }
}
