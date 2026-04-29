package tools;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class RequestGetter {
    private DatagramChannel channel;

    public RequestGetter(DatagramChannel dc){
        channel = dc;
    }
    public SocketAddress getRequest(ByteBuffer buffer){
        try {
            SocketAddress client = channel.receive(buffer);
            buffer.flip();
            return client;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
