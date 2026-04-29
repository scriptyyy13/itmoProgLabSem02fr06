package serverCommands;


import commands.ShowRequest;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code show}.
 */
public class Show extends ShowRequest {
    private CollectionManager collectionManager;

    public Show(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public String execute(){
        return collectionManager.show();
    }
    public void setCollectionManager(CollectionManager collectionManager){this.collectionManager = collectionManager;}
}