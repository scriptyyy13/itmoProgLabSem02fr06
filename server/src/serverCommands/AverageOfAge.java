package serverCommands;


import commands.AverageOfAgeRequest;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code average_of_age}.
 */
public class AverageOfAge extends AverageOfAgeRequest {
    private CollectionManager collectionManager;

    public AverageOfAge(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public String execute(){
        return collectionManager.averageOfAge();
    }
    public void setCollectionManager(CollectionManager collectionManager){this.collectionManager = collectionManager;}

}