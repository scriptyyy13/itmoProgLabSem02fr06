package tools;


import commands.*;
import mainFiles.ApplicationContext;
import models.Dragon;
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
import java.util.ArrayDeque;
import java.util.Scanner;
import java.util.Set;

public class ServerCommandManager {
    private DatagramChannel channel;
    private InetSocketAddress inetSocketAddress;
    private Selector selector;
    private CollectionManager collectionManager;
    private CollectionSync synchronizer;

    public ServerCommandManager(int port, CollectionManager collection) {
        this.synchronizer = new CollectionSync(ApplicationContext.collectionPath);
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
    private void checkSync() {
        ArrayDeque<Dragon> updated = synchronizer.syncBeforeRead(collectionManager.getCollection());
        if (updated != collectionManager.getCollection()) {
            collectionManager.setCollection(updated);
            collectionManager.validate();
        }
    }

    private void saveSync() {
        synchronizer.syncAfterWrite(collectionManager.getCollection());
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

                //collectionManager.setCollection( XMLReader.readXmlCollection(ConfigManager.collectionFile));
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
                                buffer.clear();
                                SocketAddress client = new RequestGetter(dc).getRequest(buffer);

                                Object received = Deserializer.deserializeFromBytes(buffer.array());

                                // отвечаем на сообщение пинг для проверки работоспособности сервера
                                if (received instanceof Message && "PING".equals(((Message) received).getText())) {
                                    Message pong = new Message("PONG");
                                    new RequestMaker(dc).makeRequest(pong, client, buffer);
                                } else if (received instanceof CommandRequest cmd) {
                                    // выполнение обычных команд
                                    checkSync();
                                    Message ans = new Message(toCollectionCommand(cmd).execute());
                                    new RequestMaker(dc).makeRequest(ans, client, buffer);
                                    saveSync();
                                    //XMLWriter.dequeToXML(collectionManager.getCollection(),ConfigManager.collectionFile );
                                }
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
                XMLWriter.dequeToXML(collectionManager.getCollection(), ConfigManager.collectionFile);
                System.exit(0);
                break;
            case "save":
                XMLWriter.dequeToXML(collectionManager.getCollection(), ConfigManager.collectionFile);
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

    public Command toCollectionCommand(CommandRequest cmd) {
        if (cmd instanceof AddRequest) return new Add(cmd, collectionManager);
        if (cmd instanceof AddIfMinRequest) return new AddIfMin(cmd, collectionManager);
        if (cmd instanceof AddIfMaxRequest) return new AddIfMax(cmd, collectionManager);
        if (cmd instanceof AverageOfAgeRequest) return new AverageOfAge(cmd, collectionManager);
        if (cmd instanceof ClearRequest) return new Clear(cmd, collectionManager);
        if (cmd instanceof FilterLessThanAgeRequest) return new FilterLessThanAge(cmd, collectionManager);
        if (cmd instanceof HelpRequest) return new Help(cmd, collectionManager);
        if (cmd instanceof InfoRequest) return new Info(cmd, collectionManager);
        if (cmd instanceof PrintUniqueWeightRequest) return new PrintUniqueWeight(cmd, collectionManager);
        if (cmd instanceof RemoveByIdRequest) return new RemoveById(cmd, collectionManager);
        if (cmd instanceof RemoveHeadRequest) return new RemoveHead(cmd, collectionManager);
        if (cmd instanceof ShowRequest) return new Show(cmd, collectionManager);
        if (cmd instanceof UpdateRequest) return new Update(cmd, collectionManager);
        return null;
    }
}
