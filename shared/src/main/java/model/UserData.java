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

    public String getEmail() {
        return email;
    }

    public boolean validData() {
        if (username.isEmpty() || username.isBlank()) {
            return false;
        } else if (password.isEmpty() || password.isBlank()) {
            return false;
        } else if (email.isEmpty() || email.isBlank()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        return "Username: " +  username + ", Password: " + password + ", email: " + email;
    }
}
