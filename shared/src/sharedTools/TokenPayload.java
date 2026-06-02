package sharedTools;

import java.io.Serializable;

public class TokenPayload implements Serializable {
    /**
     * ID пользователя
     **/
    private final Long userId;
    /**
     * Логин пользователя
     **/
    private final String login;
    /**
     * Роль пользователя
     **/
    private final String role;
    /**
     * Время до которого живет токен
     **/
    private final long expireAt;

    public TokenPayload(Long userId, String login, String role, long durationMs) {
        this.userId = userId;
        this.login = login;
        this.role = role;
        this.expireAt = System.currentTimeMillis() + durationMs;
    }

    public Long getUserId() {
        return userId;
    }

    public String getLogin() {
        return login;
    }

    public String getRole() {
        return role;
    }

    public long getExpireAt() {
        return expireAt;
    }

    @Override
    public String toString() {
        return "TokenPayload{" +
                "userId=" + userId +
                ", login='" + login + '\'' +
                ", role='" + role + '\'' +
                ", expireAt=" + expireAt +
                '}';
    }
}