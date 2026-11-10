package client;

import java.util.Scanner;

public class ChessClient {

    private ServerFacade server;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println("Welecome to my chess game! Sign in or register to start.");

        Scanner scanner  = new Scanner(System.in);
        var line = "";
        while (!line.equals("quit")) {

            line = scanner.nextLine();

            try {
                System.out.println("Got: " + line);
            } catch (Exception e) {
                System.out.println("Got the error: " + e.getMessage());
            }
        }
    }
}
