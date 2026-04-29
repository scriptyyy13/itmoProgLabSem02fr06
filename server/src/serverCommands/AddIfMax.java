package serverCommands;

import commands.AddIfMaxRequest;
import commands.CommandRequest;
import exceptions.InvalidInputException;
import models.Dragon;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code add_if_max}.
 */
public class AddIfMax extends AddIfMaxRequest {
    private CollectionManager collectionManager;

    public AddIfMax(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public String execute(){
        return collectionManager.addIfMax((Dragon)getArgs()[0].getValue());
    }
}