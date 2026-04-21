package commands;


import exceptions.InvalidInputException;
import tools.CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code remove_by_id}.
 */
public class RemoveById extends Command {
    /**
     * Создание экземпляра команды {@code remove_by_id}.
     * @param manager {@link tools.CollectionManager}, в котором будет исполнена команда.
     */
    public RemoveById(CollectionManager manager) {
        super(manager);
    }

    public RemoveById() {}

    /**
     * Запуск соответствующего метода в {@link tools.CollectionManager}.
     */
    public void execute() {
        try {
            validate();
            getManager().removeById(Long.parseLong((String)getArgs()[0].getValue()));
        } catch (InvalidInputException e){
            System.out.println(e.getMessage());
        }
    }
    /**
     * Валидация аргументов команды.
     *
     * @throws InvalidInputException исключение, выбрасываемое в случае неуспешной валидации.
     */
    public void validate() throws InvalidInputException {
        try {
            Long.parseLong((String)getArgs()[0].getValue());
        } catch (Exception e) {
            throw new InvalidInputException("Неверный формат");
        }
    }
}