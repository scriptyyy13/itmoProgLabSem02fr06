package exceptions;

/**
 * Выбрасывается при неверном вводе полей обьектов коллекции или аргументов команд.
 */
public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
}
