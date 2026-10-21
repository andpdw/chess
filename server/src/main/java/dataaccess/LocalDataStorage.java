package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalDataStorage implements DataStorage {
    private static Map<String, UserData> UserDataMap = new HashMap<>();
    private static Map<Integer, GameData> GameDataMap = new HashMap<>();
    private static Map<String, AuthData> AuthDataMap = new HashMap<>();

    @Override
    public void SetUserData(UserData userData) {
        UserDataMap.put(userData.getUsername(), userData);
    }

    @Override
    public UserData GetUserData(String username) {
        return UserDataMap.get(username);
    }

    @Override
    public List<UserData> GetAllUsers() {
        List<UserData> returnList = new ArrayList<>();
        for (Map.Entry<String, UserData> user : UserDataMap.entrySet()) {
            returnList.add(user.getValue());
        }
        return returnList;
    }


    @Override
    public void SetGameData(GameData gameData) {

    }

    @Override
    public GameData GetGameData(int gameID) {
        return null;
    }

    @Override
    public List<GameData> GetAllGames() {
        List<GameData> returnList = new ArrayList<>();
        for (Map.Entry<Integer, GameData> game : GameDataMap.entrySet()) {
            returnList.add(game.getValue());
        }
        return returnList;
    }

    @Override
    public void SetAuthData(AuthData authData) {
        AuthDataMap.put(authData.getAuthToken(), authData);
    }

    @Override
    public AuthData GetAuthData(String authToken) {
        return AuthDataMap.get(authToken);
    }

    @Override
    public List<AuthData> GetAllAuthData() {
        List<AuthData> returnList = new ArrayList<>();
        for (Map.Entry<String, AuthData> auth : AuthDataMap.entrySet()) {
            returnList.add(auth.getValue());
        }
        return returnList;
    }

    @Override
    public void ClearUserData() {
        UserDataMap.clear();
    }

    @Override
    public void ClearGameData() {
        GameDataMap.clear();
    }

    @Override
    public void ClearAuthData() {
        AuthDataMap.clear();
    }
}
