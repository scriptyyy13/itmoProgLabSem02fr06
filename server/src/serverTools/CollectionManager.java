package serverTools;

import exceptions.InvalidInputException;
import models.Dragon;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
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

    public CollectionManager(ConcurrentLinkedDeque<Dragon> collection) {
        this.creationTime = new Date();
        this.collection = collection;
    }

    /**
     * Реализация команды {@code add}.
     *
     * @param elem добавляемый {@code Dragon}.
     */
    public String add(Dragon elem) {
        collection.addLast(elem);
        return "Элемент добавлен";
    }

    /**
     * Реализация команды {@code add_if_max}.
     *
     * @param newDragon добавляемый {@code Dragon}.
     */
    public String addIfMax(Dragon newDragon) {
        boolean isMax = collection.stream()
                .allMatch(e -> newDragon.compareTo(e) > 0);

        if (isMax) {
            collection.addLast(newDragon);
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
        boolean isMin = collection.stream()
                .allMatch(e -> newDragon.compareTo(e) < 0);

        if (isMin) {
            collection.addLast(newDragon);
            return "Элемент добавлен (был минимальным)";
        }
        return "Элемент не минимальный";
    }

    /**
     * Реализация команды {@code average_of_age}.
     */
    public String averageOfAge() {
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
        return "Коллекция очищена";
    }

    /**
     * Реализация команды {@code filter_less_than_age}.
     *
     * @param age значение, по которому происходит фильтрация.
     */
    public String filterLessThanAge(long age) {
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
        boolean removed = collection.removeIf(e -> e.getId() == id);
        return removed ? "Элемент удалён" : "Элемент с таким ID не найден";
    }

    /**
     * Реализация команды {@code remove_head}.
     */
    public String removeHead() {
        Dragon head = collection.poll();
        return head != null
                ? "Удален элемент: " + head
                : "Коллекция пуста";
    }

    /**
     * Реализация команды {@code show}.
     */
    public String show() {
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
        Optional<Dragon> found = collection.stream()
                .filter(e -> e.getId() == id)
                .findFirst();
        if (found.isPresent()) {
            Dragon old = found.get();
            updDragon.setId(id);
            updDragon.setCreationDate(old.getCreationDate());

            collection.remove(old);
            collection.add(updDragon);
            return "Элемент с ID " + id + " успешно обновлен";
        }
        return "Элемент не найден";
    }

    /**
     * Валидация элементов коллекции.
     * Элементы, не прошедшие валидацию, удаляются из коллекции.
     */
    public void validate() {
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
    }

    public ConcurrentLinkedDeque<Dragon> getCollection() {
        return collection;
    }

    public void setCollection(ConcurrentLinkedDeque<Dragon> collection) {
        this.collection = collection;
    }
}