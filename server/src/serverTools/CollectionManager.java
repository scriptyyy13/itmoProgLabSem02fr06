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
            return "Элемент успешно добавлен.";
        } else {
            return "Ошибка: Не удалось добавить элемент в базу данных.";
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
                    return "Элемент добавлен (был максимальным).";
                } else {
                    return "Ошибка: Не удалось добавить элемент в базу данных.";
                }
            }
            return "Элемент не максимальный.";
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
                    return "Элемент добавлен (был минимальным).";
                } else {
                    return "Ошибка: Не удалось добавить элемент в базу данных.";
                }
            }
            return "Элемент не минимальный.";
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
                return String.valueOf(average.getAsDouble());
            } else {
                return "Коллекция пуста";
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
            return "Ваши элементы успешно удалены из коллекции.";
        } else {
            return "Ошибка при очистке коллекции в базе данных.";
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
                    .map(Dragon::toString)
                    .collect(Collectors.joining("\n"));

            return result.isEmpty()
                    ? "Нет элементов моложе " + age
                    : result;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Реализация команды {@code info}.
     */
    public String info() {
        return String.format("""
                Информация о коллекции:
                
                Тип: ConcurrentLinkedDeque
                Дата инициализации: %s
                Количество элементов: %d
                """, creationTime, collection.size());
    }

    /**
     * Реализация команды {@code print_unique_weight}.
     */
    public String printUniqueWeight() {
        lock.readLock().lock();
        try {
            return collection.stream()
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
                return "Элемент с таким ID не найден.";
            }

            if (found.get().getCreatorId() != creatorId) {
                return "Ошибка: У вас нет прав на удаление этого объекта!";
            }
            boolean isDeleted = dbManager.deleteDragon(id, creatorId);

            if (isDeleted) {
                collection.removeIf(e -> e.getId() == id);
                return "Элемент удалён.";
            } else {
                return "Ошибка: Не удалось удалить элемент из базы данных.";
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
                return "В коллекции нет принадлежащих вам элементов.";
            }

            Dragon target = firstUserDragon.get();
            boolean isDeleted = dbManager.deleteDragon(target.getId(), creatorId);

            if (isDeleted) {
                collection.remove(target);
                return "Удален ваш первый элемент в коллекции: " + target;
            } else {
                return "Ошибка базы данных при удалении элемента.";
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
            if (collection.isEmpty()) return "Коллекция пуста";

            String result = collection.stream()
                    .sorted(Comparator.comparing(Dragon::getWeight, Comparator.nullsLast(Comparator.naturalOrder())))
                    .map(Dragon::toString)
                    .collect(Collectors.joining("\n---------------\n"));
            return "Элементы коллекции:\n" + result;
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
                return "Элемент с ID " + id + " не найден.";
            }

            if (found.get().getCreatorId() != creatorId) {
                return "Ошибка: У вас нет прав на модификацию этого объекта!";
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
                return "Элемент с ID " + id + " успешно обновлен.";
            } else {
                return "Ошибка: Не удалось обновить элемент в базе данных.";
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
