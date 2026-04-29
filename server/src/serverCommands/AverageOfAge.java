package serverCommands;


import commands.AverageOfAgeRequest;
import commands.CommandRequest;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code average_of_age}.
 */
public class AverageOfAge extends Command {

    public AverageOfAge(CommandRequest cmd, CollectionManager collection) {
        super(cmd,collection);
    }

    public String execute(){
        return getCollectionManager().averageOfAge();
    }

}