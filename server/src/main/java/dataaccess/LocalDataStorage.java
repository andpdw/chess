package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

public class LocalDataStorage implements DataStorage {

    @Override
    public void AddUserData(UserData userData) {

    }

    @Override
    public UserData GetUserData(String username) {
        return null;
    }

    @Override
    public void SetGameData(GameData gameData) {

    }

    @Override
    public GameData GetGameData(int gameID) {
        return null;
    }

    @Override
    public void SetAuthData(AuthData authData) {

    }

    @Override
    public AuthData GetAuthData(String userName) {
        return null;
    }
}
