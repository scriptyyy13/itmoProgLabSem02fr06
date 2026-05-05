import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import tools.Serializer;
import tools.Message;

public class LoadBalancer {
    private final List<InetSocketAddress> servers;
    private final Map<SocketAddress, Integer> requestCounter = new ConcurrentHashMap<>();
    private final int packetSize;

    public LoadBalancer(List<InetSocketAddress> servers, int packetSize) {
        this.servers = servers;
        this.packetSize = packetSize;
        for (InetSocketAddress addr : servers) {
            requestCounter.put(addr, 0);
        }
    }

    /** Выбор сервера и проверка доступности */
    public InetSocketAddress getBestServer() {
        return servers.stream()
                .filter(this::isAlive)
                .min(Comparator.comparingInt(s -> requestCounter.getOrDefault(s, 0)))
                .orElse(null);
    }

    /** Проверка доступности */
    private boolean isAlive(InetSocketAddress addr) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(300);
            byte[] ping = Serializer.serializeToBytes(new Message("PING"));
            socket.send(new DatagramPacket(ping, ping.length, addr));

            byte[] buf = new byte[1024];
            socket.receive(new DatagramPacket(buf, buf.length));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Пересылка и получение */
    public byte[] forwardRequest(byte[] data, InetSocketAddress server) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(2000);
            socket.send(new DatagramPacket(data, data.length, server));

            byte[] resBuf = new byte[packetSize];
            DatagramPacket packet = new DatagramPacket(resBuf, resBuf.length);
            socket.receive(packet);

            requestCounter.put(server, requestCounter.get(server) + 1);
            return Arrays.copyOf(packet.getData(), packet.getLength());
        } catch (IOException e) {
            return null;
        }
    }
}