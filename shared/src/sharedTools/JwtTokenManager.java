package sharedTools;

import exceptions.TokenException;

import java.io.*;
import java.security.MessageDigest;
import java.util.Base64;

public class JwtTokenManager {
    private final String secretKey;

    public JwtTokenManager(String secretKey) {
        if (secretKey == null || secretKey.isEmpty()) {
            this.secretKey = "SUPER_SECRET_KEY_FOR_SUPER_COOL_SECURITY";
        } else {
            this.secretKey = secretKey;
        }
    }

    /**
     * Создает строковый токен: Base64(Payload) + "." + Base64(Signature)
     * @param userId ID пользователя из БД
     * @param login Логин пользователя
     * @param role Роль ('user' или 'admin')
     * @param durationMs Время жизни токена в миллисекундах (например, 1800000 для 30 минут)
     */
    public String createToken(Long userId, String login, String role, long durationMs) throws TokenException {
        try {
            TokenPayload payload = new TokenPayload(userId, login, role, durationMs);

            // Сериализуем payload в байты и кодируем в Base64 URL-safe (без лишних символов)
            byte[] payloadBytes = serialize(payload);
            String payloadBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(payloadBytes);

            // Создаем цифровую подпись на основе payload и секретного ключа
            String signature = generateSignature(payloadBase64, this.secretKey);

            // Склеиваем через точку
            return payloadBase64 + "." + signature;
        } catch (Exception e) {
            throw new TokenException("Ошибка при создании токена", e);
        }
    }

    /**
     * Проверяет токен на валидность, отсутствие подделки и срок действия.
     * Возвращает чистый Payload, если всё хорошо.
     */
    public TokenPayload validateAndParse(String tokenStr) throws TokenException {
        if (tokenStr == null || !tokenStr.contains(".")) {
            throw new TokenException("Неверный формат токена (отсутствует разделитель).");
        }

        String[] parts = tokenStr.split("\\.");
        if (parts.length != 2) {
            throw new TokenException("Неверная структура токена.");
        }

        String payloadBase64 = parts[0];
        String providedSignature = parts[1];

        try {
            // Проверяем подпись. Генерируем ожидаемую подпись от полученного payload и нашего secretKey
            String expectedSignature = generateSignature(payloadBase64, this.secretKey);
            if (!expectedSignature.equals(providedSignature)) {
                throw new TokenException("Токен скомпрометирован! Подпись не совпадает.");
            }

            // Десериализуем payload обратно в объект
            byte[] payloadBytes = Base64.getUrlDecoder().decode(payloadBase64);
            TokenPayload payload = (TokenPayload) deserialize(payloadBytes);

            // Проверяем, не истекло ли время жизни токена
            if (System.currentTimeMillis() > payload.getExpireAt()) {
                throw new TokenException("Срок действия токена истек. Пожалуйста, авторизуйтесь заново.");
            }

            return payload;
        } catch (TokenException e) {
            throw e;
        } catch (Exception e) {
            throw new TokenException("Не удалось распарсить токен (возможно, он поврежден).", e);
        }
    }

    // Хэширование SHA-256 для генерации неподделываемой подписи
    private String generateSignature(String data, String secret) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String input = data + "::" + secret;
        byte[] hash = digest.digest(input.getBytes("UTF-8"));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    }

    // Вспомогательный метод сериализации объекта в byte[]
    private byte[] serialize(Object obj) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            return baos.toByteArray();
        }
    }

    // Вспомогательный метод десериализации byte[] в объект
    private Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        }
    }
}