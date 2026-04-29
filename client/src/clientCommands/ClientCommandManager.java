package clientCommands;

import commands.*;
import exceptions.InvalidInputException;
import network.UDPClient;
import utils.*;

import java.util.List;

public class ClientCommandManager {
    private final Reader consoleReader;
    private final UDPClient udpClient;
    private final CommandParser parser;
    private final ScriptManager scriptManager;

    public ClientCommandManager(Reader reader, UDPClient udpClient) {
        this.consoleReader = reader;
        this.udpClient = udpClient;
        this.parser = new CommandParser();
        this.scriptManager = new ScriptManager(this.parser);
    }

    public void start() {
        while (true) {
            try {
                String str = consoleReader.getLine();
                if (str == null) break;

                String[] parts = str.trim().split("\\s+");
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

    private void sendAndReceive(CommandRequest command) {
        try {
            udpClient.sendCommand(command);
            tools.Message response = udpClient.receiveResponse();
            OutputManager.println(response.getText());
        } catch (java.net.SocketTimeoutException e) {
            OutputManager.errPrintln("Сервер недоступен.");
        } catch (Exception e) {
            OutputManager.errPrintln("Ошибка связи: " + e.getMessage());
        }
    }
}