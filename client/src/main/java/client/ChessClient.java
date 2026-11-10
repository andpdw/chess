package client;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public class ChessClient {

    private ServerFacade server;
    private String authToken;
    private Boolean loggedIn = Boolean.FALSE;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to my chess game! Sign in or register to start.");

        Scanner scanner  = new Scanner(System.in);
        var line = "";
        while (!line.equals("quit")) {

            line = scanner.nextLine();
            String[] input = line.toLowerCase().split(" ");
            String[] params = Arrays.copyOfRange(input, 1, input.length);
            String command = input[0];

            try {
                if (command.equals("clear")) {
                    clear();
                } else if (command.equals("register")) {
                    register(params);
                } else if (command.equals("logout")) {
                    logout();
                } else if (command.equals("help")) {
                    help();
                } else if (command.equals("quit")) {
                    break;
                } else {
                    System.out.println("Unknown command, please try again or type \"help\" for more options");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void help() {
        if (loggedIn) {
            System.out.print("" +
                    "\thelp\t- list possible commands\n" +
                    "\tlogout\t- logout of client\n" +
                    "\tcreate <NAME> \t- create a game\n" +
                    "\tlist\t- lists current games\n" +
                    "\tjoin <ID> <WHITE|BLACK>\t- join game with id and color\n" +
                    "\tobserve <ID>\t- observe game with id\n\n");
        } else {
            System.out.print("" +
                    "\thelp\t- list possible commands\n" +
                    "\tquit\t- exit the program\n" +
                    "\tlogin <USERNAME> <PASSWORD>\t- login to user\n" +
                    "\tregister <USERNAME> <PASSWORD> <EMAIL>\t- register a new account\n\n");
        }
    }

    public void register(String... params) {
        isLoggedOut();
        if (params.length != 3) {
            throw new InvalidParameters("Incorrect number of parameters");
        }

        try {
            UserData userData = new UserData(params[0], params[1], params[2]);
            AuthData authData = server.registerUser(userData);
            authToken = authData.getAuthToken();
            loggedIn = Boolean.TRUE;
            System.out.println("Logged in to the user " + params[0]);
        } catch (ResponseException e) {
            System.out.println(errorJsonToString(e.getMessage()));
        }
    }

    public void logout() {
        isLoggedIn();

        try {
            server.logoutUser(authToken);
            loggedIn = Boolean.FALSE;
            System.out.println("You have been logged out");
        } catch (ResponseException e) {
            System.out.println(e.getMessage());
        }
    }

    public void clear() {
        try {
            server.clearDatabase();
        } catch (ResponseException e) {
            System.out.println(e.getMessage());
        }
    }

    public void isLoggedIn() {
        if (!loggedIn) {
            throw new UnauthorizedException("You must be logged in to do that action");
        }
    }

    public void isLoggedOut() {
        if (loggedIn) {
            throw new UnauthorizedException("Action is not accessible while logged in.");
        }
    }

    public String errorJsonToString(String json) {
        Map<String, String> map  = new Gson().fromJson(json, Map.class);
        if (map.get("message") != null) {
            return map.get("message");
        } else {
            return json;
        }
    }
}
