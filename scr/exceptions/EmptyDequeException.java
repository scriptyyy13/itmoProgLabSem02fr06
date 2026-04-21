package exceptions;

/**
 * Выбрасывается при попытке провести операцию над пустой коллекцией, если это невозможно.
 */
public class EmptyDequeException extends RuntimeException {
    public EmptyDequeException(String message) {
        super(message);
    }
}
