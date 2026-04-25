package commands;

import CollectionManager;

/**
 * Класс, отвечающий за сохрание экземпляра команды {@code print_unique_weight}.
 */
public class PrintUniqueWeight extends Command {
    /**
     * Создание экземпляра команды {@code print_unique_weight}.
     *
     * @param manager {@link CollectionManager}, в котором будет исполнена команда.
     */
    public PrintUniqueWeight(CollectionManager manager) {
        super(manager);
    }

    public PrintUniqueWeight() {
    }

    /**
     * Запуск соответствующего метода в {@link CollectionManager}.
     */
    public void execute() {
        getManager().printUniqueWeight();
    }
}