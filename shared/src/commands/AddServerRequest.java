package commands;

import exceptions.InvalidInputException;

/**
 * Класс, отвечающий за команду балансера {@code add_server}.
 */
public class AddServerRequest extends AdminCommandRequest {
    /**
     * Создание экземпляра команды {@code add_server}.
     */
    public AddServerRequest() {
    }

    /**
     * Валидация аргументов команды.
     *
     * @throws InvalidInputException исключение, выбрасываемое в случае неуспешной валидации.
     */
    public void validate() throws InvalidInputException {
        if (!(getArgs()[0].getValue() instanceof String) && (getArgs().length != 1))
            throw new InvalidInputException("error.command.validate");
    }
}
