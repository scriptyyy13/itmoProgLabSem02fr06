package serverCommands;


import commands.RemoveHeadRequest;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code remove_head}.
 */
public class RemoveHead extends RemoveHeadRequest {
    private CollectionManager collectionManager;

    public RemoveHead(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public String execute(){
        return collectionManager.removeHead();
    }
    public void setCollectionManager(CollectionManager collectionManager){this.collectionManager = collectionManager;}

}