package service;

import handler.UnauthorizedException;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.*;
import service.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceTests {
    private static Service service;
    private static UserData userData;
    private static LoginData loginData;

    @BeforeAll
    public static void init() {
        service = new Service();
    }

    @BeforeEach
    public void setup() {
        service.clear();
        userData = new UserData("usernameTest", "passwordTest", "test@email.com");
        loginData = new LoginData("usernameTest", "passwordTest");
    }

    @Test
    @Order(1)
    @DisplayName("RegisterUser")
    public void testServiceRegisterUser() {
        UserData badData = new UserData("", "passwordTest", "test@email.com");

        Assertions.assertThrows(BadRequestException.class, () -> service.registerUser(badData), "Created with bad dada");
        Assertions.assertNotNull(service.registerUser(userData), "Did not get AuthData");
        Assertions.assertThrows(AlreadyTakenException.class, () -> service.registerUser(userData), "Created with dubplicate data");
    }

    @Test
    @Order(2)
    @DisplayName("LogoutUser")
    public void testServiceLogoutUser() {
        AuthData response = service.registerUser(userData);

        service.logoutUser(response.getAuthToken());
        Assertions.assertDoesNotThrow(() -> service.logoutUser(response.getAuthToken()), "Did not delete AuthToken");
    }

    @Test
    @Order(3)
    @DisplayName("LoginUser")
    public void testServiceLoginUser() {
        AuthData response = service.registerUser(userData);
        service.logoutUser(response.getAuthToken());
        response = service.loginUser(loginData);
        Assertions.assertNotNull(response, "Did not get Auth Token");

        LoginData badData = new LoginData("", "passwordTest");
        LoginData noUserName = new LoginData("notInDatabase", "passwordTest");
        LoginData badPassword = new LoginData("usernameTest", "bad");

        Assertions.assertThrows(BadRequestException.class, () -> service.loginUser(badData), "Did not throw error for bad data");
        Assertions.assertThrows(UnauthorizedException.class, () -> service.loginUser(noUserName), "Did not throw Unauthorized error");
        Assertions.assertThrows(UnauthorizedException.class, () -> service.loginUser(badPassword), "Did not throw Unauthorized error");
    }

    @Test
    @Order(4)
    @DisplayName("CreateGame")
    public void testServiceCreateGame() {
        AuthData response = service.registerUser(userData);
        int gameID = service.createGame("gameTest");
        Assertions.assertTrue(gameID > -1, "Invalid Game ID");
    }

    @Test
    @Order(5)
    @DisplayName("ListGames")
    public void testServiceListGames() {
        AuthData response = service.registerUser(userData);
        int gameID = service.createGame("gameTest");
        int gameID2 = service.createGame("gameTest2");

        Assertions.assertEquals(2, service.listGames().size(), "Incorrect number of games");
    }

    @Test
    @Order(6)
    @DisplayName("JoinGame")
    public void testServiceJoinGame() {
        UserData userData1 = new UserData("usernameTest1", "usernameTest", "email@test.com");
        AuthData response = service.registerUser(userData);
        AuthData response2 = service.registerUser(userData1);

        int gameID = service.createGame("gameTest");
        JoinGameData joinGameData = new JoinGameData("WHITE", gameID);
        JoinGameData joinGameData1 = new JoinGameData("BLACK", gameID);

        service.joinGame(response.getAuthToken(), joinGameData);
        service.joinGame(response2.getAuthToken(), joinGameData1);
        Assertions.assertEquals(response.getUsername(), service.listGames().getFirst().getWhiteUsername(), "White user not added");
        Assertions.assertEquals(response2.getUsername(), service.listGames().getFirst().getBlackUsername(), "Black user not added");
        Assertions.assertThrows(AlreadyTakenException.class, () -> service.joinGame(response.getAuthToken(), joinGameData1),
                "Did not get already taken error");
        Assertions.assertThrows(AlreadyTakenException.class, () -> service.joinGame(response2.getAuthToken(), joinGameData),
                "Did not get already taken error");

        JoinGameData badGameData = new JoinGameData("ORANGE", gameID);
        JoinGameData badGameID = new JoinGameData("WHITE", 52);
        Assertions.assertThrows(BadRequestException.class, () -> service.joinGame(response.getAuthToken(), badGameData), "Did not get bad request");
        Assertions.assertThrows(BadRequestException.class, () -> service.joinGame(response.getAuthToken(), badGameID), "Did not get bad request");
    }

    @Test
    @Order(7)
    @DisplayName("AuthorizationTest")
    public void testServiceAuthorization() {
        AuthData response = service.registerUser(userData);
        Assertions.assertTrue(service.authorized(response.getAuthToken()), "Gave Unauthorized when authorized");
        Assertions.assertFalse(service.authorized("cow"), "Gave Unauthorized when authorized");
    }

    @Test
    @Order(8)
    @DisplayName("JoinGameData")
    public void testJoinGameData() {
        JoinGameData badID = new JoinGameData("WHITE", -4);
        JoinGameData badColor = new JoinGameData("BLUE", 4);

        Assertions.assertFalse(badID.validData(), "Bad ID returned valid");
        Assertions.assertFalse(badColor.validData(), "Bad color returned valid");
    }

    @Test
    @Order(9)
    @DisplayName("LoginData")
    public void testLoginData() {
        LoginData badUsername = new LoginData("", "pass");
        LoginData badUsername2 = new LoginData(null, "pass");
        LoginData badPassword = new LoginData("username", "");
        LoginData badPassword2 = new LoginData("username", null);

        Assertions.assertFalse(badUsername.validData(), "Invalid loginData");
        Assertions.assertFalse(badUsername2.validData(), "Invalid loginData");
        Assertions.assertFalse(badPassword.validData(), "Invalid loginData");
        Assertions.assertFalse(badPassword2.validData(), "Invalid loginData");
    }
}
