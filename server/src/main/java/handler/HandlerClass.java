package handler;

import dataaccess.ConnectionError;
import model.AuthData;
import model.GameData;
import model.UserData;
import model.JoinGameData;
import model.LoginData;
import service.Service;

import java.util.List;

public class HandlerClass {
    private Service service;

    public HandlerClass() {
        try {
            service = new Service();
        } catch (ConnectionError e) {
            throw new ConnectionError(toString());
        }
    }

    public void clear() {
        try {
            service.clear();
        } catch (ConnectionError e) {
            throw new ConnectionError(toString());
        }
    }

    public AuthData registerUser(UserData userData) {
        try {
            return service.registerUser(userData);
        } catch (ConnectionError e) {
            throw new ConnectionError(toString());
        }
    }

    public AuthData loginUser(LoginData loginData) {
        try {
            return service.loginUser(loginData);
        } catch (ConnectionError e) {
            throw new ConnectionError(toString());
        }
    }

    public void logoutUser(String authToken) {
        try {
            checkIfAuthorized(authToken);
            service.logoutUser(authToken);
        } catch (ConnectionError e) {
            throw new ConnectionError(toString());
        }
    }

    public List<GameData> listGames(String authToken) {
        try {
            checkIfAuthorized(authToken);
            return service.listGames();
        } catch (ConnectionError e) {
            throw new ConnectionError(toString());
        }
    }

    public int createGame(String authToken, String gameName) {
        try {
            checkIfAuthorized(authToken);
            return service.createGame(gameName);
        } catch (ConnectionError e) {
            throw new ConnectionError(toString());
        }
    }

    public void joinGame(String authToken, JoinGameData joinGameData) {
        try {
            checkIfAuthorized(authToken);
            service.joinGame(authToken, joinGameData);
        } catch (ConnectionError e) {
            throw new ConnectionError(toString());
        }
    }

    public void checkIfAuthorized(String authToken) {
        try {
            if (!service.authorized(authToken)) {
                throw new UnauthorizedException("unauthorized");
            }
        } catch (ConnectionError e) {
            throw new ConnectionError(toString());
        }
    }
}
