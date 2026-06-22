package serverTools;

import database.DatabaseManager;
import exceptions.InvalidInputException;
import models.Dragon;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Коллекция с методами для её управления.
 */
public class CollectionManager {
    /**
     * Коллекция.
     */
    private ConcurrentLinkedDeque<Dragon> collection;
    private final Date creationTime;
    /**
     * Блокировка для потокобезопасности.
     */
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    /**
     * Экземпляр базы данных.
     */
    private final DatabaseManager dbManager = DatabaseManager.getInstance();
    /**
     * Колонки csv таблицы драконов.
     */
    private static final String CSV_HEADER = "id,name,coordinate_x,coordinate_y,creationDate,age,weight,speaking,color,killer_name,killer_birthday,killer_passportID,killer_nationality,location_x,location_y,location_z,location_name,creator_id";

    public CollectionManager(ConcurrentLinkedDeque<Dragon> collection) {
        this.creationTime = new Date();
        this.collection = collection;
    }

    /**
     * Реализация команды {@code add}.
     *
     * @param elem      добавляемый {@code Dragon}.
     * @param creatorId ID пользователя, создающего объект.
     */
    public String add(Dragon elem, long creatorId) {
        long generatedId = dbManager.insertDragon(elem, creatorId);

        if (generatedId != -1) {
            elem.setId(generatedId);
            elem.setCreatorId(creatorId);

            lock.writeLock().lock();
            try {
                collection.addLast(elem);
            } finally {
                lock.writeLock().unlock();
            }
            return "200:success.collection.add";
        } else {
            return "500:error.database.add_failed";
        }
    }

    /**
     * Реализация команды {@code add_if_max}.
     *
     * @param newDragon добавляемый {@code Dragon}.
     * @param creatorId ID пользователя, создающего объект.
     */
    public String addIfMax(Dragon newDragon, long creatorId) {
        lock.writeLock().lock();
        try {
            boolean isMax = collection.stream()
                    .allMatch(e -> newDragon.compareTo(e) > 0);

            if (isMax) {
                long generatedId = dbManager.insertDragon(newDragon, creatorId);
                if (generatedId != -1) {
                    newDragon.setId(generatedId);
                    newDragon.setCreatorId(creatorId);
                    collection.addLast(newDragon);
                    return "200:success.collection.add_if_max";
                } else {
                    return "500:error.database.add_failed";
                }
            }
            return "400:error.collection.not_max";
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Реализация команды {@code add_if_min}.
     *
     * @param newDragon добавляемый {@code Dragon}.
     * @param creatorId ID пользователя, создающего объект.
     */
    public String addIfMin(Dragon newDragon, long creatorId) {
        lock.writeLock().lock();
        try {
            boolean isMin = collection.stream()
                    .allMatch(e -> newDragon.compareTo(e) < 0);

            if (isMin) {
                long generatedId = dbManager.insertDragon(newDragon, creatorId);
                if (generatedId != -1) {
                    newDragon.setId(generatedId);
                    newDragon.setCreatorId(creatorId);
                    collection.addLast(newDragon);
                    return "200:success.collection.add_if_min";
                } else {
                    return "500:error.database.add_failed";
                }
            }
            return "400:error.collection.not_min";
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Реализация команды {@code average_of_age}.
     */
    public String averageOfAge() {
        lock.readLock().lock();
        try {
            OptionalDouble average = collection.stream()
                    .mapToLong(Dragon::getAge)
                    .average();
            if (average.isPresent()) {
                return "200:" + average.getAsDouble();
            } else {
                return "404:error.collection.empty";
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Реализация команды {@code clear}.
     * * @param creatorId ID пользователя, очищающего свои объекты.
     */
    public String clear(long creatorId) {
        boolean isCleared = dbManager.clearDragons(creatorId);

        if (isCleared) {
            lock.writeLock().lock();
            try {
                collection.removeIf(e -> e.getCreatorId() == creatorId);
            } finally {
                lock.writeLock().unlock();
            }
            return "200:success.collection.clear";
        } else {
            return "500:error.database.clear_failed";
        }
    }

    /**
     * Реализация команды {@code filter_less_than_age}.
     *
     * @param age значение, по которому происходит фильтрация.
     */
    public String filterLessThanAge(long age) {
        lock.readLock().lock();
        try {
            String result = collection.stream()
                    .filter(e -> e.getAge() < age)
                    .map(Dragon::toCSV)
                    .collect(Collectors.joining("\n"));

            if (result.isEmpty()) {
                return "404:error.collection.no_elements_younger_than;" + age;
            }

            // Возвращаем хедер + данные
            return "200:" + CSV_HEADER + "\n" + result;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Реализация команды {@code info}.
     */
    public String info() {
        return "200:success.collection.info_format;" + creationTime + ";" + collection.size();
    }

    /**
     * Реализация команды {@code print_unique_weight}.
     */
    public String printUniqueWeight() {
        lock.readLock().lock();
        try {
            return "200:" + collection.stream()
                    .map(Dragon::getWeight)
                    .filter(Objects::nonNull)
                    .distinct()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Реализация команды {@code remove_by_id}.
     *
     * @param id        id удаляемого объекта.
     * @param creatorId ID пользователя, инициировавшего удаление.
     */
    public String removeById(long id, long creatorId) {
        lock.writeLock().lock();
        try {
            Optional<Dragon> found = collection.stream()
                    .filter(e -> e.getId() == id)
                    .findFirst();

            if (found.isEmpty()) {
                return "404:error.collection.id_not_found";
            }

            if (found.get().getCreatorId() != creatorId) {
                return "403:error.collection.permission_denied";
            }
            boolean isDeleted = dbManager.deleteDragon(id, creatorId);

            if (isDeleted) {
                collection.removeIf(e -> e.getId() == id);
                return "200:success.collection.remove";
            } else {
                return "500:error.database.delete_failed";
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Реализация команды {@code remove_head}.
     * * @param creatorId ID пользователя, инициировавшего операцию.
     */
    public String removeHead(long creatorId) {
        lock.writeLock().lock();
        try {
            Optional<Dragon> firstUserDragon = collection.stream()
                    .filter(e -> e.getCreatorId() == creatorId)
                    .findFirst();

            if (firstUserDragon.isEmpty()) {
                return "404:error.collection.no_user_elements";
            }

            Dragon target = firstUserDragon.get();
            boolean isDeleted = dbManager.deleteDragon(target.getId(), creatorId);

            if (isDeleted) {
                collection.remove(target);
                return "200:success.collection.remove_head_format;" + target;
            } else {
                return "500:error.database.delete_failed";
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Реализация команды {@code show}.
     */
    public String show() {
        lock.readLock().lock();
        try {
            if (collection.isEmpty()) return "404:error.collection.empty";

            String result = collection.stream()
                    .sorted(Comparator.comparing(Dragon::getWeight, Comparator.nullsLast(Comparator.naturalOrder())))
                    .map(Dragon::toCSV)
                    .collect(Collectors.joining("\n"));

            // Возвращаем хедер + данные
            return "200:" + CSV_HEADER + "\n" + result;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Реализация команды {@code update}.
     *
     * @param id        id обновляемого объекта.
     * @param updDragon новое значение объекта.
     * @param creatorId ID пользователя, инициировавшего обновление.
     */
    public String update(long id, Dragon updDragon, long creatorId) {
        lock.writeLock().lock();
        try {
            Optional<Dragon> found = collection.stream()
                    .filter(e -> e.getId() == id)
                    .findFirst();

            if (found.isEmpty()) {
                return "404:error.collection.id_not_found_format;" + id;
            }

            if (found.get().getCreatorId() != creatorId) {
                return "403:error.collection.permission_denied";
            }

            // Атомарно обновляем в БД
            boolean isUpdated = dbManager.updateDragon(id, updDragon, creatorId);

            if (isUpdated) {
                Dragon old = found.get();
                updDragon.setId(id);
                updDragon.setCreatorId(creatorId);
                updDragon.setCreationDate(old.getCreationDate());

                collection.remove(old);
                collection.add(updDragon);
                return "200:success.collection.update_format;" + id;
            } else {
                return "500:error.database.update_failed";
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Валидация элементов коллекции.
     * Элементы, не прошедшие валидацию, удаляются из коллекции.
     */
    public void validate() {
        lock.writeLock().lock();
        try {
            Set<Long> ids = new HashSet<>();
            ConcurrentLinkedDeque<Dragon> validCollection = new ConcurrentLinkedDeque<>();

            for (Dragon e : collection) {
                if (ids.contains(e.getId())) {
                    System.out.println("Обнаружен повтор id, элемент пропущен: " + e.getId());
                    continue;
                }
                try {
                    e.validate();
                    ids.add(e.getId());
                    validCollection.add(e);
                } catch (InvalidInputException ex) {
                    System.out.println("Ошибка в объекте ID " + e.getId() + ": " + ex.getMessage());
                }
            }
            this.collection = validCollection;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public ConcurrentLinkedDeque<Dragon> getCollection() {
        return collection;
    }

    public void setCollection(ConcurrentLinkedDeque<Dragon> collection) {
        lock.writeLock().lock();
        try {
            this.collection = collection;
        } finally {
            lock.writeLock().unlock();
        }
    }
}