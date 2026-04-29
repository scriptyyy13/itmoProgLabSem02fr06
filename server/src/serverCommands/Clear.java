package serverCommands;


import commands.ClearRequest;
import commands.CommandRequest;
import models.Dragon;
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
}