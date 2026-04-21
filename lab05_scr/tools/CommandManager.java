package tools;

import commands.*;
import exceptions.InvalidInputException;
import main_classes.ApplicationContext;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

/**
 * Класс, управляющий вводом команд и их исполнением.
 */
public class CommandManager {
    /**
     * {@code Reader}, откуда будут читаться вводные данные.
     */
    private Reader reader;
    /**
     * Управляемая коллекция.
     */
    private CollectionManager collectionManager;
    /**
     * Словарь, хранящий для каждого строкового имени команды соотвествующий экземпляр.
     */
    private HashMap<String, Command> commands;
    /**
     *  Менеджер, управляющий журналом команд.
     */
    private JournalManager journalManager;
    /**
     * Флаг, имеет ли экземпляр журнал.
     */
    private Boolean isHavingJournal;

    /**
     * Создание экземепляра {@code CommandManager}.
     */
    public CommandManager(CollectionManager collectionManager,Reader reader,Boolean isHavingJournal){
        this.reader = reader;
        this.collectionManager = collectionManager;
        commands = new HashMap<String,Command>();
        this.isHavingJournal = isHavingJournal;
        journalManager = new JournalManager();
        commands.put("help",new Help(collectionManager));
        commands.put("info",new Info(collectionManager));
        commands.put("show",new Show(collectionManager));
        commands.put("add",new Add(collectionManager));//
        commands.put("update",new Update(collectionManager));//
        commands.put("remove_by_id",new RemoveById(collectionManager));//
        commands.put("clear",new Clear(collectionManager));//
        commands.put("save",new Save(collectionManager));
        commands.put("execute_script",new ExecuteScript(collectionManager));//
        commands.put("exit",new Exit(collectionManager));
        commands.put("remove_head",new RemoveHead(collectionManager));//
        commands.put("add_if_max",new AddIfMax(collectionManager));//
        commands.put("add_if_min",new AddIfMin(collectionManager));//
        commands.put("average_of_age",new AverageOfAge(collectionManager));
        commands.put("filter_less_than_age",new FilterLessThanAge(collectionManager));
        commands.put("print_unique_speaking",new PrintUniqueWeight(collectionManager));

    }

    /**
     * Начинает обработку ввода комманд.
     */
    public void startManage(){
        //здесь проверка на наличие журнала
        if(isHavingJournal) checkOldJournal();
        while (true){
            collectionManager.validate();
            try {
                var str = reader.getLine();
                if(str==null) break;
                String[] splittedStr = str.split(" ");
                if(!commands.containsKey(splittedStr[0])) throw new InvalidInputException("Команда не найдена. Введите help для помощи.");
                Command command = commands.get(splittedStr[0]);
                switch (splittedStr[0]){
                    case "add","add_if_max","add_if_min":
                        command.setArgs(new Arg(InputManager.inputDragon(reader)));
                        break;
                    case "update":
                        command.setArgs(new Arg(splittedStr[1]), new Arg(InputManager.inputDragon(reader)));
                        break;
                    default:
                        command.setArgs(Arg.toArgList(Arrays.copyOfRange(splittedStr,1,splittedStr.length)));
                        break;
                }
                command.execute();
                if(isHavingJournal) updateJournal(command);
            }catch (InvalidInputException e){
                System.out.println(e.getMessage());
            }catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Неверный формат");
            }

        }
    }

    /**
     * Обновляет журнал после исполнения команды.
     * @param command исполненная команда.
     */
    public void updateJournal(Command command){
        Class<?>[] pushableCommands = {Add.class,AddIfMin.class, AddIfMax.class, Clear.class, ExecuteScript.class,RemoveById.class,RemoveHead.class, Update.class};
        for(var cls : pushableCommands){
            if(cls.isInstance(command)) journalManager.addCommand(command);
        }
        if(command instanceof Save) journalManager.clearJournal();
        journalManager.saveJournal();
    }

    /**
     * Проверяет, существует ли не пустой журнал. При необходимости выполняет все комманды из него.
     */
    public void checkOldJournal(){
        journalManager.readJournal();
        CommandList oldJournal = journalManager.getJournal();
        if(!oldJournal.getCommands().isEmpty()){
            System.out.println("Найдены несохраненные изменения. Восстановить? (true/false)");
            if(InputManager.inputBool(reader,false) ){
                for(Command command : oldJournal.getCommands()) {
                    command.setManager(collectionManager);
                    command.execute();
                }
            }
        }
    }

}
