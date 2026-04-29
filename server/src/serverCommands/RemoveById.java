package serverCommands;


import commands.RemoveByIdRequest;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code remove_by_id}.
 */
public class RemoveById extends RemoveByIdRequest {
    private CollectionManager collectionManager;

    public RemoveById(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public String execute(){
        return collectionManager.removeById((long)getArgs()[0].getValue());
    }
    public void setCollectionManager(CollectionManager collectionManager){this.collectionManager = collectionManager;}
}