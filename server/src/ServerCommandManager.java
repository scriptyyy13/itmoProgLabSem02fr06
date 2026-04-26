import commands.Add;
import commands.Command;
import tools.Message;

public class ServerCommandManager {
    private CommandExecutor commandExecutor;

    public ServerCommandManager(CollectionManager collectionManager){
        this.commandExecutor = new CommandExecutor(collectionManager);
    }
    public void startManage(){
        while(true){
            //типа получили с клиента
            Command command =new Add();
            Message ans = commandExecutor.executeCommand(command);
            // этот анс мы сериализуем и отправляем нахцуууууу на клиент
        }
    }
}
