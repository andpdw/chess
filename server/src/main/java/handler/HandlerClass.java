package handler;

import model.AuthData;
import model.GameData;
import model.UserData;
import service.JoinGameData;
import service.LoginData;
import service.Service;

import java.util.List;

public class HandlerClass {
    private Service service;

    public HandlerClass() {
        service = new Service();
    }

    public void clear() {
        service.clear();
    }

    public AuthData registerUser(UserData userData) {
        return service.registerUser(userData);
    }

    public AuthData loginUser(LoginData loginData) {
        return service.loginUser(loginData);
    }

    public void logoutUser(String authToken) {
        checkIfAuthorized(authToken);
        service.logoutUser(authToken);
    }

    public List<GameData> listGames(String authToken) {
        checkIfAuthorized(authToken);
        return service.listGames();
    }

    public int createGame(String authToken, String gameName) {
        checkIfAuthorized(authToken);
        return service.createGame(gameName);
    }

    public void joinGame(String authToken, JoinGameData joinGameData) {
        checkIfAuthorized(authToken);
        service.joinGame(authToken, joinGameData);
    }

    public void checkIfAuthorized(String authToken) {
        if (!service.authorized(authToken)) {
            throw new UnauthorizedException("unauthorized");
        }
    }
}
