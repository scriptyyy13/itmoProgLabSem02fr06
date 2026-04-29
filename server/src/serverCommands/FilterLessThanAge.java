package serverCommands;

import commands.CommandRequest;
import commands.FilterLessThanAgeRequest;
import exceptions.InvalidInputException;
import models.Dragon;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code filter_less_than_age}.
 */
public class FilterLessThanAge extends FilterLessThanAgeRequest {
    private CollectionManager collectionManager;

    public FilterLessThanAge(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public String execute(){
        return collectionManager.filterLessThanAge((long)getArgs()[0].getValue());
    }
}