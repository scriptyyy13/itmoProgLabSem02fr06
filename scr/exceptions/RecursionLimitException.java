package exceptions;

/**
 * Выбрасывается при превышении допустимого предела рекурсии.
 */
public class RecursionLimitException extends RuntimeException {
    public RecursionLimitException(String message) {
        super(message);
    }
}
