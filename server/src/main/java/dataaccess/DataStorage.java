package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.List;
import java.util.Map;


public interface DataStorage {

    void SetUserData(UserData userData);
    UserData GetUserData(String username);
    List<UserData> GetAllUsers();

    void SetGameData(GameData gameData);
    GameData GetGameData(int gameID);
    List<GameData> GetAllGames();
    int GetNextGameID();

    void SetAuthData(AuthData authData);
    AuthData GetAuthData(String authToken);
    List<AuthData> GetAllAuthData();
    void RemoveAuthData(String authToken);

    void ClearUserData();
    void ClearGameData();
    void ClearAuthData();
}