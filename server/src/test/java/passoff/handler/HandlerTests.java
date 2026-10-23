package passoff.handler;

import handler.HandlerClass;
import handler.UnauthorizedException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.JoinGameData;
import service.LoginData;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HandlerTests {
    private static HandlerClass handler;
    private static UserData userData;
    private static AuthData authData;
    private static GameData gameData;
    private static LoginData loginData;
    private static JoinGameData joinGameData;

    @BeforeAll
    public static void init() {
        handler = new HandlerClass();
    }

    @BeforeEach
    public void setup() {
        handler.clear();
        userData = new UserData("usernameTest", "passwordTest", "test@email.com");
        authData = new AuthData("usernameTest");
        gameData = new GameData(13, "gameTest");
        loginData = new LoginData("usernameTest", "passwordTest");
        joinGameData = new JoinGameData("WHITE", 13);
    }

    @Test
    @Order(1)
    @DisplayName("RegisterUser")
    public void testHandlerRegisterUser() {
        AuthData response = handler.registerUser(userData);
        Assertions.assertNotNull(response, "No Auth Token Given");
        Assertions.assertThrows(AlreadyTakenException.class, () -> handler.registerUser(userData), "Did not throw an error for duplciate username");
    }

    @Test
    @Order(2)
    @DisplayName("LogoutUser")
    public void testHandlerLogoutUser() {
        AuthData response = handler.registerUser(userData);
        handler.logoutUser(response.getAuthToken());

        Assertions.assertThrows(UnauthorizedException.class, () -> handler.logoutUser(response.getAuthToken()), "Did not get Unauthorized");
        Assertions.assertThrows(UnauthorizedException.class, () -> handler.listGames(response.getAuthToken()), "Was able to access data while logged out");
    }

    @Test
    @Order(3)
    @DisplayName("LoginUser")
    public void testHandlerLoginUser() {
        AuthData response = handler.registerUser(userData);
        handler.logoutUser(response.getAuthToken());
        AuthData loginResponse = handler.loginUser(loginData);

        Assertions.assertNotNull(loginResponse, "Did not get AuthData after logging in");
    }

    @Test
    @Order(4)
    @DisplayName("CreateGame")
    public void testHandlerCreateGame() {
        AuthData response = handler.registerUser(userData);

        Assertions.assertThrows(UnauthorizedException.class, () -> handler.createGame(authData.getAuthToken(), "invalid"),
                "Created game without authorization");
        Assertions.assertTrue(-1 < handler.createGame(response.getAuthToken(), "gameTest"), "Bad Game ID");
    }

    @Test
    @Order(5)
    @DisplayName("ListGames")
    public void testHandlerListGames() {
        AuthData response = handler.registerUser(userData);

        Assertions.assertThrows(UnauthorizedException.class, () -> handler.listGames(authData.getAuthToken()), "Able to access with bad AuthToken");

        handler.createGame(response.getAuthToken(), "gameTest1");
        Assertions.assertNotNull(handler.listGames(response.getAuthToken()), "Did not get a list");
    }

    @Test
    @Order(6)
    @DisplayName("JoinGame")
    public void testHandlerJoinGame() {
        AuthData response = handler.registerUser(userData);

        Assertions.assertThrows(UnauthorizedException.class, () -> handler.joinGame(authData.getAuthToken(), joinGameData),
                "Able to access with bad AuthToken");

        int gameID = handler.createGame(response.getAuthToken(), "gameTest");
        JoinGameData newGameData = new JoinGameData("WHITE", gameID);
        handler.joinGame(response.getAuthToken(), newGameData);

        Assertions.assertEquals(response.getUsername(), handler.listGames(response.getAuthToken()).getFirst().getWhiteUsername(),
                "White user not added");
    }

}
