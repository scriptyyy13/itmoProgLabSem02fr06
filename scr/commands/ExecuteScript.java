package commands;

import exceptions.InvalidInputException;
import exceptions.RecursionLimitException;
import tools.CollectionManager;
import tools.InputManager;

import java.io.FileNotFoundException;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code execute_script}.
 */
public class ExecuteScript extends Command{
    /**
     * Счетчик исполняемых в данный момент скриптов, необходим для {@link exceptions.RecursionLimitException}.
     */
    public static int runningScripts = 0;

    /**
     * Создание экземпляра команды {@code execute_script}.
     * @param manager {@link tools.CollectionManager}, в котором будет исполнена команда.
     */
    public ExecuteScript(CollectionManager manager){super(manager);}
    public ExecuteScript(){}
    /**
     * Запуск соответствующего метода в {@link tools.CollectionManager}.
     */
    public void execute(){
        try {
            validate();
            getManager().executeScript((String)getArgs()[0].getValue());
        }catch (InvalidInputException e){
            System.out.println(e.getMessage());
        }

    }
    /**
     * Валидация аргументов команды.
     *
     * @throws InvalidInputException исключение, выбрасываемое в случае неуспешной валидации.
     */
    public void validate() throws InvalidInputException {
        if(!(getArgs()[0].getValue() instanceof String)) throw new InvalidInputException("Неверный формат");
    }
}
