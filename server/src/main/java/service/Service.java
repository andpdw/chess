package service;

import dataaccess.ConnectionError;
import dataaccess.DataStorage;
import dataaccess.sql.DatabaseStorage;
import handler.UnauthorizedException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Objects;

public class Service {
    private DataStorage dataBase;

    public Service() {
        try {
            dataBase = new DatabaseStorage();
        } catch (ConnectionError e) {
            throw new ConnectionError(e.toString());
        } catch (Exception e) {
            System.out.println("Error setting up DC: " + e);
        }
    }

    public void clear() {
        try {
            dataBase.clearAuthData();
            dataBase.clearUserData();
            dataBase.clearGameData();
        } catch (ConnectionError e) {
            throw new ConnectionError(e.toString());
        }
    }

    public AuthData registerUser(UserData userData) {
        if (!userData.validData()) {
            throw new BadRequestException("bad request");
        } else if (dataBase.getUserData(userData.getUsername()) != null) {
            throw new AlreadyTakenException("already taken");
        } else {
            try {
                AuthData authData = new AuthData(userData.getUsername());
                dataBase.setUserData(userData);
                dataBase.setAuthData(authData);
                return authData;
            } catch (ConnectionError e) {
                throw new ConnectionError(e.toString());
            }
        }
    }

    public AuthData loginUser(LoginData loginData) {
        if (!loginData.validData()) {
            throw new BadRequestException("bad request");
        } else if (dataBase.getUserData(loginData.getUsername()) == null) {
            throw new UnauthorizedException("unauthorized");
        } else if (!BCrypt.checkpw(loginData.getPassword(), dataBase.getUserData(loginData.getUsername()).getPassword())) {
            throw new UnauthorizedException("unauthorized");
        } else {
            try {
                AuthData authData = new AuthData(loginData.getUsername());
                dataBase.setAuthData(authData);
                return authData;
            } catch (ConnectionError e) {
                throw new ConnectionError(e.toString());
            }
        }
    }

    public void logoutUser(String authToken) {
        try {
            dataBase.removeAuthData(authToken);
        } catch (ConnectionError e) {
            throw new ConnectionError(e.toString());
        }
    }

    public List<GameData> listGames() {
        try {
            return dataBase.getAllGames();
        } catch (ConnectionError e) {
            throw new ConnectionError(e.toString());
        }
    }

    public int createGame(String gameName) {
        try {
            int gameID = dataBase.getNextGameID();
            GameData gameData = new GameData(gameID, gameName);
            return dataBase.setGameData(gameData);
        } catch (ConnectionError e) {
            throw new ConnectionError(e.toString());
        }
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
        try {
            dataBase.setGameData(gameData);
        } catch (ConnectionError e) {
            throw new ConnectionError(e.toString());
        }
    }

    public boolean authorized(String authToken) {
        try {
            AuthData authData = dataBase.getAuthData(authToken);
            return authData != null && authData.validToken();
        } catch (ConnectionError e) {
            throw new ConnectionError(e.toString());
        }
    }
}
