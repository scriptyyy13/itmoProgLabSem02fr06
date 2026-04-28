import exceptions.XmlReadingException;
import models.Dragon;

import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayDeque;

public class Server {
    public static void main(String[] args) {
        ApplicationContext.collectionPath = "collection.xml";
        ArrayDeque<Dragon> collection = new ArrayDeque<Dragon>();
        try {
            XMLReader xmlReader = new XMLReader();
            collection = xmlReader.readXmlCollection(ApplicationContext.collectionPath);
        }catch (XmlReadingException e){
            System.out.println(e.getMessage());
            System.out.println("Коллекция пуста");
        }
        CollectionManager collectionManager = new CollectionManager(collection);
        ServerCommandManager commandManager= new ServerCommandManager(8085,collectionManager);
        commandManager.start();
    }
}