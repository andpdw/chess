package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.List;


public interface DataStorage {

    void setUserData(UserData userData);
    UserData getUserData(String username);
    List<UserData> getAllUsers();

    void setGameData(GameData gameData);
    GameData getGameData(int gameID);
    List<GameData> getAllGames();
    int getNextGameID();

    void setAuthData(AuthData authData);
    AuthData getAuthData(String authToken);
    List<AuthData> getAllAuthData();
    void removeAuthData(String authToken);

    void clearUserData();
    void clearGameData();
    void clearAuthData();
}