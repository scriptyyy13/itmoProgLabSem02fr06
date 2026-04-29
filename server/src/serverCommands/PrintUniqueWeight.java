package serverCommands;


import commands.CommandRequest;
import commands.PrintUniqueWeightRequest;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code print_unique_weight}.
 */
public class PrintUniqueWeight extends Command {

    public PrintUniqueWeight(CommandRequest cmd, CollectionManager collection) {
        super(cmd,collection);
    }

    public String execute(){
        return getCollectionManager().printUniqueWeight();
    }

}