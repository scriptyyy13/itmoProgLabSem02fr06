package network;

import commands.CommandRequest;
import tools.Message;
import utils.ConfigManager;

import java.io.*;
import java.net.*;

public class UDPClient {
    private final DatagramSocket socket;
    private final InetAddress serverAddress;
    private final int serverPort;
    private final int TIMEOUT = ConfigManager.serverResponseTimeout; // таймаут в 5 секунд

    public UDPClient(String host, int port) throws SocketException, UnknownHostException {
        this.socket = new DatagramSocket();
        this.socket.setSoTimeout(TIMEOUT);
        this.serverAddress = InetAddress.getByName(host);
        this.serverPort = port;
    }

    /** Отправка команды в виде байтового объекта */
    public void sendCommand(CommandRequest command) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(command); // Отправляем саму команду со всеми аргументами
        oos.flush();
        byte[] data = baos.toByteArray();

        DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);
        socket.send(packet);
    }

    /** Получение сообщения от сервера */
    public Message receiveResponse() throws IOException, ClassNotFoundException {
        byte[] buffer = new byte[ConfigManager.maxPacketSize]; // максимальный размер пакета
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);

        ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (Message) ois.readObject();
    }

    public void close() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}