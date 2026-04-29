package serverCommands;


import commands.ClearRequest;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code clear}.
 */
public class Clear extends ClearRequest {
    private CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public String execute(){
        return collectionManager.clear();
    }
    public void setCollectionManager(CollectionManager collectionManager){this.collectionManager = collectionManager;}
}