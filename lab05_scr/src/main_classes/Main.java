package main_classes;

import exceptions.XmlReadingException;
import models.Dragon;
import tools.*;

import java.util.ArrayDeque;

/**
 * Точка входа в программу.
 */

public class Main {
    /**
     * Запускает программу.
     *
     * @param args аргументы командной строки.
     */
    public static void main(String[] args) {
        ArrayDeque<Dragon> collection = new ArrayDeque<Dragon>();
        try {
            XMLReader xmlReader = new XMLReader();
            collection = xmlReader.readXmlCollection(args[0]);
        } catch (XmlReadingException e) {
            OutputManager.println(e.getMessage());
            OutputManager.println("Коллекция пуста");
        }
        Reader reader = new Reader();
        CollectionManager collectionManager = new CollectionManager(collection);
        CommandManager commandManager = new CommandManager(collectionManager, reader);
        commandManager.startManage();
    }
}