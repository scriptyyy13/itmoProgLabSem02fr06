package serverMainFiles;

import database.DatabaseManager;
import serverExceptions.XmlReadingException;
import models.Dragon;
import serverTools.CollectionManager;
import serverTools.ConfigManager;
import serverTools.ServerCommandManager;

import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * mainFiles.Main-класс для сервера
 */
public class Server {
    public static void main(String[] args) {
        if (args.length > 0) {
            ConfigManager.scanConfig(args[0]);
        } else {
            ConfigManager.scanConfig("server.preps");
        }

        ConcurrentLinkedDeque<Dragon> collection = new ConcurrentLinkedDeque<Dragon>();
        try {
            DatabaseManager db = DatabaseManager.getInstance();
            collection = db.fetchAllDragons();
        } catch (XmlReadingException e) {
            System.out.println(e.getMessage());
            System.out.println("Коллекция пуста");
        } catch (SQLException e) {
            System.err.println("Ошибка инициализации БД: " + e.getMessage());
        }

        CollectionManager collectionManager = new CollectionManager(collection);
        ServerCommandManager commandManager = new ServerCommandManager(ConfigManager.port, collectionManager);
        commandManager.start();
    }
}