package commands;

/**
 * Реквест для команды авторизации (входа) в аккаунт.
 */
public class LoginRequest extends CommandRequest {
    private final String userLogin;
    private final String userPassword;

    public LoginRequest(String userLogin, String userPassword) {
        this.userLogin = userLogin;
        this.userPassword = userPassword;
    }

    public String getUserLogin() { return userLogin; }
    public String getUserPassword() { return userPassword; }
}