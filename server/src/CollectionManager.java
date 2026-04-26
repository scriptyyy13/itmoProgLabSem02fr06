import commands.*;
import exceptions.EmptyDequeException;
import exceptions.InvalidInputException;
import exceptions.RecursionLimitException;
import exceptions.XmlSaveException;
import models.Dragon;
import tools.Message;

import java.util.*;

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
     * Создание экзмепляра {@code CollectionManager}.
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
        Message ans =new Message("Элемент добавлен");
        return ans;
    }

    /**
     * Реализация команды {@code add_if_max}.
     *
     * @param newDragon добавляемый {@code Dragon}.
     */
    public Message addIfMax(Dragon newDragon) {
        Message ans = new Message();
        newDragon.setId(getMaxId() + 1);
        Boolean isMax = true;
        for (var elem : collection) {
            if (newDragon.compareTo(elem) < 0) isMax = false;
        }
        if (isMax) {
            collection.addLast(newDragon);
            ans.setText("Элемент добавлен");
        } else {
            ans.setText("Элемент не максимальный");
        }
        return ans;
    }

    /**
     * Реализация команды {@code add_if_min}.
     *
     * @param newDragon добавляемый {@code Dragon}.
     */
    public Message addIfMin(Dragon newDragon) {
        Message ans = new Message();
        newDragon.setId(getMaxId() + 1);
        Boolean isMin = true;
        for (var elem : collection) {
            if (newDragon.compareTo(elem) > 0) isMin = false;
        }
        if (isMin) {
            collection.addLast(newDragon);
            ans.setText("Элемент добавлен");
        } else {
            ans.setText("Элемент не минимальный");
        }
        return ans;
    }

    /**
     * Реализация команды {@code average_of_age}.
     */
    public Message averageOfAge() {
        try {
            if (collection.isEmpty()) throw new EmptyDequeException("Коллекция пуста");
            Float sum = 0F;
            for (var elem : collection) {
                sum += (float) elem.getAge();
            }
            Float average = sum / (float) collection.size();
            return new Message(String.valueOf(average));
        } catch (EmptyDequeException e) {
            return new Message(e.getMessage());
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
     * Реализация команды {@code exit}.
     */
    public void exit() {
        System.exit(0);
    }

    /**
     * Реализация команды {@code filter_less_than_age}.
     *
     * @param age значение, по которому происходит фильтрация.
     */
    public Message filterLessThanAge(long age) {
        String filtredCollection = "";
        for (var elem : collection) {
            if (elem.getAge() < age) filtredCollection += elem.toString();
        }
        return new Message(filtredCollection);
    }

    /**
     * Реализация команды {@code help}.
     */
    public Message help() {
        String helpMessage = """
                Доступные команды:
                
                help - вывести справку по доступным командам
                info - вывести информацию о коллекции
                show - вывести все элементы коллекции
                add - добавить новый элемент в коллекцию
                update id - обновить значение элемента, id которого равен заданному
                remove_by_id id - удалить элемент по id
                clear - очистить коллекцию
                save - сохранить коллекцию в файл
                execute_script file_name - считать и исполнить скрипт из указанного файла
                exit - завершить программу
                remove_head - вывести первый элемент коллекции и удалить его
                add_if_max - добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции
                add_if_min - добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции
                average_of_age - вывести среднее значение поля age для всех элементов коллекции
                filter_less_than_age age - вывести элементы, значение поля age которых меньше заданного
                print_unique_weight - вывести уникальные значения поля weight всех элементов в коллекции
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
        HashSet<Integer> set = new HashSet<>();
        for (var elem : collection) {
            set.add(elem.getWeight());
        }
        return new Message(set.toString());
    }

    /**
     * Реализация команды {@code remove_by_id}.
     *
     * @param id id удаляемого объекта.
     */
    public Message removeById(long id) {
        for (var elem : collection) {
            if (elem.getId() == id) {
                collection.remove(elem);
                return new Message("Элемент удалён");
            }
        }
        return new Message("Элемент не найден");
    }

    /**
     * Реализация команды {@code remove_head}.
     */
    public Message removeHead() {
        try {
            if (collection.isEmpty()) throw new EmptyDequeException("Коллекция пуста");
            return new Message(collection.poll().toString());
        } catch (EmptyDequeException e) {
            return new Message(e.getMessage());
        }
    }

    /**
     * Реализация команды {@code save}.
     */
    public void save() {
        XMLWriter xmlWriter = new XMLWriter();
        try {
            xmlWriter.dequeToXML(collection, ApplicationContext.collectionPath);
            OutputManager.println("Запись файла прошла успешно");
        } catch (XmlSaveException e) {
            OutputManager.println(e.getMessage());
        }
    }

    /**
     * Реализация команды {@code show}.
     */
    public Message show() {
        StringBuilder ans = new StringBuilder();
        for (var elem : collection) {
            ans.append(String.valueOf(elem));
        }
        return new Message(ans.toString());

    }

    /**
     * Реализация команды {@code update}.
     *
     * @param id        id обновляемого объекта.
     * @param updDragon новое значение обьекта.
     */
    public Message update(long id, Dragon updDragon) {
        Dragon[] array = collection.toArray(new Dragon[0]);
        for (int i = 0; i < array.length; i++) {
            if (array[i].getId() == id) {
                Date date = array[i].getCreationDate();
                array[i] = updDragon;
                array[i].setId(id);
                array[i].setCreationDate(date);
                collection = new ArrayDeque<>(Arrays.asList(array));
                return new Message("Элемент обновлён");
            }
        }
        return new Message("Элемент не найден");
    }

    /**
     * Получения максимального id среди объектов коллекции.
     *
     * @return максимальный id.
     */
    public long getMaxId() {
        long id = 0;
        for (var e : collection) {
            id = Math.max(id, e.getId());
        }
        return id;
    }

    /**
     * Валидация элементов коллекции.
     * Элементы, не прошедшие валидацию, удаляются из коллекции.
     */
    public void validate() {
        HashSet<Long> ids = new HashSet<Long>();
        ArrayDeque<Dragon> vCollection = new ArrayDeque<Dragon>();
        for (var e : collection) {
            if (ids.contains(e.getId())) {
                OutputManager.println("Обнаружен повтор id, элемент пропущен");
            } else {
                ids.add(e.getId());
                try {
                    e.validate();
                    vCollection.add(e);
                } catch (InvalidInputException ex) {
                    OutputManager.println(ex.getMessage());
                }
            }
        }
        collection = vCollection;
    }
}
