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
    public Message add(Dragon elem) {
        elem.setId(getMaxId() + 1);
        collection.addLast(elem);
        Message ans = new Message("Элемент добавлен");
        return ans;
    }

    /**
     * Реализация команды {@code add_if_max}.
     *
     * @param newDragon добавляемый {@code Dragon}.
     */
    public Message addIfMax(Dragon newDragon) {
        boolean isMax = collection.stream()
                .allMatch(e -> newDragon.compareTo(e) > 0);

        if (isMax || collection.isEmpty()) {
            newDragon.setId(getMaxId() + 1);
            collection.addLast(newDragon);
            return new Message("Элемент добавлен (был максимальным)");
        }
        return new Message("Элемент не максимальный");
    }

    /**
     * Реализация команды {@code add_if_min}.
     *
     * @param newDragon добавляемый {@code Dragon}.
     */
    public Message addIfMin(Dragon newDragon) {
        boolean isMin = collection.stream()
                .allMatch(e -> newDragon.compareTo(e) < 0);

        if (isMin || collection.isEmpty()) {
            newDragon.setId(getMaxId() + 1);
            collection.addLast(newDragon);
            return new Message("Элемент добавлен (был минимальным)");
        }
        return new Message("Элемент не минимальный");
    }

    /**
     * Реализация команды {@code average_of_age}.
     */
    public Message averageOfAge() {
        OptionalDouble average = collection.stream()
                .mapToLong(Dragon::getAge)
                .average();
        if (average.isPresent()) {
            return new Message(String.valueOf(average.getAsDouble()));
        } else {
            return new Message("Коллекция пуста");
        }
    }

    /**
     * Реализация команды {@code clear}.
     */
    public Message clear() {
        collection.clear();
        return new Message("Коллекция очищена");
    }

    /**
     * Реализация команды {@code filter_less_than_age}.
     *
     * @param age значение, по которому происходит фильтрация.
     */
    public Message filterLessThanAge(long age) {
        String result = collection.stream()
                .filter(e -> e.getAge() < age)
                .map(Dragon::toString)
                .collect(Collectors.joining("\n"));

        return result.isEmpty()
                ? new Message("Нет элементов моложе " + age)
                : new Message(result);
    }

    /**
     * Реализация команды {@code help}.
     */
    public Message help() {
        String helpMessage = """
                
                """;
        return new Message(helpMessage);
    }

    /**
     * Реализация команды {@code info}.
     */
    public Message info() {
        String info = String.format("""
                Информация о коллекции:
                
                Тип: ArrayDeque
                Дата инициализации: %s
                Количество элементов: %d
                """, creationTime, collection.size());
        return new Message(info);
    }

    /**
     * Реализация команды {@code print_unique_weight}.
     */
    public Message printUniqueWeight() {
        String weights = collection.stream()
                .map(Dragon::getWeight)
                .filter(Objects::nonNull)
                .distinct()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        return new Message(weights);
    }

    /**
     * Реализация команды {@code remove_by_id}.
     *
     * @param id id удаляемого объекта.
     */
    public Message removeById(long id) {
        boolean removed = collection.removeIf(e -> e.getId() == id);
        return removed ? new Message("Элемент удалён") : new Message("Элемент с таким ID не найден");
    }

    /**
     * Реализация команды {@code remove_head}.
     */
    public Message removeHead() {
        Dragon head = collection.poll();
        return head != null
                ? new Message("Удален элемент: " + head)
                : new Message("Коллекция пуста");
    }

    /**
     * Реализация команды {@code save}.
     */
    public void save() {
        XMLWriter xmlWriter = new XMLWriter();
        try {
            xmlWriter.dequeToXML(collection, ApplicationContext.collectionPath);
            System.out.println("Сохранения коллекции прошло успешно");
        } catch (XmlSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Реализация команды {@code show}.
     */
    public Message show() {
        if (collection.isEmpty()) return new Message("Коллекция пуста");

        String result = collection.stream()
                .sorted(Comparator.comparing(Dragon::getWeight, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(Dragon::toString)
                .collect(Collectors.joining("\n---------------\n"));

        return new Message("Элементы коллекции:\n" + result);
    }

    /**
     * Реализация команды {@code update}.
     *
     * @param id        id обновляемого объекта.
     * @param updDragon новое значение обьекта.
     */
    public Message update(long id, Dragon updDragon) {
        Optional<Dragon> found = collection.stream()
                .filter(e -> e.getId() == id)
                .findFirst();
        if (found.isPresent()) {
            Dragon old = found.get();
            updDragon.setId(id);
            updDragon.setCreationDate(old.getCreationDate());

            collection.remove(old);
            collection.add(updDragon);
            return new Message("Элемент с ID " + id + " успешно обновлен");
        }
        return new Message("Элемент не найден");
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
