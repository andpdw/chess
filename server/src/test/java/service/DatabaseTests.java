package service;

import dataaccess.DataStorage;
import dataaccess.DatabaseStorage;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Test
    @Order(3)
    @DisplayName("Get AuthData List")
    public void getAuthDataList() {
        AuthData testAuth1 = new AuthData("testUser1");
        AuthData testAuth2 = new AuthData("testUser2");
        List<AuthData> answerList = new ArrayList<>();
        answerList.add(testAuth1);
        answerList.add(testAuth2);

        Assertions.assertDoesNotThrow(() -> testSQL.setAuthData(testAuth1));
        Assertions.assertDoesNotThrow(() -> testSQL.setAuthData(testAuth2));

        List<AuthData> response = testSQL.getAllAuthData();

        Assertions.assertTrue((Objects.equals(answerList.get(0).getUsername(), response.get(0).getUsername())) ||
                (Objects.equals(answerList.get(0).getUsername(), response.get(1).getUsername())));
        Assertions.assertTrue((Objects.equals(answerList.get(1).getUsername(), response.get(0).getUsername())) ||
                (Objects.equals(answerList.get(1).getUsername(), response.get(1).getUsername())));
    }

    @Test
    @Order(4)
    @DisplayName("Get UserData List")
    public void getUserDataList() {
        UserData testUser1 = new UserData("testUser1", "pass", "email");
        UserData testUser2 = new UserData("testUser2", "pass", "email");
        List<UserData> answerList = new ArrayList<>();
        answerList.add(testUser1);
        answerList.add(testUser2);

        Assertions.assertDoesNotThrow(() -> testSQL.setUserData(testUser1));
        Assertions.assertDoesNotThrow(() -> testSQL.setUserData(testUser2));

        List<UserData> response = testSQL.getAllUsers();

        Assertions.assertTrue((Objects.equals(answerList.get(0).getUsername(), response.get(0).getUsername())) ||
                (Objects.equals(answerList.get(0).getUsername(), response.get(1).getUsername())));
        Assertions.assertTrue((Objects.equals(answerList.get(1).getUsername(), response.get(0).getUsername())) ||
                (Objects.equals(answerList.get(1).getUsername(), response.get(1).getUsername())));
    }

    @Test
    @Order(5)
    @DisplayName("Remove Auth Data")
    public void removeAuthDataTest() {
        AuthData testAuth1 = new AuthData("testUser1");

        Assertions.assertDoesNotThrow(() -> testSQL.setAuthData(testAuth1));
        Assertions.assertDoesNotThrow(() -> testSQL.removeAuthData(testAuth1.getAuthToken()));

        List<AuthData> response = testSQL.getAllAuthData();

        Assertions.assertTrue(response.isEmpty());
    }
}
