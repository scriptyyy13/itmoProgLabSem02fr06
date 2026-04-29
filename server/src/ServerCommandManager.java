import commands.Command;
import tools.Deserializer;
import tools.Message;
import tools.Serializer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Pipe;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.Set;

public class ServerCommandManager {
    private DatagramChannel channel;
    private InetSocketAddress inetSocketAddress;
    private Selector selector;
    private CommandExecutor commandExecutor;

    public ServerCommandManager(int port, CollectionManager collection){
        commandExecutor = new CommandExecutor(collection);
        try {
            inetSocketAddress = new InetSocketAddress(port);
            channel = DatagramChannel.open();
            channel.bind(inetSocketAddress);
            channel.configureBlocking(false);
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);
        }catch (Exception e){
            System.out.println("Не удалось открыть сервер");
        }



    }

    public void start() {
        try {
            Pipe pipe = Pipe.open();
            Pipe.SinkChannel sink = pipe.sink();
            sink.configureBlocking(false);
            sink.register(selector, SelectionKey.OP_WRITE);

            Pipe.SourceChannel source = pipe.source();
            source.configureBlocking(false);
            source.register(selector, SelectionKey.OP_READ);

            Thread consoleThread = new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNextLine()) {
                    String cmd = scanner.nextLine();
                    ByteBuffer buf = StandardCharsets.UTF_8.encode(cmd + "\n");
                    try {
                        while (buf.hasRemaining()) {
                            sink.write(buf);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            consoleThread.setDaemon(true);
            consoleThread.start();

            ByteBuffer buffer = ByteBuffer.allocate(65535);
            ByteBuffer serverCmdBuffer = ByteBuffer.allocate(1024);
            while (true) {
                try {
                    selector.select();
                    Set<SelectionKey> keys = selector.selectedKeys();
                    for (var iter = keys.iterator(); iter.hasNext();) {
                        SelectionKey key = iter.next();
                        iter.remove();
                        if (key.isReadable()) {
                            if(key.channel() == source){
                                serverCmdBuffer.clear();
                                source.read(serverCmdBuffer);
                                serverCmdBuffer.flip();
                                String command = StandardCharsets.UTF_8.decode(serverCmdBuffer).toString().trim();
                                executeServerCommand(command);
                                serverCmdBuffer.clear();
                            }else {
                                DatagramChannel dc = (DatagramChannel) key.channel();
                                SocketAddress client = dc.receive(buffer);
                                buffer.flip();
                                Command cmd = (Command) Deserializer.deserializeFromBytes(buffer.array());
                                Message ans = commandExecutor.executeCommand(cmd);
                                buffer.clear();
                                buffer.put(Serializer.serializeToBytes(ans));
                                buffer.flip();
                                dc.send(buffer, client);
                                buffer.clear();
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Ошибка сервера: " + e.getMessage());
                }

            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void executeServerCommand(String cmd){
        switch (cmd){
            case "exit":
                commandExecutor.getCollection().save();
                System.exit(0);
                break;
            case "save":
                commandExecutor.getCollection().save();
                break;
            default:
                System.out.println("""
                        Доступные команды:
                        save - сохранить коллекцию
                        exit - завершить работу приложения с сохранением коллекции
                        """);
                break;
        }
    }

}
