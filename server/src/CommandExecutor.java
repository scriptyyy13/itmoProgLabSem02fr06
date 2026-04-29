import commands.*;
import models.Dragon;
import tools.CollectionManager;
import tools.Message;

public class CommandExecutor {
    private CollectionManager collection;

    public CommandExecutor(CollectionManager collection){
        this.collection = collection;
    }

    public Message executeCommand(CommandRequest command){
        Message ans = new Message();
        if (command instanceof AddRequest) {
            ans = collection.add((Dragon) command.getArgs()[0].getValue());
        } else if (command instanceof AddIfMinRequest) {
            ans = collection.addIfMin((Dragon) command.getArgs()[0].getValue());
        } else if (command instanceof AddIfMaxRequest) {
            ans = collection.addIfMax((Dragon) command.getArgs()[0].getValue());
        } else if (command instanceof AverageOfAgeRequest) {
            ans = collection.averageOfAge();
        } else if (command instanceof ClearRequest) {
            ans = collection.clear();
        } else if (command instanceof FilterLessThanAgeRequest) {
            ans = collection.filterLessThanAge((Long) command.getArgs()[0].getValue());
        } else if (command instanceof HelpRequest) {
            ans = collection.help();
        } else if (command instanceof InfoRequest) {
            ans = collection.info();
        } else if (command instanceof PrintUniqueWeightRequest) {
            ans = collection.printUniqueWeight();
        } else if (command instanceof RemoveByIdRequest) {
            ans = collection.removeById((Long) command.getArgs()[0].getValue());
        } else if (command instanceof RemoveHeadRequest) {
            ans = collection.removeHead();
        } else if (command instanceof ShowRequest) {
            ans = collection.show();
        } else if (command instanceof UpdateRequest) {
            ans = collection.update((Long) command.getArgs()[0].getValue(), (Dragon) command.getArgs()[1].getValue());
        }
        return ans;
    }

    public CollectionManager getCollection(){return collection;}
}
