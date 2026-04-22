package tools;

import commands.*;
import exceptions.InvalidInputException;
import main_classes.ApplicationContext;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
     * Создание экземепляра {@code CommandManager}.
     */
    public CommandManager(CollectionManager collectionManager, Reader reader) {
        this.reader = reader;
        this.collectionManager = collectionManager;
        commands = new HashMap<String, Command>();
        commands.put("help", new Help(collectionManager));
        commands.put("info", new Info(collectionManager));
        commands.put("show", new Show(collectionManager));
        commands.put("add", new Add(collectionManager));//
        commands.put("update", new Update(collectionManager));//
        commands.put("remove_by_id", new RemoveById(collectionManager));//
        commands.put("clear", new Clear(collectionManager));//
        commands.put("save", new Save(collectionManager));
        commands.put("execute_script", new ExecuteScript(collectionManager));//
        commands.put("exit", new Exit(collectionManager));
        commands.put("remove_head", new RemoveHead(collectionManager));//
        commands.put("add_if_max", new AddIfMax(collectionManager));//
        commands.put("add_if_min", new AddIfMin(collectionManager));//
        commands.put("average_of_age", new AverageOfAge(collectionManager));
        commands.put("filter_less_than_age", new FilterLessThanAge(collectionManager));
        commands.put("print_unique_speaking", new PrintUniqueWeight(collectionManager));
    }

    /**
     * Начинает обработку ввода комманд.
     */
    public void startManage() {
        //здесь проверка на наличие журнала
        while (true) {
            collectionManager.validate();
            try {
                var str = reader.getLine();
                if (str == null) break;
                String[] splittedStr = str.split(" ");
                if (!commands.containsKey(splittedStr[0]))
                    throw new InvalidInputException("Команда не найдена. Введите help для помощи.");
                Command command = commands.get(splittedStr[0]);
                switch (splittedStr[0]) {
                    case "add", "add_if_max", "add_if_min":
                        command.setArgs(new Arg(InputManager.inputDragon(reader)));
                        break;
                    case "update":
                        command.setArgs(new Arg(splittedStr[1]), new Arg(InputManager.inputDragon(reader)));
                        break;
                    case "execute_script":
                        try {
                            command.setArgs(new Arg(Files.readString(Paths.get(splittedStr[1]))));
                        }catch (IOException e){
                            throw new InvalidInputException("Файл не найден");
                        }
                        break;
                    default:
                        command.setArgs(Arg.toArgList(Arrays.copyOfRange(splittedStr, 1, splittedStr.length)));
                        break;
                }
                command.execute();
            } catch (InvalidInputException e) {
                OutputManager.println(e.getMessage());
            } catch (ArrayIndexOutOfBoundsException e) {
                OutputManager.println("Неверный формат");
            }
        }
    }


}
