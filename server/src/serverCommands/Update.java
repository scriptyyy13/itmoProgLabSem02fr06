package serverCommands;

import commands.UpdateRequest;
import models.Dragon;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code update}.
 */
public class Update extends UpdateRequest {
    private CollectionManager collectionManager;

    public Update(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public String execute(){
        return collectionManager.update((long)getArgs()[0].getValue(), (Dragon) getArgs()[0].getValue());
    }
    public void setCollectionManager(CollectionManager collectionManager){this.collectionManager = collectionManager;}
}