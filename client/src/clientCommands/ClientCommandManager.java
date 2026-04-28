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

                Command command = parser.parseCommand(str, consoleReader);
                if (command == null) continue;

                if (command instanceof Exit) {
                    OutputManager.println("Завершение работы клиента...");
                    break;
                }

                if (command instanceof ExecuteScript) {
                    String[] parts = str.trim().split("\\s+");
                    if (parts.length > 1) {
                        List<Command> scriptCommands = scriptManager.processScript(parts[1]);
                        if (scriptCommands != null) {
                            for (Command cmd : scriptCommands) {
                                sendAndReceive(cmd);
                            }
                        }
                    }
                } else {
                    sendAndReceive(command);
                }
            } catch (InvalidInputException e) {
                OutputManager.println(e.getMessage());
            } catch (Exception e) {
                OutputManager.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private void handleScript(String inputLine) {
        String[] splitted = inputLine.trim().split("\\s+");

        if (splitted.length < 2) {
            OutputManager.errPrintln("Ошибка: не указан путь к файлу скрипта.");
            return;
        }
        String filePath = splitted[1];

        List<Command> commands = scriptManager.processScript(filePath);

        if (commands != null) {
            OutputManager.println("Выполнение скрипта: " + filePath);
            for (Command cmd : commands) {
                if (!sendAndReceive(cmd)) {
                    OutputManager.errPrintln("Скрипт прерван из-за ошибки сети.");
                    break;
                }
            }
        } else {
            OutputManager.errPrintln("Выполнение скрипта отменено.");
        }
    }

    private boolean sendAndReceive(Command command) {
        try {
            udpClient.sendCommand(command);
            tools.Message response = udpClient.receiveResponse();
            OutputManager.println(response.getText());
            return true;
        } catch (java.net.SocketTimeoutException e) {
            OutputManager.errPrintln("Сервер недоступен.");
            return false;
        } catch (Exception e) {
            OutputManager.errPrintln("Ошибка связи: " + e.getMessage());
            return false;
        }
    }
}