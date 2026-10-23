package service;

public class LoginData {
    private String username;
    private String password;

    public LoginData(String usernameIn, String passwordIn) {
        username = usernameIn;
        password = passwordIn;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean validData() {
        return !(username == null || username.isEmpty() || password == null || password.isEmpty());
    }
}
