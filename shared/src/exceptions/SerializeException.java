package exceptions;

/**
 * Исключение, выбрасываемое при ошибке сериализации.
 */
public class SerializeException extends RuntimeException {
    public SerializeException(String message) {
        super(message);
    }
}
