package commands;


import exceptions.InvalidInputException;
import tools.CollectionManager;
/**
 * Класс, отвечающий за сохрание экземпляра команды {@code save}.
 */
public class Save extends Command {
    /**
     * Создание экземпляра команды {@code save}.
     * @param manager {@link tools.CollectionManager}, в котором будет исполнена команда.
     */
    public Save(CollectionManager manager){
        super(manager);
    }

    public Save() {}

    /**
     * Запуск соответствующего метода в {@link tools.CollectionManager}.
     */
    public void execute() {
        getManager().save();
    }
}