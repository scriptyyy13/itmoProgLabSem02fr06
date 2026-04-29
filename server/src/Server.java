import exceptions.XmlReadingException;
import models.Dragon;
import tools.CollectionManager;
import tools.ConfigManager;
import tools.ServerCommandManager;
import tools.XMLReader;

import java.util.ArrayDeque;

public class Server {
    public static void main(String[] args) {
        if (args.length > 0) {
            ConfigManager.scanConfig(args[0]);
        }
        ApplicationContext.collectionPath = ConfigManager.collectionFile;
        ArrayDeque<Dragon> collection = new ArrayDeque<Dragon>();
        try {
            XMLReader xmlReader = new XMLReader();
            collection = xmlReader.readXmlCollection(ApplicationContext.collectionPath);
        } catch (XmlReadingException e) {
            System.out.println(e.getMessage());
            System.out.println("Коллекция пуста");
        }
        CollectionManager collectionManager = new CollectionManager(collection);
        ServerCommandManager commandManager = new ServerCommandManager(ConfigManager.port, collectionManager);
        commandManager.start();
    }
}