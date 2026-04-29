package serverCommands;

import commands.AddRequest;
import models.Dragon;
import tools.CollectionManager;
import tools.CollectionWrapper;


/**
 * Класс, отвечающий за сохрание экземпляра команды {@code add}.
 */

public class Add extends AddRequest {
    private CollectionManager collectionManager;

    public Add(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public String execute(){
        return collectionManager.add((Dragon)getArgs()[0].getValue());
    }

    public void setCollectionManager(CollectionManager collectionManager){this.collectionManager = collectionManager;}
}
