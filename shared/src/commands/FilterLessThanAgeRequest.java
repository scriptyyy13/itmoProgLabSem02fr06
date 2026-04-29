package commands;

import exceptions.InvalidInputException;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code filter_less_than_age}.
 */
public class FilterLessThanAgeRequest extends Command {
    /**
     * Создание экземпляра команды {@code filter_less_than_age}.
     *
     */

    public FilterLessThanAgeRequest() {
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