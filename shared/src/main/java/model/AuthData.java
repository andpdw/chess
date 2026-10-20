package model;

import java.util.UUID;

public class AuthData {
    private String authToken;
    private String username;

    public AuthData(String usernameIn) {
        username = usernameIn;
        authToken = UUID.randomUUID().toString();
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }
}
