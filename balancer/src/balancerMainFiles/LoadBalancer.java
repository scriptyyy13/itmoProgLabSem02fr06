package balancerMainFiles;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import commands.AddServerRequest;
import commands.BalancerStatusRequest;
import commands.RemoveServerRequest;
import sharedTools.Serializer;
import sharedTools.Deserializer;
import sharedTools.JwtTokenManager;
import sharedTools.TokenPayload;
import sharedTools.Message;
import commands.CommandRequest;
import exceptions.TokenException;

/**
 * Основной класс балансировщика нагрузки.
 */
public class LoadBalancer {
    /**
     * Список активных серверов.
     */
    private final List<InetSocketAddress> servers;
    /**
     * Счетчик запросов для каждого сервера.
     */
    private final Map<SocketAddress, Integer> requestCounter = new ConcurrentHashMap<>();
    /**
     * Максимальный размер сетевого пакета.
     */
    private final int packetSize;
    /**
     * Менеджер для валидации JWT токенов.
     */
    private final JwtTokenManager tokenManager;

    public LoadBalancer(List<InetSocketAddress> servers, int packetSize, String secretKey) {
        this.servers = new CopyOnWriteArrayList<>(servers);
        this.packetSize = packetSize;
        this.tokenManager = new JwtTokenManager(secretKey);

        for (InetSocketAddress addr : this.servers) {
            requestCounter.put(addr, 0);
        }
    }

    /**
     * Проверяет, является ли команда административной, и обрабатывает ее.
     *
     * @param clientData сырые байты запроса от клиента.
     * @return байты ответа балансировщика или null, если команду нужно переслать серверу.
     */
    public byte[] handlePacketIfAdminCommand(byte[] clientData) {
        try {
            Object obj = Deserializer.deserializeFromBytes(clientData);
            if (obj instanceof CommandRequest) {
                CommandRequest request = (CommandRequest) obj;
                String commandName = "";
                if (obj instanceof BalancerStatusRequest) commandName = "balancer_status";
                if (obj instanceof AddServerRequest) commandName = "add_server";
                if (obj instanceof RemoveServerRequest) commandName = "remove_server";

                if ("balancer_status".equalsIgnoreCase(commandName) || "add_server".equalsIgnoreCase(commandName) || "remove_server".equalsIgnoreCase(commandName)) {
                    return processAdminCommand(request, commandName);
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Обрабатывает административные команды и возвращает локализованный статус-код.
     *
     * @param request     объект запроса команды.
     * @param commandName имя административной команды.
     * @return сериализованный объект Message с результатом.
     */
    private byte[] processAdminCommand(CommandRequest request, String commandName) {
        String token = request.getUserToken();

        if (token == null || token.isEmpty()) {
            return Serializer.serializeToBytes(new Message("401:error.token.missing"));
        }

        try {
            // Валидация токена криптографически в памяти балансера
            TokenPayload payload = tokenManager.validateAndParse(token);

            // Проверка роли ADMIN
            if (!"admin".equalsIgnoreCase(payload.getRole())) {
                return Serializer.serializeToBytes(new Message("403:error.role.access_denied"));
            }

            if ("balancer_status".equalsIgnoreCase(commandName)) {
                StringBuilder sb = new StringBuilder("200:");
                for (InetSocketAddress addr : servers) {
                    boolean alive = isAlive(addr);
                    sb.append(String.format("%s;%s;%d\n", addr, alive ? "ONLINE" : "OFFLINE", requestCounter.getOrDefault(addr, 0)));
                }
                return Serializer.serializeToBytes(new Message(sb.toString().trim()));

            } else if ("add_server".equalsIgnoreCase(commandName) || "remove_server".equalsIgnoreCase(commandName)) {
                // Извлекаем строку "айпи:порт" из аргументов
                if (request.getArgs() == null || request.getArgs().length == 0) {
                    return Serializer.serializeToBytes(new Message("400:error.balancer.missing_address"));
                }

                String rawAddress = (String) request.getArgs()[0].getValue();
                InetSocketAddress targetAddr = parseAddress(rawAddress);

                if ("add_server".equalsIgnoreCase(commandName)) {
                    if (!servers.contains(targetAddr)) {
                        servers.add(targetAddr);
                        requestCounter.put(targetAddr, 0);
                        return Serializer.serializeToBytes(new Message("200:success.balancer.server_added"));
                    }
                    return Serializer.serializeToBytes(new Message("400:error.balancer.server_exists"));
                } else {
                    if (servers.size() > 1) {
                        if (servers.remove(targetAddr)) {
                            requestCounter.remove(targetAddr);
                            return Serializer.serializeToBytes(new Message("200:success.balancer.server_removed"));
                        }
                        return Serializer.serializeToBytes(new Message("404:error.balancer.server_not_found"));
                    } else {
                        return Serializer.serializeToBytes(new Message("400:error.balancer.remove_last_server"));
                    }
                }
            }
        } catch (TokenException e) {
            return Serializer.serializeToBytes(new Message("401:error.token.invalid"));
        }
        return Serializer.serializeToBytes(new Message("500:error.internal.balancer_error"));
    }

    /**
     * Парсит строковый адрес в объект InetSocketAddress.
     *
     * @param rawAddress строка адреса вида ip:port.
     * @return сформированный объект InetSocketAddress.
     */
    private InetSocketAddress parseAddress(String rawAddress) {
        String[] parts = rawAddress.trim().split(":");
        String host = parts[0];
        int port = 443; // Порт по умолчанию

        if (parts.length > 1) {
            try {
                port = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                // Если порт кривой, останется 443
            }
        }
        return new InetSocketAddress(host, port);
    }

    /**
     * Выбирает наименее загруженный живой сервер из пула по алгоритму Least Connections.
     *
     * @return адрес оптимального сервера или null, если живых серверов нет.
     */
    public InetSocketAddress getBestServer() {
        return servers.stream().filter(this::isAlive).min(Comparator.comparingInt(s -> requestCounter.getOrDefault(s, 0))).orElse(null);
    }

    /**
     * Проверяет доступность конкретного сервера отправкой датаграммы PING.
     *
     * @param addr адрес проверяемого сервера.
     * @return true, если сервер ответил, иначе false.
     */
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

    /**
     * Пересылает запрос на выбранный сервер и ожидает ответа.
     *
     * @param data   байты запроса клиента.
     * @param server адрес целевого сервера.
     * @return байты ответа сервера или null в случае таймаута.
     */
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