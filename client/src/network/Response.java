package network;

/**
 * Объект ответа от ядра к интерфейсу.
 * Используется для передачи статус-кода и данных (ключей локали или JSON-строки).
 */
public class Response {

    /**
     * Статус-код ответа сервера.
     */
    private final int statusCode;

    /**
     * Данные ответа (содержит ключ локализации или JSON).
     */
    private final String data;

    /**
     * Конструктор ответа.
     *
     * @param statusCode код состояния.
     * @param data       полезные данные или ключ.
     */
    public Response(int statusCode, String data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    /**
     * Возвращает статус-код.
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Возвращает данные ответа.
     */
    public String getData() {
        return data;
    }

    /**
     * Проверяет, успешный ли код (в диапазоне 200-299).
     *
     * @return true, если запрос успешный.
     */
    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300;
    }
}