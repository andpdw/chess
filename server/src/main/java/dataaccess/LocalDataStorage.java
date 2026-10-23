package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.*;

public class LocalDataStorage implements DataStorage {
    private static Map<String, UserData> userDataMap = new HashMap<>();
    private static Map<Integer, GameData> gameDataMap = new HashMap<>();
    private static Map<String, AuthData> authDataMap = new HashMap<>();
    private int nextGameID = 0;

    @Override
    public void setUserData(UserData userData) {
        userDataMap.put(userData.getUsername(), userData);
    }

    @Override
    public UserData getUserData(String username) {
        return userDataMap.get(username);
    }

    @Override
    public List<UserData> getAllUsers() {
        return getAllData(UserData.class, userDataMap);
    }

    @Override
    public void setGameData(GameData gameData) {
        gameDataMap.put(gameData.getID(), gameData);
    }

    @Override
    public GameData getGameData(int gameID) {
        return gameDataMap.get(gameID);
    }

    @Override
    public List<GameData> getAllGames() {
        List<GameData> returnList = new ArrayList<>();
        for (Map.Entry<Integer, GameData> game : gameDataMap.entrySet()) {
            returnList.add(game.getValue());
        }
        return returnList;
    }

    @Override
    public int getNextGameID() {
        nextGameID += 1;
        return nextGameID;
    }

    @Override
    public void setAuthData(AuthData authData) {
        authDataMap.put(authData.getAuthToken(), authData);
    }

    @Override
    public AuthData getAuthData(String authToken) {
        return authDataMap.get(authToken);
    }

    @Override
    public List<AuthData> getAllAuthData() {
        return getAllData(AuthData.class, authDataMap);
    }

    @Override
    public void removeAuthData(String authToken) {
        authDataMap.remove(authToken);
    }

    @Override
    public void clearUserData() {
        userDataMap.clear();
    }

    @Override
    public void clearGameData() {
        gameDataMap.clear();
    }

    @Override
    public void clearAuthData() {
        authDataMap.clear();
    }

    private <T> List<T> getAllData(Class<T> type, Map<String, T> data ) {
        return new ArrayList<>(data.values());
    }
}
