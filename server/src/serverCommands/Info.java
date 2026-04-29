package serverCommands;


import commands.InfoRequest;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code info}.
 */
public class Info extends InfoRequest {
    private CollectionManager collectionManager;

    public Info(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public String execute(){
        return collectionManager.info();
    }
    public void setCollectionManager(CollectionManager collectionManager){this.collectionManager = collectionManager;}

}