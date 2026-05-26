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
import java.util.concurrent.*;


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
    private ExecutorService readingPool;
    private ForkJoinPool workingPool;
    private ExecutorService sendingPool;
    private LinkedBlockingQueue<Request> requestBuffer;
    private LinkedBlockingQueue<ResultOfRequest> resultBuffer;

    public ServerCommandManager(int port, CollectionManager collection) {
        this.synchronizer = new CollectionSync();
        this.collectionManager = collection;
        readingPool = Executors.newFixedThreadPool(4); // TODO: здесь в конфиге добавить колво потоков на чтение
        sendingPool = Executors.newFixedThreadPool(4); // TODO: здесь в конфиге добавить колво потоков на чтение
        workingPool = new ForkJoinPool(4);
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
            for (int i = 0; i < 4; i++) {
                workingPool.execute(this::workLoop);
            }
            for (int i = 0; i < 4; i++) {
                sendingPool.execute(this::sendingLoop);
            }
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
                            DatagramChannel dc = (DatagramChannel) key.channel();
                            var client = new RequestGetter(dc).getRequest(buffer);
                            ByteRequest br = new ByteRequest(buffer.duplicate(), client);
                            readingPool.execute(() -> {
                                readingByteRequest(br);
                            });
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
     * Конвертаация реквеста команды в команду.
     *
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


    public void readingByteRequest(ByteRequest br) {
        try {
            requestBuffer.offer(new Request(br.client(), (CommandRequest) Deserializer.deserializeFromBytes(br.bytes().array())), 500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void workLoop() {
        while (!Thread.interrupted()) {
            try {
                synchronizer.syncBeforeRead(collectionManager);
                Request r = requestBuffer.take();
                CommandRequest cmd = r.command();
                String login = cmd.getLogin();
                String password = cmd.getUserPassword();
                Long id = DatabaseManager.getInstance().validateUser(login, password);

                Command collectionCmd = toCollectionCommand(cmd);
                Message msg;
                if (id == -1L && collectionCmd.requiresAuth) {
                    msg = new Message("Ошибка валидации пользователя.");
                } else {
                    collectionCmd.setExecutorId(id);
                    msg = new Message(collectionCmd.execute());
                }
                resultBuffer.offer(new ResultOfRequest(r.client(), msg), 500, TimeUnit.MILLISECONDS);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void sendingLoop() {
        while (!Thread.interrupted()) {
            try {
                Request r = requestBuffer.take();

                Message msg = new Message();
                resultBuffer.offer(new ResultOfRequest(r.client(), msg), 500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
