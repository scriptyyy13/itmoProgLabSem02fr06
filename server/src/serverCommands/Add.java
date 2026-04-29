package serverCommands;

import commands.AddRequest;
import commands.CommandRequest;
import exceptions.InvalidInputException;
import models.Dragon;
import tools.CollectionManager;


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
}
