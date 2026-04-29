package serverCommands;


import commands.CommandRequest;
import commands.PrintUniqueWeightRequest;
import models.Dragon;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code print_unique_weight}.
 */
public class PrintUniqueWeight extends PrintUniqueWeightRequest {
    private CollectionManager collectionManager;

    public PrintUniqueWeight(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public String execute(){
        return collectionManager.printUniqueWeight();
    }

}