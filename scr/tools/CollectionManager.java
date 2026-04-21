package tools;

import commands.*;
import exceptions.EmptyDequeException;
import exceptions.InvalidInputException;
import exceptions.RecursionLimitException;
import exceptions.XmlSaveException;
import main_classes.ApplicationContext;
import models.Dragon;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * Коллекция с методами для ней управления.
 */
public class CollectionManager {
    /**
     * Коллекция.
     */
    private ArrayDeque<Dragon> collection;

    /**
     * Создание экзмепляра {@code CollectionManager}.
     * @param collection коллекция.
     */
    public CollectionManager( ArrayDeque<Dragon> collection){
        this.collection = collection;
    }

    /**
     * Реализация команды {@code add}.
     * @param elem добавляемый {@code Dragon}.
     */
    public void add(Dragon elem){
        elem.setId(getMaxId()+1);
        collection.addLast(elem);
        System.out.println("Элемент добавлен");
    }
    /**
     * Реализация команды {@code add_if_max}.
     * @param newDragon добавляемый {@code Dragon}.
     */
    public void addIfMax(Dragon newDragon){
        newDragon.setId(getMaxId()+1);
        Boolean isMax = true;
        for(var elem : collection){
            if( newDragon.compareTo(elem) <0) isMax = false;
        }
        if(isMax){
            collection.addLast(newDragon);
            System.out.println("Элемент добавлен");
        }else {
            System.out.println("Элемент не максимальный");
        }
    }
    /**
     * Реализация команды {@code add_if_min}.
     * @param newDragon добавляемый {@code Dragon}.
     */
    public void addIfMin(Dragon newDragon){
        newDragon.setId(getMaxId()+1);
        Boolean isMin = true;
        for(var elem : collection){
            if( newDragon.compareTo(elem) >0) isMin = false;
        }
        if(isMin){
            collection.addLast(newDragon);
            System.out.println("Элемент добавлен");
        }else {
            System.out.println("Элемент не минимальный");
        }
    }
    /**
     * Реализация команды {@code average_of_age}.
     */
    public void averageOfAge(){
        try {
            if(collection.isEmpty()) throw new EmptyDequeException("Коллекция пуста");
            Float sum = 0F;
            for(var elem : collection){
                sum+= (float) elem.getAge();
            }
            Float average = sum/(float)collection.size();
            System.out.println(average);
        }catch (EmptyDequeException e){
            System.out.println(e.getMessage());
        }
    }
    /**
     * Реализация команды {@code clear}.
     */
    public void clear(){
        collection.clear();
        System.out.println("Коллекция очищена");
    }
    /**
     * Реализация команды {@code execute_script}.
     * @param path путь до скрипта.
     */
    public void executeScript(String path){
        try {
            if (ExecuteScript.runningScripts >100) throw new RecursionLimitException("Превышен предел рекурсии");
            Reader scriptReader =  new Reader(path);
            CommandManager commandManager = new CommandManager(this,scriptReader,false);
            ExecuteScript.runningScripts +=1;
            commandManager.startManage();
            ExecuteScript.runningScripts -=1;
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        }catch (RecursionLimitException e){
            System.out.println(e.getMessage());
        }
    }
    /**
     * Реализация команды {@code exit}.
     */
    public void exit(){
        System.exit(0);
    }
    /**
     * Реализация команды {@code filter_less_than_age}.
     * @param age значение, по которому происходит фильтрация.
     */
    public void filterLessThanAge(long age){
        String filtredCollection = "";
        for(var elem : collection){
            if(elem.getAge()<age) filtredCollection+=elem.toString();
        }
        System.out.println(filtredCollection);
    }
    /**
     * Реализация команды {@code help}.
     */
    public void help(){
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
        System.out.println(helpMessage);
    }
    /**
     * Реализация команды {@code info}.
     */
    public void info(){
        String info = String.format("""
                Информация о коллекции:
                
                Тип: ArrayDeque
                Дата инициализации: %s
                Количество элементов: %d
                """, ApplicationContext.creationTime,collection.size());
        System.out.println(info);
    }
    /**
     * Реализация команды {@code print_unique_weight}.
     */
    public void printUniqueWeight(){
        HashSet<Integer> set = new HashSet<>();
        for(var elem: collection){
            set.add(elem.getWeight());
        }
        System.out.println(set.toString());
    }
    /**
     * Реализация команды {@code remove_by_id}.
     * @param id id удаляемого объекта.
     */
    public void removeById(long id){
        for(var elem : collection){
            if(elem.getId() == id){
                collection.remove(elem);
                System.out.println("Элемент удалён");
                return;
            }
        }
        System.out.println("Элемент не найден");
    }
    /**
     * Реализация команды {@code remove_head}.
     */
    public void removeHead(){
        try {
            if(collection.isEmpty()) throw new EmptyDequeException("Коллекция пуста");
           System.out.println(collection.poll().toString());
        } catch (EmptyDequeException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Реализация команды {@code save}.
     */
    public void save(){
        XMLWriter xmlWriter = new XMLWriter();
        try {
            xmlWriter.dequeToXML(collection,ApplicationContext.collectionPath);
            System.out.println("Запись файла прошла успешно");
        }catch (XmlSaveException e){
            System.out.println(e.getMessage());
        }
    }
    /**
     * Реализация команды {@code show}.
     */
    public void show(){
        for(var elem : collection){
            System.out.println(elem);
        }
    }
    /**
     * Реализация команды {@code update}.
     * @param id id обновляемого объекта.
     * @param updDragon новое значение обьекта.
     */
    public void update(long id,Dragon updDragon){
        Dragon[] array = collection.toArray(new Dragon[0]);
        for(int i =0;i< array.length;i++){
            if(array[i].getId() == id){
                Date date = array[i].getCreationDate();
                array[i] = updDragon;
                array[i].setId(id);
                array[i].setCreationDate(date);
                collection = new ArrayDeque<>(Arrays.asList(array));
                System.out.println("Элемент обновлён");
                return;
            }
        }
        System.out.println("Элемент не найден");
    }
    /**
     * Получения максимального id среди объектов коллекции.
     * @return максимальный id.
     */
    public long getMaxId(){
        long id = 0;
        for(var e : collection){
            id = Math.max(id,e.getId());
        }
        return id;
    }
    /**
     * Валидация элементов коллекции.
     * Элементы, не прошедшие валидацию, удаляются из коллекции.
     */
    public void validate(){
        HashSet<Long> ids = new HashSet<Long>();
        ArrayDeque<Dragon> vCollection= new ArrayDeque<Dragon>();
        for(var e : collection){
            if(ids.contains(e.getId()) ) System.out.println("Обнаружен повтор id, элемент пропущен");
            else {
                ids.add(e.getId());
                try {
                    e.validate();
                    vCollection.add(e);
                } catch (InvalidInputException ex) {
                    System.out.println(ex.getMessage());
                }
            }

        }
        collection =vCollection;
    }
}
