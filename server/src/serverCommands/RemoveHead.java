package serverCommands;


import commands.CommandRequest;
import commands.RemoveHeadRequest;
import models.Dragon;
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

}