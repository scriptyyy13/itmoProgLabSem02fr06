package core;

import commands.CommandRequest;
import network.UDPClient;
import network.Response;
import sharedTools.Message;
import utils.ConfigManager;
import utils.CommandParser;
import utils.Reader;
import utils.ScriptManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Центральный менеджер для работы с командами и сетью.
 * Обрабатывает универсальные запросы от UI и возвращает структурированный Response.
 */
public class ClientCore {

    /**
     * Компонент для сетевого взаимодействия.
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

    /**
     * Конструктор менеджера.
     *
     * @param udpClient сетевой клиент.
     */
    public ClientCore(UDPClient udpClient) {
        this.udpClient = udpClient;
        this.parser = new CommandParser();
        this.scriptManager = new ScriptManager(this.parser);
    }

    /**
     * Универсальный метод выполнения любой команды из графического интерфейса.
     *
     * @param commandName имя команды (например, "add", "remove_by_id", "login").
     * @param inputLines  список строк-аргументов, собранных из полей UI.
     * @return Универсальный объект Response со статус-кодом и данными.
     */
    public Response executeCommand(String commandName, List<String> inputLines) {
        if (commandName.equals("execute_script") && !inputLines.isEmpty()) {
            return handleScriptExecution(inputLines.get(0));
        }

        try {
            // Склеиваем переданные из UI поля через перенос строки
            String joinedInput = String.join("\n", inputLines);

            // Используем существующий конструктор Reader для строк
            Reader virtualReader = new Reader(joinedInput);

            CommandRequest request = parser.parseCommand(commandName, virtualReader);

            if (request == null) {
                return new Response(400, "error.command.validation_failed");
            }

            // Автоматически подкладываем данные текущей сессии
            request.setLogin(ConfigManager.login);
            request.setUserToken(ConfigManager.token);

            // Отправляем пакет по UDP
            udpClient.sendCommand(request);

            // Получаем ответ, где сервер гарантированно возвращает "КОД:ДАННЫЕ"
            Message response = udpClient.receiveResponse();

            // Парсим результат и возвращаем его интерфейсу
            return parseRawResponse(response.getText());

        } catch (java.net.SocketTimeoutException e) {
            return new Response(503, "error.network.server_unavailable");
        } catch (Exception e) {
            return new Response(500, "error.network.connection_failed");
        }
    }

    /**
     * Выполняет скрипт из файла, последовательно отправляя команды на сервер.
     *
     * @param filePath путь к файлу скрипта.
     * @return Ответ со статус-кодом 200 и списком результатов всех команд, склеенных в одну строку.
     */
    private Response handleScriptExecution(String filePath) {
        try {
            List<CommandRequest> scriptCommands = scriptManager.processScript(filePath);
            if (scriptCommands == null || scriptCommands.isEmpty()) {
                return new Response(200, "success.script.empty_or_executed");
            }

            List<String> results = new ArrayList<>();
            for (CommandRequest cmd : scriptCommands) {
                cmd.setLogin(ConfigManager.login);
                cmd.setUserToken(ConfigManager.token);

                udpClient.sendCommand(cmd);
                Message response = udpClient.receiveResponse();
                results.add(response.getText());
            }

            // Склеиваем результаты всех команд скрипта обратно для вывода в UI
            return new Response(200, String.join("\n", results));

        } catch (java.net.SocketTimeoutException e) {
            return new Response(503, "error.network.server_unavailable");
        } catch (Exception e) {
            return new Response(500, "error.network.script_failed");
        }
    }

    /**
     * Преобразует строку ответа сервера в объект Response.
     *
     * @param rawResponse сырой ответ от сервера в формате КОД:ДАННЫЕ.
     * @return Экземпляр ответа.
     */
    private Response parseRawResponse(String rawResponse) {
        if (rawResponse == null || !rawResponse.contains(":")) {
            return new Response(500, "error.internal.invalid_server_format");
        }

        // Разбиваем строку строго на две части по первому двоеточию
        String[] parts = rawResponse.split(":", 2);

        try {
            int statusCode = Integer.parseInt(parts[0].trim());
            String data = parts[1].trim();
            return new Response(statusCode, data);
        } catch (NumberFormatException e) {
            return new Response(500, "error.internal.invalid_status_code");
        }
    }
}