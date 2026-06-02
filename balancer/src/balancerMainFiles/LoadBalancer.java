package balancerMainFiles;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList; // Важно для динамического изменения списка

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
 * Основной класс балансера.
 */
public class LoadBalancer {
    /**
     * Список серверов.
     */
    private final List<InetSocketAddress> servers;
    /**
     * Список операций с каждым сервером
     */
    private final Map<SocketAddress, Integer> requestCounter = new ConcurrentHashMap<>();
    /**
     * Размер пакета.
     */
    private final int packetSize;
    /**
     * Менеджер JWT токенов.
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
     * Точка фильтрации запросов. Проверяет, является ли команда административной.
     * Если да - обрабатывает сам, если нет - возвращает null (сигнал пересылать дальше).
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
     * Внутренняя обработка админ-команд
     */
    private byte[] processAdminCommand(CommandRequest request, String commandName) {
        String token = request.getUserToken();

        if (token == null || token.isEmpty()) {
            return Serializer.serializeToBytes(new Message("Ошибка: Токен отсутствует. Доступ запрещен."));
        }

        try {
            // Валидация токена криптографически в памяти балансера
            TokenPayload payload = tokenManager.validateAndParse(token);

            // Проверка роли ADMIN
            if (!"admin".equalsIgnoreCase(payload.getRole())) {
                return Serializer.serializeToBytes(new Message("Ошибка: Недостаточно прав. Требуется роль ADMIN."));
            }

            if ("balancer_status".equalsIgnoreCase(commandName)) {
                StringBuilder sb = new StringBuilder("--- Статус Балансировщика ---\n");
                sb.append("Серверы в пуле:\n");
                for (InetSocketAddress addr : servers) {
                    boolean alive = isAlive(addr);
                    sb.append(String.format(" - %s [%s] (Запросов обработано: %d)\n", addr, alive ? "ONLINE" : "OFFLINE", requestCounter.getOrDefault(addr, 0)));
                }
                return Serializer.serializeToBytes(new Message(sb.toString()));

            } else if ("add_server".equalsIgnoreCase(commandName) || "remove_server".equalsIgnoreCase(commandName)) {
                // Извлекаем строку "айпи:порт" из аргументов
                if (request.getArgs() == null || request.getArgs().length == 0) {
                    return Serializer.serializeToBytes(new Message("Ошибка: Не указан адрес сервера (айпи:порт)."));
                }

                String rawAddress = (String) request.getArgs()[0].getValue();
                InetSocketAddress targetAddr = parseAddress(rawAddress);

                if ("add_server".equalsIgnoreCase(commandName)) {
                    if (!servers.contains(targetAddr)) {
                        servers.add(targetAddr);
                        requestCounter.put(targetAddr, 0);
                        return Serializer.serializeToBytes(new Message("Сервер " + targetAddr + " успешно добавлен в пул балансировщика."));
                    }
                    return Serializer.serializeToBytes(new Message("Сервер " + targetAddr + " уже находится в пуле."));
                } else {
                    if (servers.size() > 1) {
                        if (servers.remove(targetAddr)) {
                            requestCounter.remove(targetAddr);
                            return Serializer.serializeToBytes(new Message("Сервер " + targetAddr + " успешно удален из пула балансировщика."));
                        }
                        return Serializer.serializeToBytes(new Message("Сервер " + targetAddr + " не найден в пуле."));
                    } else {
                        return Serializer.serializeToBytes(new Message("Невозможно удалить единственный сервер."));
                    }
                }
            }

        } catch (TokenException e) {
            return Serializer.serializeToBytes(new Message("Ошибка авторизации: " + e.getMessage()));
        }

        return Serializer.serializeToBytes(new Message("Неизвестная ошибка при обработке админ-команды."));
    }

    /**
     * Вспомогательный парсер строки вида айпи:порт или просто айпи
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
     * Выбор сервера и проверка доступности
     */
    public InetSocketAddress getBestServer() {
        return servers.stream().filter(this::isAlive).min(Comparator.comparingInt(s -> requestCounter.getOrDefault(s, 0))).orElse(null);
    }

    /**
     * Проверка доступности
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
     * Пересылка и получение
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