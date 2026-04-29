package serverCommands;


import commands.CommandRequest;
import commands.ShowRequest;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code show}.
 */
public class Show extends Command {

    public Show(CommandRequest cmd, CollectionManager collection) {
        super(cmd,collection);
    }

    public String execute(){
        return getCollectionManager().show();
    }
}