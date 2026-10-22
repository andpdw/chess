package passoff.server;

import chess.ChessGame;
import dataaccess.LocalDataStorage;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataStorageTests {
    private static AuthData authDataTest;
    private static GameData gameDataTest;
    private static UserData userDataTest;
    private static LocalDataStorage dataStorageTest;

    @BeforeAll
    public static void init() {
        dataStorageTest = new LocalDataStorage();
    }

    @BeforeEach
    public void setup() {
        dataStorageTest.clearUserData();
        dataStorageTest.clearGameData();
        dataStorageTest.clearAuthData();
        authDataTest = new AuthData("usernameTest");
        gameDataTest = new GameData(13, "gameTest");
        userDataTest = new UserData("usernameTest", "passwordTest", "test@email.com");

        dataStorageTest.setAuthData(authDataTest);
        dataStorageTest.setUserData(userDataTest);
        dataStorageTest.setGameData(gameDataTest);
    }

    @Test
    @Order(1)
    @DisplayName("UserData Test")
    public void testUserData() {
        //Tests getting data from UserData
        Assertions.assertEquals("usernameTest", userDataTest.getUsername(), "Different Username");
        Assertions.assertEquals("passwordTest", userDataTest.getPassword(), "Different Password");
        Assertions.assertEquals("test@email.com", userDataTest.getEmail(), "Different Email");

        //Tests toString
        Assertions.assertEquals("Username: usernameTest, Password: passwordTest, email: test@email.com", userDataTest.toString(), "Different String Values");

        //Tests validData Function
        Assertions.assertTrue(userDataTest.validData(), "validData returned wrong value");
        userDataTest = new UserData("", "passwordTest", "test@email.com");
        Assertions.assertFalse(userDataTest.validData(), "validData returned wrong value");
        userDataTest = new UserData("usernameTest", "", "test@email.com");
        Assertions.assertFalse(userDataTest.validData(), "validData returned wrong value");
        userDataTest = new UserData("usernameTest", "passwordTest", "");
        Assertions.assertFalse(userDataTest.validData(), "validData returned wrong value");
    }

    @Test
    @Order(2)
    @DisplayName("AuthData Test")
    public void testAuthData() {
        //Tests getting data from AuthData
        Assertions.assertEquals("usernameTest", authDataTest.getUsername(), "Different Username");
        Assertions.assertNotEquals("", authDataTest.getAuthToken(), "No AuthToken returned");
    }

    @Test
    @Order(3)
    @DisplayName("GameData Test")
    public void testGameData() {
        //Test getting data from AuthData
        Assertions.assertEquals(13, gameDataTest.getID(), "Different Game IDs");
        Assertions.assertEquals("gameTest", gameDataTest.getGameName(), "Different Game Name");
        Assertions.assertEquals(new ChessGame(), gameDataTest.getGame(), "Different ChessGame");

        //Tests the adding user functions
        gameDataTest.setBlackUserName("blackUserTest");
        gameDataTest.setWhiteUserName("whiteUserTest");
        Assertions.assertEquals("whiteUserTest", gameDataTest.getWhiteUsername(), "Different White User");
        Assertions.assertEquals("blackUserTest", gameDataTest.getBlackUsername(), "Different Black User");
        gameDataTest.removeBlackUserName();
        gameDataTest.removeWhiteUserName();
        gameDataTest.setBlackUserName("blackUserTest");
        gameDataTest.setWhiteUserName("whiteUserTest");
        Assertions.assertEquals("whiteUserTest", gameDataTest.getWhiteUsername(), "Different White User");
        Assertions.assertEquals("blackUserTest", gameDataTest.getBlackUsername(), "Different Black User");

        //Tests setting user if already set
        Assertions.assertThrows(RuntimeException.class, () -> gameDataTest.setBlackUserName("duplicateBlack"), "Exception not thrown");
        Assertions.assertThrows(RuntimeException.class, () -> gameDataTest.setWhiteUserName("duplicateWhite"), "Exception not thrown");

    }

    @Test
    @Order(4)
    @DisplayName("DataStorageTest")
    public void testDataStorage() {
        //Test get data functions
        Assertions.assertEquals(authDataTest, dataStorageTest.getAuthData(authDataTest.getAuthToken()), "Different AuthData");
        Assertions.assertEquals(userDataTest, dataStorageTest.getUserData(userDataTest.getUsername()), "Different UserData");
        Assertions.assertEquals(gameDataTest, dataStorageTest.getGameData(gameDataTest.getID()), "Different GameData");

        AuthData authDataTest2 = new AuthData("usernameTest2");
        UserData userDataTest2 = new UserData("usernameTest2", "passwordTest2", "test2@email.com");
        GameData gameDataTest2 = new GameData(15, "gameTest2");

        dataStorageTest.setAuthData(authDataTest2);
        dataStorageTest.setUserData(userDataTest2);
        dataStorageTest.setGameData(gameDataTest2);

        List<AuthData> testAuthData = List.of(authDataTest, authDataTest2);
        List<UserData> testUserData = List.of(userDataTest, userDataTest2);
        List<GameData> testGameData = List.of(gameDataTest, gameDataTest2);

        Assertions.assertEquals(testAuthData, dataStorageTest.getAllAuthData(), "Missing AuthData");
        Assertions.assertEquals(testUserData, dataStorageTest.getAllUsers(), "Missing UserData");
        Assertions.assertEquals(testGameData, dataStorageTest.getAllGames(), "Missing GameData");
    }
}
