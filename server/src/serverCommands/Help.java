package serverCommands;

import commands.CommandRequest;
import commands.HelpRequest;
import tools.CollectionManager;

public class Help extends Command {
    private CollectionManager collectionManager;

    public Help(CommandRequest cmd, CollectionManager collection) {
        super(cmd,collection);
    }

    public String execute(){
        return """
                Доступные команды:
                
                help - вывести справку по доступным командам
                info - вывести информацию о коллекции
                show - вывести все элементы коллекции
                add - добавить новый элемент в коллекцию
                update id - обновить значение элемента, id которого равен заданному
                remove_by_id id - удалить элемент по id
                clear - очистить коллекцию
                save - сохранить коллекцию в файл
                execute_script file_name - считать и исполнить скрипт из указанного файла
                exit - завершить программу
                remove_head - вывести первый элемент коллекции и удалить его
                add_if_max - добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции
                add_if_min - добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции
                average_of_age - вывести среднее значение поля age для всех элементов коллекции
                filter_less_than_age age - вывести элементы, значение поля age которых меньше заданного
                print_unique_weight - вывести уникальные значения поля weight всех элементов в коллекции
                """;
    }
}
