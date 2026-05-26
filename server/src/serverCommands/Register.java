package serverCommands;

import commands.CommandRequest;
import database.DatabaseManager;
import sharedTools.Arg;
import serverTools.CollectionManager;

/**
 * Команда для регистрации нового пользователя.
 */
public class Register extends Command {

    public Register(CommandRequest commandRequest, CollectionManager collectionManager) {
        super(commandRequest, collectionManager);
        this.requiresAuth = false;
    }

    /**
     * Исполнение команды регистрации.
     */
    @Override
    public String execute() {
        Arg[] args = getArgs();
        if (args == null || args.length < 2) {
            return "Ошибка: Неверное количество аргументов для регистрации.";
        }

        String login = (String) args[0].getValue();
        String password = (String) args[1].getValue();
        if (login.trim().isEmpty() || password.trim().isEmpty()) {
            return "Ошибка: Логин и пароль не могут быть пустыми.";
        }

        DatabaseManager dbManager = DatabaseManager.getInstance();
        long userId = dbManager.registerUser(login, password);

        if (userId != -1) {
            return "SUCCESS_REGISTER:" + userId;
        } else {
            return "Ошибка: Пользователь с таким логином уже существует.";
        }
    }
}