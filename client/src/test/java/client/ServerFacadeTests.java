package client;

import model.*;
import org.eclipse.jetty.util.log.Log;
import org.junit.jupiter.api.*;
import server.Server;

import java.util.ArrayList;
import java.util.List;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static int portNum;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        portNum = port;
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void testPrep() {
        serverFacade = new ServerFacade("http://localhost:" + String.valueOf(portNum));
        serverFacade.clearDatabase();
    }

    @Test
    @DisplayName("Register User")
    public void registerUser() {
        UserData userData = new UserData("user", "pass", "email");
        Assertions.assertDoesNotThrow(() -> serverFacade.registerUser(userData));
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.registerUser(userData));
    }

    @Test
    @DisplayName("Logout User")
    public void logoutUser() {
        AuthData authdata = registerDefaultUser();
        Assertions.assertDoesNotThrow(() -> serverFacade.logoutUser(authdata.getAuthToken()));
    }

    @Test
    @DisplayName("Login User")
    public void loginUser() {
        AuthData authData = registerDefaultUser();
        serverFacade.logoutUser(authData.getAuthToken());
        LoginData loginData = new LoginData("user", "pass");
        LoginData badData = new LoginData("badUser", "pass");
        Assertions.assertDoesNotThrow(() -> serverFacade.loginUser(loginData));
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.loginUser(badData));
    }

    @Test
    @DisplayName("Create Game")
    public void createGame() {
        AuthData authData = registerDefaultUser();
        Assertions.assertDoesNotThrow(() -> serverFacade.createGame("game", authData.getAuthToken()));
    }

    @Test
    @DisplayName("List Games")
    public void listGames() {
        AuthData authData = registerDefaultUser();
        serverFacade.createGame("game", authData.getAuthToken());
        serverFacade.createGame("game2", authData.getAuthToken());
        serverFacade.createGame("game3", authData.getAuthToken());

        Assertions.assertDoesNotThrow(() -> serverFacade.listGames(authData.getAuthToken()));
        List<GameData> gameDataList = serverFacade.listGames(authData.getAuthToken());
        Assertions.assertEquals(3, gameDataList.size());
    }

    @Test
    @DisplayName("Join Game")
    public void joinGame() {
        AuthData authData = registerDefaultUser();
        serverFacade.createGame("game", authData.getAuthToken());
        GameData correctGameData = serverFacade.listGames(authData.getAuthToken()).getFirst();
        correctGameData.setBlackUserName("user");

        JoinGameData joinGameData = new JoinGameData("BLACK", correctGameData.getID());
        serverFacade.joinGame(joinGameData, authData.getAuthToken());
        GameData returnedGameData = serverFacade.listGames(authData.getAuthToken()).getFirst();

        Assertions.assertEquals(correctGameData.getBlackUsername(), returnedGameData.getBlackUsername());
    }

    @Test
    @DisplayName("Bad Server Connection")
    public void badServerTest() {
        serverFacade = new ServerFacade("http://localhost:1234");
        UserData userData = new UserData("user", "pass", "email");
        Assertions.assertThrows(ServerConnectionException.class, () -> serverFacade.registerUser(userData));
    }

    public AuthData registerDefaultUser() {
        UserData userData = new UserData("user", "pass", "email");
        return serverFacade.registerUser(userData);
    }

}
