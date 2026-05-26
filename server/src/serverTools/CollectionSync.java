package serverTools;

import database.DatabaseManager;
import models.Dragon;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Класс для синхронизации состояния коллекции с базой данных.
 * Защищает от рассинхронизации при работе нескольких серверов через балансировщик.
 */
public class CollectionSync {
    private final DatabaseManager dbManager = DatabaseManager.getInstance();

    public CollectionSync() {
    }

    /**
     * Проверяет и обновляет коллекцию в памяти актуальными данными из БД перед чтением или записью.
     * Вызывается перед обработкой команды сервером.
     *
     * @param collectionManager менеджер коллекции, чьё состояние нужно актуализировать.
     */
    public void syncBeforeRead(CollectionManager collectionManager) {
        try {
            // Запрашиваем из БД самую свежую версию данных
            ConcurrentLinkedDeque<Dragon> updated = dbManager.fetchAllDragons();

            // Просто обновляем коллекцию целиком через стандартный сеттер менеджера
            collectionManager.setCollection(updated);
            collectionManager.validate();
        } catch (Exception e) {
            System.err.println("Ошибка синхронизации серверов через БД: " + e.getMessage());
        }
    }
}