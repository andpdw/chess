package dataaccess;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class DatabaseStorage implements DataStorage{

    public DatabaseStorage() throws ResponseException, DataAccessException {
        configureDatabase();
    }

    @Override
    public void setUserData(UserData userData) {
        try {
            var statement = "INSERT INTO userData (username, password, email, json) VALUES (?, ?, ?, ?)";
            String password = userData.getPassword(); //Hash later
            String json = new Gson().toJson(userData);
            executeUpdate(statement, userData.getUsername(), password, userData.getEmail(), json);
        } catch (Exception e) {
            System.out.println("Unable to add userData: " + e);
        }
    }

    @Override
    public UserData getUserData(String username) {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, json FROM userData WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Gson().fromJson(rs.getString("json"), UserData.class);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting authData for " + username + ", " + e);
        }
        return null;
    }

    @Override
    public List<UserData> getAllUsers() {
        List<UserData> returnList = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT json FROM userData";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        returnList.add(new Gson().fromJson(rs.getString("json"), UserData.class));
                    }
                    return returnList;
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting list of userData: " + e);
        }
        return null;
    }

    @Override
    public void setGameData(GameData gameData) {

    }

    @Override
    public GameData getGameData(int gameID) {
        return null;
    }

    @Override
    public List<GameData> getAllGames() {
        return List.of();
    }

    @Override
    public int getNextGameID() {
        return 0;
    }

    @Override
    public void setAuthData(AuthData authData) {
        try {
            var statement = "INSERT INTO authData (username, authToken, json) VALUES (?, ?, ?)";
            String json = new Gson().toJson(authData);
            executeUpdate(statement, authData.getUsername(), authData.getAuthToken(), json);
        } catch (Exception e) {
            System.out.println("Unable to add authData: " + e);
        }
    }

    @Override
    public AuthData getAuthData(String authToken) {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username, json FROM authData WHERE authToken=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Gson().fromJson(rs.getString("json"), AuthData.class);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting authData for " + authToken + ", " + e);
        }
        return null;
    }

    @Override
    public List<AuthData> getAllAuthData() {
        List<AuthData> returnList = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT json FROM authData";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        returnList.add(new Gson().fromJson(rs.getString("json"), AuthData.class));
                    }
                    return returnList;
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting list of authData: " + e);
        }
        return null;
    }

    @Override
    public void removeAuthData(String authToken) {

    }

    @Override
    public void clearUserData() {
        var statement = "TRUNCATE userData";
        try {
            executeUpdate(statement);
        } catch (Exception e) {
            System.out.println("Error deleting userData: " + e);
        }
    }

    @Override
    public void clearGameData() {
        var statement = "TRUNCATE chessGames";
        try {
            executeUpdate(statement);
        } catch (Exception e) {
            System.out.println("Error deleting gameData: " + e);
        }
    }

    @Override
    public void clearAuthData() {
        var statement = "TRUNCATE authData";
        try {
            executeUpdate(statement);
        } catch (Exception e) {
            System.out.println("Error deleting authData: " + e);
        }
    }

    private int executeUpdate(String statement, Object... params) throws ResponseException, DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof GameData p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new ResponseException(ResponseException.Code.ServerError, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS chessGames (
              `id` int NOT NULL AUTO_INCREMENT,
              `name` varchar(256) NOT NULL,
              `whiteUser` varchar(256) DEFAULT NULL,
              `blackUser` varchar(256) DEFAULT NULL,
              `gamejson` TEXT NOT NULL,
              PRIMARY KEY (`id`),
              INDEX(whiteUser),
              INDEX(blackUser)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS userData (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              `json` TEXT NOT NULL,
              PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS authData (
              `username` varchar(256) NOT NULL,
              `authToken` varchar(256) NOT NULL,
              `json` TEXT NOT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws ResponseException, DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
