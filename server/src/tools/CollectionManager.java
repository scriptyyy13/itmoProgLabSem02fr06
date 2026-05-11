package tools;

import exceptions.InvalidInputException;
import exceptions.XmlSaveException;
import models.Dragon;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Коллекция с методами для её управления.
 */
public class CollectionManager {
    /**
     * Коллекция.
     */
    private ArrayDeque<Dragon> collection;
    private final Date creationTime;
    private final CollectionSync synchronizer;

    public CollectionManager(ArrayDeque<Dragon> collection, String path) {
        this.creationTime = new Date();
        this.collection = collection;
        this.synchronizer = new CollectionSync(path);
    }

    private void checkSync() {
        ArrayDeque<Dragon> updated = synchronizer.syncBeforeRead(this.collection);
        if (updated != this.collection) {
            this.collection = updated;
            validate();
        }
    }

    private void saveSync() {
        synchronizer.syncAfterWrite(this.collection);
    }

    /**
     * Реализация команды {@code add}.
     *
     * @param elem добавляемый {@code Dragon}.
     */
    public String add(Dragon elem) {
        checkSync();
        elem.setId(getMaxId() + 1);
        collection.addLast(elem);
        saveSync();
        return "Элемент добавлен";
    }

    /**
     * Реализация команды {@code add_if_max}.
     *
     * @param newDragon добавляемый {@code Dragon}.
     */
    public String addIfMax(Dragon newDragon) {
        checkSync();
        boolean isMax = collection.stream()
                .allMatch(e -> newDragon.compareTo(e) > 0);

        if (isMax) {
            newDragon.setId(getMaxId() + 1);
            collection.addLast(newDragon);
            saveSync();
            return "Элемент добавлен (был максимальным)";
        }
        return "Элемент не максимальный";
    }

    /**
     * Реализация команды {@code add_if_min}.
     *
     * @param newDragon добавляемый {@code Dragon}.
     */
    public String addIfMin(Dragon newDragon) {
        checkSync();
        boolean isMin = collection.stream()
                .allMatch(e -> newDragon.compareTo(e) < 0);

        if (isMin) {
            newDragon.setId(getMaxId() + 1);
            collection.addLast(newDragon);
            saveSync();
            return "Элемент добавлен (был минимальным)";
        }
        return "Элемент не минимальный";
    }

    /**
     * Реализация команды {@code average_of_age}.
     */
    public String averageOfAge() {
        checkSync();
        OptionalDouble average = collection.stream()
                .mapToLong(Dragon::getAge)
                .average();
        if (average.isPresent()) {
            return String.valueOf(average.getAsDouble());
        } else {
            return "Коллекция пуста";
        }
    }

    /**
     * Реализация команды {@code clear}.
     */
    public String clear() {
        collection.clear();
        saveSync();
        return "Коллекция очищена";
    }

    /**
     * Реализация команды {@code filter_less_than_age}.
     *
     * @param age значение, по которому происходит фильтрация.
     */
    public String filterLessThanAge(long age) {
        checkSync();
        String result = collection.stream()
                .filter(e -> e.getAge() < age)
                .map(Dragon::toString)
                .collect(Collectors.joining("\n"));

        return result.isEmpty()
                ? "Нет элементов моложе " + age
                : result;
    }


    /**
     * Реализация команды {@code info}.
     */
    public String info() {
        checkSync();
        return String.format("""
                Информация о коллекции:
                
                Тип: ArrayDeque
                Дата инициализации: %s
                Количество элементов: %d
                """, creationTime, collection.size());
    }

    /**
     * Реализация команды {@code print_unique_weight}.
     */
    public String printUniqueWeight() {
        checkSync();
        return collection.stream()
                .map(Dragon::getWeight)
                .filter(Objects::nonNull)
                .distinct()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }

    /**
     * Реализация команды {@code remove_by_id}.
     *
     * @param id id удаляемого объекта.
     */
    public String removeById(long id) {
        checkSync();
        boolean removed = collection.removeIf(e -> e.getId() == id);
        saveSync();
        return removed ? "Элемент удалён" : "Элемент с таким ID не найден";
    }

    /**
     * Реализация команды {@code remove_head}.
     */
    public String removeHead() {
        checkSync();
        Dragon head = collection.poll();
        saveSync();
        return head != null
                ? "Удален элемент: " + head
                : "Коллекция пуста";
    }


    /**
     * Реализация команды {@code show}.
     */
    public String show() {
        checkSync();
        if (collection.isEmpty()) return "Коллекция пуста";

        String result = collection.stream()
                .sorted(Comparator.comparing(Dragon::getWeight, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(Dragon::toString)
                .collect(Collectors.joining("\n---------------\n"));
        return "Элементы коллекции:\n" + result;
    }

    /**
     * Реализация команды {@code update}.
     *
     * @param id        id обновляемого объекта.
     * @param updDragon новое значение обьекта.
     */
    public String update(long id, Dragon updDragon) {
        checkSync();
        Optional<Dragon> found = collection.stream()
                .filter(e -> e.getId() == id)
                .findFirst();
        if (found.isPresent()) {
            Dragon old = found.get();
            updDragon.setId(id);
            updDragon.setCreationDate(old.getCreationDate());

            collection.remove(old);
            collection.add(updDragon);
            saveSync();
            return "Элемент с ID " + id + " успешно обновлен";
        }
        return "Элемент не найден";
    }

    /**
     * Получения максимального id среди объектов коллекции.
     *
     * @return максимальный id.
     */
    public long getMaxId() {
        return collection.stream()
                .mapToLong(Dragon::getId)
                .max()
                .orElse(0L);
    }

    /**
     * Валидация элементов коллекции.
     * Элементы, не прошедшие валидацию, удаляются из коллекции.
     */
    public void validate() {
        Set<Long> ids = new HashSet<>();
        collection = collection.stream()
                .filter(e -> {
                    if (ids.contains(e.getId())) {
                        System.out.println("Обнаружен повтор id, элемент пропущен: " + e.getId());
                        return false;
                    }
                    try {
                        e.validate();
                        ids.add(e.getId());
                        return true;
                    } catch (InvalidInputException ex) {
                        System.out.println("Ошибка в объекте ID " + e.getId() + ": " + ex.getMessage());
                        return false;
                    }
                })
                .collect(Collectors.toCollection(ArrayDeque::new));
    }

    public ArrayDeque<Dragon> getCollection() {
        return collection;
    }

    public void setCollection(ArrayDeque<Dragon> collection) {
        this.collection = collection;
    }
}
