package serverCommands;

import commands.CommandRequest;
import database.DatabaseManager;
import exceptions.TokenException;
import serverTools.ConfigManager;
import sharedTools.Arg;
import serverTools.CollectionManager;
import sharedTools.JwtTokenManager;

/**
 * Команда для авторизации пользователя.
 */
public class Login extends Command {
    private JwtTokenManager tokenManager = new JwtTokenManager(ConfigManager.tokenSecretKey);

    public Login(CommandRequest commandRequest, CollectionManager collectionManager) {
        super(commandRequest, collectionManager);
        this.requiresAuth = false;
    }

    /**
     * Исполнение команды авторизации.
     */
    @Override
    public String execute() throws TokenException {
        Arg[] args = getArgs();
        if (args == null || args.length < 2) {
            return "400:error.login.missing_args";
        }

        String login = (String) args[0].getValue();
        String password = (String) args[1].getValue();

        DatabaseManager dbManager = DatabaseManager.getInstance();
        long userId = dbManager.validateUser(login, password);
        String role = dbManager.getUserRoleById(userId);
        if (userId != -1) {
            return "200:" + tokenManager.createToken(userId, login, role, 1800000);
        } else {
            return "400:error.login.invalid_credentials";
        }
    }
}