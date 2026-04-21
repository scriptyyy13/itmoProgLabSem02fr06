package commands;

import exceptions.InvalidInputException;
import models.Dragon;
import tools.CollectionManager;
/**
 * Класс, отвечающий за сохрание экземпляра команды {@code update}.
 */
public class Update extends Command {
    /**
     * Создание экземпляра команды {@code update}.
     * @param manager {@link tools.CollectionManager}, в котором будет исполнена команда.
     */
    public Update(CollectionManager manager){super(manager);}
    public Update(){}
    /**
     * Запуск соответствующего метода в {@link tools.CollectionManager}.
     */
    public void execute(){
        try {
            validate();
            getManager().update(Long.parseLong((String)getArgs()[0].getValue()),(Dragon) getArgs()[1].getValue());
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
        try{
            Long.parseLong((String)getArgs()[0].getValue());
        } catch (Exception e) {
            throw new InvalidInputException("Неверный формат");
        }
        if(getArgs().length<2 || !(getArgs()[1].getValue() instanceof Dragon)) throw new InvalidInputException("Неверный формат");
    }
}