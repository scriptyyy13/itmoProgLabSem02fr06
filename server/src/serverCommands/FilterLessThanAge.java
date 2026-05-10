package serverCommands;

import commands.CommandRequest;
import commands.FilterLessThanAgeRequest;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code filter_less_than_age}.
 */
public class FilterLessThanAge extends Command {

    public FilterLessThanAge(CommandRequest cmd, CollectionManager collection) {
        super(cmd, collection);
    }

    public String execute() {
        String ageString = getArgs()[0].getValue().toString();
        long age = Long.parseLong(ageString);
        return getCollectionManager().filterLessThanAge(age);
    }
}