package tools;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class RequestMaker {
    public DatagramChannel channel;

    public RequestMaker (DatagramChannel dc){
        channel = dc;
    }
    public void makeRequest(Message msg, SocketAddress addr, ByteBuffer buffer){
        try {
            buffer.clear();
            buffer.put(Serializer.serializeToBytes(msg));
            buffer.flip();
            channel.send(buffer, addr);
            buffer.clear();
        }catch(IOException e){
            return;
        }
    }
}
