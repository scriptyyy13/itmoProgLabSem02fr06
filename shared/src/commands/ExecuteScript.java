package commands;

import exceptions.InvalidInputException;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code execute_script}.
 */
public class ExecuteScript extends Command {

    /**
     * Создание экземпляра команды {@code execute_script}.
     *
     */

    public ExecuteScript() {
    }


    /**
     * Валидация аргументов команды.
     *
     * @throws InvalidInputException исключение, выбрасываемое в случае неуспешной валидации.
     */
    public void validate() throws InvalidInputException {
        if (!(getArgs()[0].getValue() instanceof String)) throw new InvalidInputException("Неверный формат");
    }
}
