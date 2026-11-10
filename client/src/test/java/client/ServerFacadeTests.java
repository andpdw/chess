package client;

import model.AuthData;
import model.LoginData;
import model.UserData;
import org.eclipse.jetty.util.log.Log;
import org.junit.jupiter.api.*;
import server.Server;


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
        UserData userData = new UserData("user", "pass", "email");
        AuthData authdata = serverFacade.registerUser(userData);
        Assertions.assertDoesNotThrow(() -> serverFacade.logoutUser(authdata.getAuthToken()));
    }

    @Test
    @DisplayName("Login User")
    public void loginUser() {
        UserData userData = new UserData("user", "pass", "email");
        AuthData authData = serverFacade.registerUser(userData);
        serverFacade.logoutUser(authData.getAuthToken());
        LoginData loginData = new LoginData("user", "pass");
        LoginData badData = new LoginData("badUser", "pass");
        Assertions.assertDoesNotThrow(() -> serverFacade.loginUser(loginData));
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.loginUser(badData));
    }

    @Test
    @DisplayName("Create Game")
    public void createGame() {
        UserData userData = new UserData("user", "pass", "email");
        AuthData authData = serverFacade.registerUser(userData);
        Assertions.assertDoesNotThrow(() -> serverFacade.createGame("game", authData.getAuthToken()));
    }

}
