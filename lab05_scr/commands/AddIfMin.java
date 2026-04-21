package commands;

import exceptions.InvalidInputException;
import models.Dragon;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code add_if_min}.
 */
public class AddIfMin extends Command {
    /**
     * Создание экземпляра команды {@code add_if_min}.
     * @param manager {@link tools.CollectionManager}, в котором будет исполнена команда.
     */
    public AddIfMin(CollectionManager manager){super(manager);}
    public AddIfMin(){}
    /**
     * Запуск соответствующего метода в {@link tools.CollectionManager}.
     */
    public void execute(){
        try {
            validate();
            getManager().addIfMin((Dragon) getArgs()[0].getValue());
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Валидация аргументов команды.
     *
     * @throws InvalidInputException исключение, выбрасываемое в случае неуспешной валидации.
     */
    public void validate() throws InvalidInputException {
        if(! (getArgs()[0].getValue() instanceof Dragon)) throw new InvalidInputException("Неверный формат");
    }

}