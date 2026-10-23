package service;

import dataaccess.AlreadyTakenException;
import dataaccess.DataStorage;
import dataaccess.LocalDataStorage;
import dataaccess.UnauthorizedException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.List;
import java.util.Objects;

public class Service {
    private DataStorage dataBase;

    public Service() {
        dataBase = new LocalDataStorage();
    }

    public void clear() {
        dataBase.clearAuthData();
        dataBase.clearUserData();
        dataBase.clearGameData();
    }

    public AuthData registerUser(UserData userData) {
        if (!userData.validData()) {
            throw new BadRequestException("bad request");
        } else if (dataBase.getUserData(userData.getUsername()) != null) {
            throw new AlreadyTakenException("already taken");
        } else {
            AuthData authData = new AuthData(userData.getUsername());
            dataBase.setUserData(userData);
            dataBase.setAuthData(authData);
            return authData;
        }
    }

    public AuthData loginUser(LoginData loginData) {
        if (!loginData.validData()) {
            throw new BadRequestException("bad request");
        } else if (dataBase.getUserData(loginData.getUsername()) == null) {
            throw new UnauthorizedException("unauthorized");
        } else if (!Objects.equals(dataBase.getUserData(loginData.getUsername()).getPassword(), loginData.getPassword())) {
            throw new UnauthorizedException("unauthorized");
        } else {
            AuthData authData = new AuthData(loginData.getUsername());
            dataBase.setAuthData(authData);
            return authData;
        }
    }

    public void logoutUser(String authToken) {
        dataBase.removeAuthData(authToken);
    }

    public List<GameData> listGames() {
        return dataBase.getAllGames();
    }

    public boolean authorized(String authToken) {
        AuthData authData = dataBase.getAuthData(authToken);
        return authData != null && authData.validToken();
    }

    public int createGame(String gameName) {
        int gameID = dataBase.getNextGameID();
        GameData gameData = new GameData(gameID, gameName);
        dataBase.setGameData(gameData);
        return gameID;
    }

    public void joinGame(String authToken, JoinGameData joinGameData) {
        if (!joinGameData.validData()) {
            throw new BadRequestException("bad request");
        }
        GameData gameData = dataBase.getGameData(joinGameData.getGameID());
        if (gameData == null) {
            throw new BadRequestException("bad request");
        }

        String userName = dataBase.getAuthData(authToken).getUsername();

        if (Objects.equals(joinGameData.getPlayerColor(), "WHITE")) {
            if (gameData.getWhiteUsername() == null || gameData.getWhiteUsername().isEmpty()) {
                gameData.setWhiteUserName(userName);
            } else {
                throw new AlreadyTakenException("already taken");
            }
        } else if (Objects.equals(joinGameData.getPlayerColor(), "BLACK")) {
            if (gameData.getBlackUsername() == null || gameData.getBlackUsername().isEmpty()) {
                gameData.setBlackUserName(userName);
            } else {
                throw new AlreadyTakenException("already taken");
            }
        }

        dataBase.setGameData(gameData);
    }
}
