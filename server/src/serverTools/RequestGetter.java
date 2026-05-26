package serverTools;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * Отвечает за принятин интернет-пакета по каналу.
 */
public class RequestGetter {
    /**
     * Канал, с которого берем пакет.
     */
    private DatagramChannel channel;

    public RequestGetter(DatagramChannel dc) {
        channel = dc;
    }

    /**
     * Прием пакета
     * @param buffer буфер, в который кладется резултатю.
     * @return Адрес отправителя.
     */
    public SocketAddress getRequest(ByteBuffer buffer) {
        try {
            SocketAddress client = channel.receive(buffer);
            buffer.clear();
            return client;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
