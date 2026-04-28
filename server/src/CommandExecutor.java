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
        switch (command){
            case Add a:
                ans =collection.add((Dragon) command.getArgs()[0].getValue());
                break;
            case AddIfMin amn:
                ans = collection.addIfMin((Dragon) command.getArgs()[0].getValue());
                break;
            case AddIfMax amx:
                ans = collection.addIfMax((Dragon) command.getArgs()[0].getValue());
                break;
            case AverageOfAge avr:
                ans = collection.averageOfAge();
                break;
            case Clear cl:
                ans = collection.clear();
                break;
            case FilterLessThanAge flt:
                ans = collection.filterLessThanAge((Long) command.getArgs()[0].getValue());
                break;
            case Help hlp:
                ans = collection.help();
                break;
            case Info inf:
                ans = collection.info();
                break;
            case PrintUniqueWeight puw:
                ans = collection.printUniqueWeight();
                break;
            case RemoveById rbi:
                ans = collection.removeById((Long) command.getArgs()[0].getValue());
                break;
            case RemoveHead a:
                ans = collection.removeHead();
                break;
            case Show shw:
                ans = collection.show();
                break;
            case Update upd:
                ans = collection.update((Long) command.getArgs()[0].getValue(),(Dragon) command.getArgs()[1].getValue() );
                break;
            default:
                break;
        }
        return ans;
    }

    public CollectionManager getCollection(){return collection;}
}
