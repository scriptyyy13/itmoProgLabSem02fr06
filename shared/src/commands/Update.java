package commands;

import exceptions.InvalidInputException;
import models.Dragon;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code update}.
 */
public class Update extends Command {
    /**
     * Создание экземпляра команды {@code update}.
     *
     */

    public Update() {
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
        if (getArgs().length < 2 || !(getArgs()[1].getValue() instanceof Dragon))
            throw new InvalidInputException("Неверный формат");
    }
}