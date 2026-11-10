package client;

import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {

    private ServerFacade server;
    private String authToken;
    private Boolean loggedIn = Boolean.FALSE;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println("Welecome to my chess game! Sign in or register to start.");

        Scanner scanner  = new Scanner(System.in);
        var line = "";
        while (!line.equals("quit")) {

            line = scanner.nextLine();
            String[] input = line.toLowerCase().split(" ");
            String[] params = Arrays.copyOfRange(input, 1, input.length);
            String command = input[0];

            try {
                System.out.println("Got: " + line);

                if (command.equals("clear")) {
                    System.out.println("Cleared DB");
                } else if (command.equals("register")) {
                    register(params);
                } else if (command.equals("logout")) {
                    logout();
                }
            } catch (Exception e) {
                System.out.println("Got the error: " + e.getMessage());
            }
        }
    }

    public void register(String... params) {
        if (params.length != 3) {
            System.out.println("Invalid arguments");
            return;
        }

        UserData userData = new UserData(params[0], params[1], params[2]);
        AuthData authData = server.registerUser(userData);
        authToken = authData.getAuthToken();
        loggedIn = Boolean.TRUE;
        System.out.println("Logged in, got auth token: " + authToken);
    }

    public void logout() {
        if (!loggedIn) {
            System.out.println("Mused be logged in to log out");
            return;
        }
        System.out.println("Logged out");
        server.logoutUser(authToken);
        loggedIn = Boolean.FALSE;
    }

    public void clear() {
        server.clearDatabase();
    }
}
