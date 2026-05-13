package tools;

import models.Dragon;
import java.io.File;
import java.util.ArrayDeque;

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
    public ArrayDeque<Dragon> syncBeforeRead(ArrayDeque<Dragon> currentCollection) {
        File file = new File(path);
        ArrayDeque<Dragon> loaded = XMLReader.readXmlCollection(path);
        return loaded;
    }

    /**
     * Сохраняет коллекцию и обновляет метку времени.
     */
    public void syncAfterWrite(ArrayDeque<Dragon> collection) {
        XMLWriter.dequeToXML(collection, path);
    }
}