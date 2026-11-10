package client;

import model.AuthData;
import model.UserData;
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

}
