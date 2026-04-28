import commands.Command;
import models.Dragon;
import tools.Deserializer;
import tools.Message;
import tools.Serializer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayDeque;

public class UDPServer {
    private DatagramChannel channel;
    private InetSocketAddress inetSocketAddress;
    private Selector selector;
    private CommandExecutor commandExecutor;

    public UDPServer(int port, CollectionManager collection){
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

    public void start(){
        ByteBuffer buffer = ByteBuffer.allocate(65535);
        while(true){
            try{
                selector.select();
                for(SelectionKey key : selector.selectedKeys()){
                    selector.selectedKeys().remove(key);
                    if(key.isReadable()) {
                        DatagramChannel dc = (DatagramChannel) key.channel();
                        SocketAddress client = dc.receive(buffer);
                        buffer.flip();
                        Command cmd = (Command) Deserializer.deserializeFromBytes(buffer.array());
                        Message ans = commandExecutor.executeCommand(cmd);
                        buffer.clear();
                        buffer.put(Serializer.serializeToBytes(ans));
                        buffer.flip();
                        dc.send(buffer, client);
                    }
                }
            }catch (IOException e){
                continue;
            }

        }
    }

}
