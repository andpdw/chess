package model;

public class UserData {
    static private String username;
    static private String password;
    static private String email;

    public UserData(String usernameIn, String passwordIn, String emailIn) {
        username = usernameIn;
        password = passwordIn;
        email = emailIn;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
