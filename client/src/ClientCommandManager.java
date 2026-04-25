
import commands.*;
import exceptions.InvalidInputException;
import tools.Arg;
import tools.CollectionManager;
import tools.InputManager;
import tools.Reader;

import java.util.Arrays;
import java.util.HashMap;

public class ClientCommandManager {

    private final Reader reader;

    private final HashMap<String, Command> commands;
    /**
     * Создание экземепляра {@code CommandManager}.
     */
    public ClientCommandManager(Reader reader){
        this.reader = reader;
        commands = new HashMap<String,Command>();
        commands.put("help",new Help());
        commands.put("info",new Info());
        commands.put("show",new Show());
        commands.put("add",new Add());//
        commands.put("update",new Update());//
        commands.put("remove_by_id",new RemoveById());//
        commands.put("clear",new Clear());//
        commands.put("save",new Save());
        commands.put("execute_script",new ExecuteScript());//
        commands.put("exit",new Exit());
        commands.put("remove_head",new RemoveHead());//
        commands.put("add_if_max",new AddIfMax());//
        commands.put("add_if_min",new AddIfMin());//
        commands.put("average_of_age",new AverageOfAge());
        commands.put("filter_less_than_age",new FilterLessThanAge());
        commands.put("print_unique_speaking",new PrintUniqueWeight());
    }

    public void start() {
        while (true) {
            try {
                var str = reader.getLine();
                String[] splittedStr = str.split(" ");
                if (!commands.containsKey(splittedStr[0]))
                    throw new InvalidInputException("Команда не найдена. Введите help для помощи.");
                Command command = commands.get(splittedStr[0]);
                Arg[] args = Arg.toArgList(Arrays.copyOfRange(splittedStr, 1, splittedStr.length));
                System.out.println(args.length);
                ArgSetter.setArgs(command, args,reader);
                command.validate();
                /*
                    здесь отправляем команду на сервак со всеми аргументами через отдельный класс-отправитель
                 */
                if(command instanceof Exit) break;
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Неверный формат");
            }
        }
    }
}
