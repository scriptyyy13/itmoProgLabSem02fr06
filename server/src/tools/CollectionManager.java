package tools;

import exceptions.InvalidInputException;
import exceptions.XmlSaveException;
import models.Dragon;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Коллекция с методами для ней управления.
 */
public class CollectionManager {
    /**
     * Коллекция.
     */
    private ArrayDeque<Dragon> collection;
    private Date creationTime;

    /**
     * Создание экзмепляра {@code tools.CollectionManager}.
     *
     * @param collection коллекция.
     */
    public CollectionManager(ArrayDeque<Dragon> collection) {
        this.creationTime = new Date();
        this.collection = collection;
    }

    /**
     * Реализация команды {@code add}.
     *
     * @param elem добавляемый {@code Dragon}.
     */
    public String add(Dragon elem) {
        elem.setId(getMaxId() + 1);
        collection.addLast(elem);
        String ans = new String("Элемент добавлен");
        return ans;
    }

    /**
     * Реализация команды {@code add_if_max}.
     *
     * @param newDragon добавляемый {@code Dragon}.
     */
    public String addIfMax(Dragon newDragon) {
        boolean isMax = collection.stream()
                .allMatch(e -> newDragon.compareTo(e) > 0);

        if (isMax || collection.isEmpty()) {
            newDragon.setId(getMaxId() + 1);
            collection.addLast(newDragon);
            return new String("Элемент добавлен (был максимальным)");
        }
        return new String("Элемент не максимальный");
    }

    /**
     * Реализация команды {@code add_if_min}.
     *
     * @param newDragon добавляемый {@code Dragon}.
     */
    public String addIfMin(Dragon newDragon) {
        boolean isMin = collection.stream()
                .allMatch(e -> newDragon.compareTo(e) < 0);

        if (isMin || collection.isEmpty()) {
            newDragon.setId(getMaxId() + 1);
            collection.addLast(newDragon);
            return new String("Элемент добавлен (был минимальным)");
        }
        return new String("Элемент не минимальный");
    }

    /**
     * Реализация команды {@code average_of_age}.
     */
    public String averageOfAge() {
        OptionalDouble average = collection.stream()
                .mapToLong(Dragon::getAge)
                .average();
        if (average.isPresent()) {
            return new String(String.valueOf(average.getAsDouble()));
        } else {
            return new String("Коллекция пуста");
        }
    }

    /**
     * Реализация команды {@code clear}.
     */
    public String clear() {
        collection.clear();
        return new String("Коллекция очищена");
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
                ? new String("Нет элементов моложе " + age)
                : new String(result);
    }


    /**
     * Реализация команды {@code info}.
     */
    public String info() {
        String info = String.format("""
                Информация о коллекции:
                
                Тип: ArrayDeque
                Дата инициализации: %s
                Количество элементов: %d
                """, creationTime, collection.size());
        return new String(info);
    }

    /**
     * Реализация команды {@code print_unique_weight}.
     */
    public String printUniqueWeight() {
        String weights = collection.stream()
                .map(Dragon::getWeight)
                .filter(Objects::nonNull)
                .distinct()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        return new String(weights);
    }

    /**
     * Реализация команды {@code remove_by_id}.
     *
     * @param id id удаляемого объекта.
     */
    public String removeById(long id) {
        boolean removed = collection.removeIf(e -> e.getId() == id);
        return removed ? new String("Элемент удалён") : new String("Элемент с таким ID не найден");
    }

    /**
     * Реализация команды {@code remove_head}.
     */
    public String removeHead() {
        Dragon head = collection.poll();
        return head != null
                ? new String("Удален элемент: " + head)
                : new String("Коллекция пуста");
    }


    /**
     * Реализация команды {@code show}.
     */
    public String show() {
        if (collection.isEmpty()) return new String("Коллекция пуста");

        String result = collection.stream()
                .sorted(Comparator.comparing(Dragon::getWeight, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(Dragon::toString)
                .collect(Collectors.joining("\n---------------\n"));

        return new String("Элементы коллекции:\n" + result);
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
            return new String("Элемент с ID " + id + " успешно обновлен");
        }
        return new String("Элемент не найден");
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

    public ArrayDeque<Dragon> getCollection(){return collection;}
}
