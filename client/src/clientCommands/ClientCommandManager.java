package clientCommands;

import commands.*;
import exceptions.InvalidInputException;
import network.UDPClient;
import utils.*;

import java.util.List;

/**
 * Основная логика клиента.
 */
public class ClientCommandManager {
    /**
     * {@code Reader}, читающий консоль.
     */
    private final Reader consoleReader;
    /**
     * Интернет-часть клиента.
     */
    private final UDPClient udpClient;
    /**
     * Парсер команд.
     */
    private final CommandParser parser;
    /**
     * Менеджер для работы со скриптами.
     */
    private final ScriptManager scriptManager;

    public ClientCommandManager(Reader reader, UDPClient udpClient) {
        this.consoleReader = reader;
        this.udpClient = udpClient;
        this.parser = new CommandParser();
        this.scriptManager = new ScriptManager(this.parser);
    }

    /**
     * Основный цикл работы клиента.
     */
    public void start() {
        while (true) {
            try {
                OutputManager.print("> ");
                String str = consoleReader.getLine();
                if (str == null) break;
                if (str.trim().isEmpty()) continue;

                String[] parts = str.trim().split("\\s+");
                if (parts.length == 0) continue;
                if (parts[0].equals("exit")) {
                    OutputManager.println("Завершение работы клиента...");
                    break;
                } else if (parts[0].equals("execute_script")) {
                    if (parts.length > 1) {
                        List<CommandRequest> scriptCommands = scriptManager.processScript(parts[1]);
                        if (scriptCommands != null) {
                            for (CommandRequest cmd : scriptCommands) {
                                sendAndReceive(cmd);
                            }
                        }
                    }
                } else {
                    CommandRequest command = parser.parseCommand(str, consoleReader);
                    if (command == null) continue;
                    sendAndReceive(command);
                }
            } catch (InvalidInputException e) {
                OutputManager.println(e.getMessage());
            } catch (Exception e) {
                OutputManager.println("Ошибка: " + e.getMessage());
            }
        }
    }

    /**
     * Отправка команды на сервер и получение ответа.
     * @param command
     */
    private void sendAndReceive(CommandRequest command) {
        try {
            udpClient.sendCommand(command);
            sharedTools.Message response = udpClient.receiveResponse();
            OutputManager.println(response.getText());
        } catch (java.net.SocketTimeoutException e) {
            OutputManager.errPrintln("Сервер недоступен.");
        } catch (Exception e) {
            OutputManager.errPrintln("Ошибка связи: " + e.getMessage());
        }
    }
}