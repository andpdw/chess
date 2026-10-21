package passoff.server;

import dataaccess.LocalDataStorage;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

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
        dataStorageTest.ClearUserData();
        dataStorageTest.ClearGameData();
        dataStorageTest.ClearAuthData();
        authDataTest = new AuthData("usernameTest");
        gameDataTest = new GameData(13, "gameTest");
        userDataTest = new UserData("usernameTest", "passwordTest", "test@email.com");

        dataStorageTest.SetAuthData(authDataTest);
        dataStorageTest.SetUserData(userDataTest);
        dataStorageTest.SetGameData(gameDataTest);
    }

    @Test
    @Order(1)
    @DisplayName("UserData Test")
    public void testUserData() {
        //Tests getting data from UserData
        Assertions.assertEquals("usernameTest", userDataTest.getUsername(), "Different Username");
        Assertions.assertEquals("passwordTest", userDataTest.getPassword(), "Different Password");
        Assertions.assertEquals("test@email.com", userDataTest.getEmail(), "Different Email");

        //Tests validData Function
        Assertions.assertTrue(userDataTest.validData(), "validData returned wrong value");
        userDataTest = new UserData("", "passwordTest", "test@email.com");
        Assertions.assertFalse(userDataTest.validData(), "validData returned wrong value");
        userDataTest = new UserData("usernameTest", "", "test@email.com");
        Assertions.assertFalse(userDataTest.validData(), "validData returned wrong value");
        userDataTest = new UserData("usernameTest", "passwordTest", "");
        Assertions.assertFalse(userDataTest.validData(), "validData returned wrong value");
    }

}
