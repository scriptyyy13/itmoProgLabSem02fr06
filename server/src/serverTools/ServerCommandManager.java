package serverTools;


import commands.*;
import database.DatabaseManager;
import serverMainFiles.ApplicationContext;
import models.Dragon;
import serverCommands.*;

import sharedTools.*;
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
import java.util.concurrent.ConcurrentLinkedDeque;


/**
 * Содержит основную логику сервера.
 */
public class ServerCommandManager {
    /**
     * Канал, по которому происходит обмен данными с клиентом.
     */
    private DatagramChannel channel;
    /**
     * Адресс сервера.
     */
    private InetSocketAddress inetSocketAddress;
    /**
     * Селектор канала.
     */
    private Selector selector;
    /**
     * Коллекция сервера.
     */
    private CollectionManager collectionManager;
    /**
     * Синхронизатор серверов.
     */
    private CollectionSync synchronizer;

    public ServerCommandManager(int port, CollectionManager collection) {
        this.synchronizer = new CollectionSync();
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

    /**
     * Основной цикл работы сервера.
     */
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
                                buffer.clear();
                                SocketAddress client = new RequestGetter(dc).getRequest(buffer);

                                Object received = Deserializer.deserializeFromBytes(buffer.array());

                                // отвечаем на сообщение пинг для проверки работоспособности сервера
                                if (received instanceof Message && "PING".equals(((Message) received).getText())) {
                                    Message pong = new Message("PONG");
                                    new RequestMaker(dc).makeRequest(pong, client, buffer);
                                } else if (received instanceof CommandRequest cmd) {
                                    // выполнение обычных команд
                                    synchronizer.syncBeforeRead(collectionManager);
                                    Thread.sleep(10);
                                    String login = cmd.getLogin();
                                    String password = cmd.getUserPassword();
                                    Long id = DatabaseManager.getInstance().validateUser(login, password);

                                    Command collectionCmd = toCollectionCommand(cmd);
                                    Message ans;
                                    if (id == -1L && collectionCmd.requiresAuth) {
                                        ans = new Message("Ошибка валидации пользователя.");
                                    } else {
                                        collectionCmd.setExecutorId(id);
                                        ans = new Message(collectionCmd.execute());
                                    }
                                    new RequestMaker(dc).makeRequest(ans, client, buffer);
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

    /**
     * Выполнить серверную команду
     * @param cmd Строковое прдеставление команды.
     */
    public void executeServerCommand(String cmd) {
        if (cmd.equals("exit")) {
            System.exit(0);
        } else {
            System.out.println("""
                    Доступные команды:
                    exit - завершить работу приложения с сохранением коллекции
                    """);
        }
    }

    /**
     * Конвертаация реквеста команды в команду.
     * @param cmd Реквест команды.
     * @return команда.
     */
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
        if (cmd instanceof LoginRequest) return new Login(cmd, collectionManager);
        if (cmd instanceof RegisterRequest) return new Register(cmd, collectionManager);
        return null;
    }
}
