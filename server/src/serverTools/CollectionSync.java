package serverTools;

import models.Dragon;
import java.io.File;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Класс для синхронизации состояния коллекции с файлом.
 */
public class CollectionSync {
    private final String path;

    public CollectionSync(String path) {
        this.path = path;
    }

    /**
     * Проверяет, нужно ли обновить коллекцию из файла.
     */
    public ConcurrentLinkedDeque<Dragon> syncBeforeRead(ConcurrentLinkedDeque<Dragon> currentCollection) {
        File file = new File(path);
        ConcurrentLinkedDeque<Dragon> loaded = XMLReader.readXmlCollection(path);
        return loaded;
    }

    /**
     * Сохраняет коллекцию и обновляет метку времени.
     */
    public void syncAfterWrite(ConcurrentLinkedDeque<Dragon> collection) {
        XMLWriter.dequeToXML(collection, path);
    }
}