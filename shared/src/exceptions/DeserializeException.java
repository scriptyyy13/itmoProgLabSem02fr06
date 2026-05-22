package exceptions;

/**
 *  Исключение, выбрасываемое при ошибке десериализации.
 */
public class DeserializeException extends RuntimeException {
    public DeserializeException(String message) {
        super(message);
    }
}
