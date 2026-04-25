import commands.*;
import models.Dragon;

public class CommandExecutor {
    private CollectionManager collection;

    public CommandExecutor(CollectionManager collection){
        this.collection = collection;
    }

    public void executeCommand(Command command){
        switch (command){
            case Add a:
                collection.add((Dragon) command.getArgs()[0].getValue());
                break;
            case AddIfMin amn:
                collection.addIfMin((Dragon) command.getArgs()[0].getValue());
                break;
            case AddIfMax amx:
                collection.addIfMax((Dragon) command.getArgs()[0].getValue());
                break;
            case AverageOfAge avr:
                collection.averageOfAge();
                break;
            case Clear cl:
                collection.clear();
                break;
            case Exit ext:
                collection.exit();
                break;
            case FilterLessThanAge flt:
                collection.filterLessThanAge((Long) command.getArgs()[0].getValue());
                break;
            case Help hlp:
                collection.help();
                break;
            case Info inf:
                collection.info();
                break;
            case PrintUniqueWeight puw:
                collection.printUniqueWeight();
                break;
            case RemoveById rbi:
                collection.removeById((Long) command.getArgs()[0].getValue());
                break;
            case RemoveHead a:
                collection.removeHead();
                break;
            case Show shw:
                collection.show();
                break;
            case Update upd:
                collection.update((Long) command.getArgs()[0].getValue(),(Dragon) command.getArgs()[1].getValue() );
                break;
            default:
                break;
        }
    }
}
