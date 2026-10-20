package model;

public class UserData {
    private String username;
    private String password;
    private String email;

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

    @Override
    public String toString() {
        return "Username: " +  username + ", Password: " + password + ", email: " + email;
    }

    public String getEmail() {
        return email;
    }
}
