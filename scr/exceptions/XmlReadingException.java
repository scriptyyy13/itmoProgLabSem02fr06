package exceptions;

/**
 * Выбрасывается при ошибке чтения XML-файла.
 */
public class XmlReadingException extends RuntimeException {
    public XmlReadingException(String message) {
        super(message);
    }
}
