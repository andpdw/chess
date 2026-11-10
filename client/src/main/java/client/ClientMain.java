package client;

import chess.*;

public class ClientMain {
    public static void main(String[] args) {
        String serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        try {
            new ChessClient(serverUrl).run();
        } catch (Exception e) {
            System.out.println("Unable to start server, got error: " + e.getMessage());
        }
    }
}
