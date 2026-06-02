package commands;

import exceptions.InvalidInputException;

/**
 * Класс, отвечающий за команду балансера {@code remove_server}.
 */
public class RemoveServerRequest extends AdminCommandRequest {
    /**
     * Создание экземпляра команды {@code remove_server}.
     */
    public RemoveServerRequest() {
    }

    /**
     * Валидация аргументов команды.
     *
     * @throws InvalidInputException исключение, выбрасываемое в случае неуспешной валидации.
     */
    public void validate() throws InvalidInputException {
        if (!(getArgs()[0].getValue() instanceof String) && (getArgs().length != 1))
            throw new InvalidInputException("Неверный формат");
    }
}
