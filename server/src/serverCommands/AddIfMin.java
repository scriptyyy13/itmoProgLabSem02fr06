package serverCommands;

import commands.AddIfMinRequest;
import commands.CommandRequest;
import exceptions.InvalidInputException;
import models.Dragon;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code add_if_min}.
 */
public class AddIfMin extends AddIfMinRequest {
    private CollectionManager collectionManager;

    public AddIfMin(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public String execute(){
        return collectionManager.addIfMin((Dragon)getArgs()[0].getValue());
    }
}