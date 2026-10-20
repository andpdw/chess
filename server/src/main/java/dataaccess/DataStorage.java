package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;


public interface DataStorage {

    void AddUserData(UserData userData);
    UserData GetUserData(String username);

    void SetGameData(GameData gameData);
    GameData GetGameData(int gameID);

    void SetAuthData(AuthData authData);
    AuthData GetAuthData(String userName);
}
