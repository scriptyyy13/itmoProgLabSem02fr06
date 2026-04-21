package exceptions;

/**
 * Выбрасывается при ошибке сохранения XML-файла.
 */
public class XmlSaveException extends RuntimeException {
    public XmlSaveException(String message) {
        super(message);
    }
}
