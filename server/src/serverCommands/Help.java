package serverCommands;

import commands.CommandRequest;
import database.DatabaseManager;
import serverTools.CollectionManager;

import java.util.Locale;

/**
 * Команда для вывода справки по доступным командам.
 */
public class Help extends Command {
    private CollectionManager collectionManager;

    public Help(CommandRequest cmd, CollectionManager collection) {
        super(cmd, collection);
    }

    /**
     * Исполнение команды в коллекции.
     *
     * @return строка с ключами локализации для клиента.
     */
    public String execute() {
        // Базовые ключи команд, доступные всем пользователям
        String helpText = "help.cmd.help;" +
                "help.cmd.info;" +
                "help.cmd.show;" +
                "help.cmd.add;" +
                "help.cmd.update;" +
                "help.cmd.remove_by_id;" +
                "help.cmd.clear;" +
                "help.cmd.execute_script;" +
                "help.cmd.exit;" +
                "help.cmd.remove_head;" +
                "help.cmd.add_if_max;" +
                "help.cmd.add_if_min;" +
                "help.cmd.average_of_age;" +
                "help.cmd.filter_less_than_age;" +
                "help.cmd.print_unique_weight";
        if (DatabaseManager.getInstance().getUserRoleById(this.getExecutorId()).equalsIgnoreCase("admin")) {
            helpText += ";help.cmd.balancer_status;" +
                    "help.cmd.add_server;" +
                    "help.cmd.remove_server";
        }
        return helpText;
    }
}