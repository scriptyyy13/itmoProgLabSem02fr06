import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.*;

public class Balancer {
    public static void main(String[] args) {
        Properties props = new Properties();
        String fileName = (args.length > 0) ? args[0] : "balancer.preps";
        File configFile = new File(fileName);
        try (InputStream is = new FileInputStream(configFile)) {
            props.load(is);

            int port = Integer.parseInt(props.getProperty("balancer.port"));
            int packetSize = Integer.parseInt(props.getProperty("packet.size"));
            String[] serverList = props.getProperty("servers").split(",");

            List<InetSocketAddress> servers = new ArrayList<>();
            for (String s : serverList) {
                String[] parts = s.trim().split(":");
                servers.add(new InetSocketAddress(parts[0], Integer.parseInt(parts[1])));
            }

            LoadBalancer lb = new LoadBalancer(servers, packetSize);

            // прием клиента
            DatagramChannel channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(port));

            ByteBuffer buffer = ByteBuffer.allocate(packetSize);
            System.out.println("Балансер запущен на " + port);

            while (true) {
                buffer.clear();
                SocketAddress clientAddr = channel.receive(buffer);

                if (clientAddr != null) {
                    buffer.flip();
                    byte[] clientData = new byte[buffer.remaining()];
                    buffer.get(clientData);

                    // выбор сервера
                    InetSocketAddress target = lb.getBestServer();

                    if (target != null) {
                        // пересылка и получение
                        byte[] response = lb.forwardRequest(clientData, target);

                        // возвращаем ответ клиенту
                        if (response != null) {
                            channel.send(ByteBuffer.wrap(response), clientAddr);
                        }
                    }
                } else {
                    Thread.sleep(10); // чтобы в холостую не работал цикл
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка конфига: " + configFile.getAbsolutePath());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}