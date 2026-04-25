package commands;

import exceptions.InvalidInputException;
import models.Dragon;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code add_if_max}.
 */
public class AddIfMax extends Command {
    /**
     * Создание экземпляра команды {@code add_if_max}.
     *
     */

    public AddIfMax() {
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