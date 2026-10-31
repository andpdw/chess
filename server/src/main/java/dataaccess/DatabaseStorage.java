package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            var statement = "INSERT INTO userData (username, password, email) VALUES (?, ?, ?)";
            String password = userData.getPassword(); //Hash later
            executeUpdate(statement, userData.getUsername(), password, userData.getEmail());
        } catch (Exception e) {
            System.out.println("Unable to add userData: " + e);
        }
    }

    @Override
    public UserData getUserData(String username) {
        return null;
    }

    @Override
    public List<UserData> getAllUsers() {
        return List.of();
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
            var statement = "INSERT INTO authData (username, authToken) VALUES (?, ?)";
            executeUpdate(statement, authData.getUsername(), authData.getAuthToken());
        } catch (Exception e) {
            System.out.println("Unable to add authData: " + e);
        }
    }

    @Override
    public AuthData getAuthData(String authToken) {
        return null;
    }

    @Override
    public List<AuthData> getAllAuthData() {
        return List.of();
    }

    @Override
    public void removeAuthData(String authToken) {

    }

    @Override
    public void clearUserData() {

    }

    @Override
    public void clearGameData() {

    }

    @Override
    public void clearAuthData() {

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
              `gamejson` TEXT DEFAULT NULL,
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
              PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS authData (
              `username` varchar(256) NOT NULL,
              `authToken` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
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
