package clientCommands;

import commands.*;
import exceptions.InvalidInputException;
import network.UDPClient;
import sharedTools.Arg;
import sharedTools.Message;
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
        boolean isAuthorized = false;

        // Автоматическая авторизация из конфига
        if (!ConfigManager.login.isEmpty() && !ConfigManager.password.isEmpty()) {
            OutputManager.println("Обнаружены учетные данные в конфигурации. Попытка автоматического входа...");
            try {
                CommandRequest authRequest = parser.parseCommand("login", consoleReader);
                if (authRequest != null) {
                    Arg[] args = Arg.toArgList(new String[]{ConfigManager.login, ConfigManager.password});
                    authRequest.setArgs(args);

                    if (sendAndReceiveAuth(authRequest)) {
                        OutputManager.println("Автоматический вход выполнен успешно!");
                        isAuthorized = true;
                    } else {
                        OutputManager.println("Ошибка: Учетные данные из конфигурации неверны.");
                    }
                }
            } catch (Exception e) {
                OutputManager.println("Не удалось выполнить автоматический вход: " + e.getMessage());
            }
        }

        // Интерактивная авторизация, если автоматическая не сработала
        while (!isAuthorized) {
            try {
                OutputManager.println("Для работы необходимо авторизоваться.");
                OutputManager.println("Доступные команды: login, register, exit");
                OutputManager.print("auth> ");

                String str = consoleReader.getLine();
                if (str == null) return;
                str = str.trim();
                if (str.isEmpty()) continue;

                if (str.equals("exit")) {
                    OutputManager.println("Завершение работы клиента...");
                    return;
                }

                if (str.equals("login") || str.equals("register")) {
                    OutputManager.print("Введите логин: ");
                    String inputLogin = consoleReader.getLine();
                    if (inputLogin == null) return;
                    inputLogin = inputLogin.trim();

                    OutputManager.print("Введите пароль: ");
                    String inputPassword = consoleReader.getLine();
                    if (inputPassword == null) return;
                    inputPassword = inputPassword.trim();

                    if (inputLogin.isEmpty() || inputPassword.isEmpty()) {
                        OutputManager.println("Ошибка: Логин и пароль не могут быть пустыми!");
                        continue;
                    }

                    CommandRequest authRequest = parser.parseCommand(str, consoleReader);
                    if (authRequest == null) continue;

                    Arg[] args = Arg.toArgList(new String[]{ConfigManager.login, ConfigManager.password});
                    authRequest.setArgs(args);

                    if (sendAndReceiveAuth(authRequest)) {
                        ConfigManager.login = inputLogin;
                        ConfigManager.password = inputPassword;
                        OutputManager.println("Авторизация успешно пройдена!");
                        isAuthorized = true;
                    }
                } else {
                    OutputManager.println("Ошибка: Пожалуйста, сначала войдите в аккаунт (команда 'login') или зарегистрируйтесь ('register').");
                }
            } catch (Exception e) {
                OutputManager.println("Ошибка при авторизации: " + e.getMessage());
            }
        }

        OutputManager.println("Вы вошли как: " + ConfigManager.login);
        OutputManager.println("Введите команду (или 'help' для списка доступных команд):");
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
     *
     * @param command
     */
    private void sendAndReceive(CommandRequest command) {
        try {
            command.setLogin(ConfigManager.login);
            command.setUserPassword(ConfigManager.password);
            udpClient.sendCommand(command);
            sharedTools.Message response = udpClient.receiveResponse();
            OutputManager.println(response.getText());
        } catch (java.net.SocketTimeoutException e) {
            OutputManager.errPrintln("Сервер недоступен.");
        } catch (Exception e) {
            OutputManager.errPrintln("Ошибка связи: " + e.getMessage());
        }
    }

    /**
     * Специальный метод отправки запросов аутентификации.
     * Возвращает true, если сервер успешно обработал команду, и false, если вернул ошибку валидации.
     */
    private boolean sendAndReceiveAuth(CommandRequest command) {
        try {
            udpClient.sendCommand(command);
            Message response = udpClient.receiveResponse();
            OutputManager.println(response.getText());

            // Если сервер вернул ошибку валидации, значит логин/пароль не подошли
            return !response.getText().contains("Ошибка валидации пользователя");
        } catch (java.net.SocketTimeoutException e) {
            OutputManager.errPrintln("Сервер недоступен.");
            return false;
        } catch (Exception e) {
            OutputManager.errPrintln("Ошибка связи: " + e.getMessage());
            return false;
        }
    }
}