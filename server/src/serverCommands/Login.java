package serverCommands;

import commands.CommandRequest;
import database.DatabaseManager;
import sharedTools.Arg;
import serverTools.CollectionManager;

/**
 * Команда для авторизации пользователя.
 */
public class Login extends Command {

    public Login(CommandRequest commandRequest, CollectionManager collectionManager) {
        super(commandRequest, collectionManager);
        this.requiresAuth = false;
    }

    /**
     * Исполнение команды авторизации.
     */
    @Override
    public String execute() {
        Arg[] args = getArgs();
        if (args == null || args.length < 2) {
            return "Ошибка: Неверное количество аргументов для входа.";
        }

        String login = (String) args[0].getValue();
        String password = (String) args[1].getValue();

        DatabaseManager dbManager = DatabaseManager.getInstance();
        long userId = dbManager.validateUser(login, password);
        if (userId != -1) {
            return "SUCCESS_LOGIN:" + userId;
        } else {
            return "Ошибка: Неверный логин или пароль.";
        }
    }
}