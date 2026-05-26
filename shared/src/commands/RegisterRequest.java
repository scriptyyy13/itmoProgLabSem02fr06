package commands;

/**
 * Реквест для команды регистрации нового аккаунта.
 */
public class RegisterRequest extends CommandRequest {
    private final String userLogin;
    private final String userPassword;

    public RegisterRequest(String userLogin, String userPassword) {
        this.userLogin = userLogin;
        this.userPassword = userPassword;
    }

    public String getUserLogin() { return userLogin; }
    public String getUserPassword() { return userPassword; }
}