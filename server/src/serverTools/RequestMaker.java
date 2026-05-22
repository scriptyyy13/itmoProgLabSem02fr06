package serverTools;

import sharedTools.Message;
import sharedTools.Serializer;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * Отвечает за отправку интренет-пакета по каналу.
 */
public class RequestMaker {
    /**
     * Канал, по которому отправляем пакее
     */
    public DatagramChannel channel;

    public RequestMaker(DatagramChannel dc) {
        channel = dc;
    }

    /**
     * Отправка пакета
     * @param msg Отправялемый {@code Message}
     * @param addr Адрес получателя
     * @param buffer Буфер, в который кладется сериализованный {@code Message}
     */
    public void makeRequest(Message msg, SocketAddress addr, ByteBuffer buffer) {
        try {
            buffer.clear();
            buffer.put(Serializer.serializeToBytes(msg));
            buffer.flip();
            channel.send(buffer, addr);
            buffer.clear();
        } catch (IOException e) {
            return;
        }
    }
}
