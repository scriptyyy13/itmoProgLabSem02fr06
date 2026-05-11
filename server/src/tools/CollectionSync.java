package tools;

import models.Dragon;
import java.io.File;
import java.util.ArrayDeque;

/**
 * Класс для синхронизации состояния коллекции с файлом.
 */
public class CollectionSync {
    private final String path;
    private long lastKnownModification = 0;

    public CollectionSync(String path) {
        this.path = path;
        updateLastModified();
    }

    /**
     * Проверяет, нужно ли обновить коллекцию из файла.
     */
    public ArrayDeque<Dragon> syncBeforeRead(ArrayDeque<Dragon> currentCollection) {
        File file = new File(path);
        if (file.exists() && file.lastModified() > lastKnownModification) {
            //System.out.println("Файл был изменен другим сервером. Синхронизация...");
            ArrayDeque<Dragon> loaded = XMLReader.readXmlCollection(path);
            updateLastModified();
            return loaded;
        }
        return currentCollection;
    }

    /**
     * Сохраняет коллекцию и обновляет метку времени.
     */
    public void syncAfterWrite(ArrayDeque<Dragon> collection) {
        XMLWriter.dequeToXML(collection, path);
        updateLastModified();
    }

    private void updateLastModified() {
        File file = new File(path);
        if (file.exists()) {
            this.lastKnownModification = file.lastModified();
        }
    }
}