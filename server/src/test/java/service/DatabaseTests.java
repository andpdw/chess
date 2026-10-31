package service;

import dataaccess.DataStorage;
import dataaccess.DatabaseStorage;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseTests {
    private static DataStorage testSQL;

    @BeforeAll
    public static void init() {
        try {
            testSQL = new DatabaseStorage();
        } catch (Exception e) {
            System.out.println("Error setting up DB: " + e);
        }
    }

    @BeforeEach
    public void setup() {
        testSQL.clearGameData();
        testSQL.clearUserData();
        testSQL.clearAuthData();
    }

    @Test
    @Order(1)
    @DisplayName("Add authData")
    public void addAuthData() {
        AuthData testAuth = new AuthData("userTest");
        Assertions.assertDoesNotThrow(() -> testSQL.setAuthData(testAuth));
        AuthData responseAuth = testSQL.getAuthData(testAuth.getAuthToken());
        Assertions.assertEquals(responseAuth.getUsername(), testAuth.getUsername());
    }

    @Test
    @Order(2)
    @DisplayName("Add userData")
    public void addUserData() {
        UserData testUser = new UserData("testUser", "testPass", "cow@gmail");
        Assertions.assertDoesNotThrow(() -> testSQL.setUserData(testUser));
        UserData responseUser = testSQL.getUserData(testUser.getUsername());
        Assertions.assertEquals(responseUser.getPassword(), testUser.getPassword());
    }
}
