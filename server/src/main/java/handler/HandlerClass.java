package handler;

import dataaccess.UnauthorizedException;
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
        if (!authorized(authToken)) {
            throw new UnauthorizedException("unauthorized");
        }
        service.logoutUser(authToken);
    }

    public List<GameData> listGames(String authToken) {
        if (!authorized(authToken)) {
            throw new UnauthorizedException("unauthorized");
        }
        return service.listGames();
    }

    public int createGame(String authToken, String gameName) {
        if (!authorized(authToken)) {
            throw new UnauthorizedException("unauthorized");
        }
        return service.createGame(gameName);
    }

    public void joinGame(String authToken, JoinGameData joinGameData) {
        if (!authorized(authToken)) {
            throw new UnauthorizedException("unauthorized");
        }
        service.joinGame(authToken, joinGameData);
    }

    public boolean authorized(String authToken) {
        return service.authorized(authToken);
    }
}
