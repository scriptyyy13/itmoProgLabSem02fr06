package serverMainFiles;

import serverExceptions.XmlReadingException;
import models.Dragon;
import serverTools.CollectionManager;
import serverTools.ConfigManager;
import serverTools.ServerCommandManager;
import serverTools.XMLReader;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * mainFiles.Main-класс для сервера
 */
public class Server {
    public static void main(String[] args) {
        if (args.length > 0) {
            ConfigManager.scanConfig(args[0]);
        }
        ApplicationContext.collectionPath = ConfigManager.collectionFile;
        ConcurrentLinkedDeque<Dragon> collection = new ConcurrentLinkedDeque<Dragon>();
        try {

            collection = XMLReader.readXmlCollection(ApplicationContext.collectionPath);
        } catch (XmlReadingException e) {
            System.out.println(e.getMessage());
            System.out.println("Коллекция пуста");
        }
        CollectionManager collectionManager = new CollectionManager(collection);
        ServerCommandManager commandManager = new ServerCommandManager(ConfigManager.port, collectionManager);
        commandManager.start();
    }
}