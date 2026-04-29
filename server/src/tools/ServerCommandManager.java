package tools;


import commands.*;
import serverCommands.*;


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
    private CollectionManager collectionManager;

    public ServerCommandManager(int port, CollectionManager collection) {
        this.collectionManager = collection;
        try {
            inetSocketAddress = new InetSocketAddress(port);
            channel = DatagramChannel.open();
            channel.bind(inetSocketAddress);
            channel.configureBlocking(false);
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);
        } catch (Exception e) {
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

            ByteBuffer buffer = ByteBuffer.allocate(ConfigManager.messageBufferCapacity);
            ByteBuffer serverCmdBuffer = ByteBuffer.allocate(ConfigManager.commandsBufferCapacity);
            while (true) {
                try {
                    selector.select();
                    Set<SelectionKey> keys = selector.selectedKeys();
                    for (var iter = keys.iterator(); iter.hasNext(); ) {
                        SelectionKey key = iter.next();
                        iter.remove();
                        if (key.isReadable()) {
                            if (key.channel() == source) {
                                serverCmdBuffer.clear();
                                source.read(serverCmdBuffer);
                                serverCmdBuffer.flip();
                                String command = StandardCharsets.UTF_8.decode(serverCmdBuffer).toString().trim();
                                executeServerCommand(command);
                                serverCmdBuffer.clear();
                            } else {
                                DatagramChannel dc = (DatagramChannel) key.channel();
                                SocketAddress client = new RequestGetter(dc).getRequest(buffer);
                                Command cmd = (Command) Deserializer.deserializeFromBytes(buffer.array());
                                Message ans = new Message((wrapCommand(cmd)).execute());
                                new RequestMaker(dc).makeRequest(ans, client, buffer);
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Ошибка сервера: " + e.getMessage());
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void executeServerCommand(String cmd) {
        switch (cmd) {
            case "exit":
                XMLWriter xmlWriter = new XMLWriter();
                xmlWriter.dequeToXML(collectionManager.getCollection(), ConfigManager.collectionFile);
                System.exit(0);
                break;
            case "save":
                XMLWriter xmlWriter2 = new XMLWriter();
                xmlWriter2.dequeToXML(collectionManager.getCollection(), ConfigManager.collectionFile);
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

    public Command wrapCommand(Command command) {
        if (command instanceof AddRequest) {
            Add cmd = (Add) command;
            cmd.setCollectionManager(collectionManager);
            return cmd;
        }
        if (command instanceof AddIfMinRequest) {
            AddIfMin cmd = (AddIfMin) command;
            cmd.setCollectionManager(collectionManager);
            return cmd;
        }
        if (command instanceof AddIfMaxRequest) {
            AddIfMax cmd = (AddIfMax) command;
            cmd.setCollectionManager(collectionManager);
            return cmd;
        }
        if (command instanceof AverageOfAgeRequest) {
            AverageOfAge cmd = (AverageOfAge) command;
            cmd.setCollectionManager(collectionManager);
            return cmd;
        }
        if (command instanceof ClearRequest) {
            Clear cmd = (Clear) command;
            cmd.setCollectionManager(collectionManager);
            return cmd;
        }
        if (command instanceof FilterLessThanAgeRequest) {
            FilterLessThanAge cmd = (FilterLessThanAge) command;
            cmd.setCollectionManager(collectionManager);
            return cmd;
        }
        if (command instanceof InfoRequest) {
            Info cmd = (Info) command;
            cmd.setCollectionManager(collectionManager);
            return cmd;
        }
        if (command instanceof PrintUniqueWeightRequest) {
            PrintUniqueWeight cmd = (PrintUniqueWeight) command;
            cmd.setCollectionManager(collectionManager);
            return cmd;
        }
        if (command instanceof RemoveByIdRequest) {
            RemoveById cmd = (RemoveById) command;
            cmd.setCollectionManager(collectionManager);
            return cmd;
        }
        if (command instanceof RemoveHeadRequest) {
            RemoveHead cmd = (RemoveHead) command;
            cmd.setCollectionManager(collectionManager);
            return cmd;
        }
        if (command instanceof ShowRequest) {
            Show cmd = (Show) command;
            cmd.setCollectionManager(collectionManager);
            return cmd;
        }
        if (command instanceof UpdateRequest) {
            Update cmd = (Update) command;
            cmd.setCollectionManager(collectionManager);
            return cmd;
        }
        return null;

    }
}
