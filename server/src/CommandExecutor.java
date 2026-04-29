import commands.*;
import models.Dragon;
import tools.Message;

public class CommandExecutor {
    private CollectionManager collection;

    public CommandExecutor(CollectionManager collection){
        this.collection = collection;
    }

    public Message executeCommand(Command command){
        Message ans = new Message();
        if (command instanceof Add) {
            ans = collection.add((Dragon) command.getArgs()[0].getValue());
        } else if (command instanceof AddIfMin) {
            ans = collection.addIfMin((Dragon) command.getArgs()[0].getValue());
        } else if (command instanceof AddIfMax) {
            ans = collection.addIfMax((Dragon) command.getArgs()[0].getValue());
        } else if (command instanceof AverageOfAge) {
            ans = collection.averageOfAge();
        } else if (command instanceof Clear) {
            ans = collection.clear();
        } else if (command instanceof FilterLessThanAge) {
            ans = collection.filterLessThanAge((Long) command.getArgs()[0].getValue());
        } else if (command instanceof Help) {
            ans = collection.help();
        } else if (command instanceof Info) {
            ans = collection.info();
        } else if (command instanceof PrintUniqueWeight) {
            ans = collection.printUniqueWeight();
        } else if (command instanceof RemoveById) {
            ans = collection.removeById((Long) command.getArgs()[0].getValue());
        } else if (command instanceof RemoveHead) {
            ans = collection.removeHead();
        } else if (command instanceof Show) {
            ans = collection.show();
        } else if (command instanceof Update) {
            ans = collection.update((Long) command.getArgs()[0].getValue(), (Dragon) command.getArgs()[1].getValue());
        }
        return ans;
    }

    public CollectionManager getCollection(){return collection;}
}
