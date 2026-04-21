package commands;

import exceptions.InvalidInputException;
import models.Dragon;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code filter_less_than_age}.
 */
public class FilterLessThanAge extends Command {
    /**
     * Создание экземпляра команды {@code filter_less_than_age}.
     *
     * @param manager {@link tools.CollectionManager}, в котором будет исполнена команда.
     */
    public FilterLessThanAge(CollectionManager manager) {
        super(manager);
    }

    public FilterLessThanAge() {
    }

    /**
     * Запуск соответствующего метода в {@link tools.CollectionManager}.
     */
    public void execute() {
        try {
            validate();
            getManager().filterLessThanAge(Long.parseLong((String) getArgs()[0].getValue()));
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Валидация аргументов команды.
     *
     * @throws InvalidInputException исключение, выбрасываемое в случае неуспешной валидации.
     */
    public void validate() throws InvalidInputException {
        try {
            Long.parseLong((String) getArgs()[0].getValue());
        } catch (Exception e) {
            throw new InvalidInputException("Неверный формат");
        }
    }
}