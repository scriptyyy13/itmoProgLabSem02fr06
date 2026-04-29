package serverCommands;

import commands.CommandRequest;
import tools.Arg;
import tools.CollectionManager;

public abstract class Command {
    private CollectionManager collectionManager;
    private Arg[] args;

    public Command(CommandRequest commandRequest, CollectionManager collectionManager) {
        args = commandRequest.getArgs();
        this.collectionManager = collectionManager;
    }

    public String execute(){return null;}

    public Arg[] getArgs(){return args;}
    public  CollectionManager getCollectionManager(){return collectionManager;}
}
