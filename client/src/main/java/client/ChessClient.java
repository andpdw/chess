package client;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import model.*;

import java.util.*;

public class ChessClient {

    private ServerFacade server;
    private String authToken;
    private Boolean loggedIn = Boolean.FALSE;
    private Map<Integer, Integer> gameIdMapping;
    private Integer clientGameIdCount = 1;
    private Boolean scannedGames;
    String defaultColors = "\u001b[32;40m";

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        gameIdMapping = new HashMap<>();
        scannedGames = false;
    }

    public void run() {
        System.out.print(defaultColors);
        System.out.println("Welcome to my chess game! Sign in or register to start, or type \"help\" for more options.");

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
                    System.out.println("DB has been cleared");
                } else if (command.equals("register")) {
                    register(params);
                } else if (command.equals("login")) {
                    login(params);
                } else if (command.equals("logout")) {
                    logout();
                } else if (command.equals("create")) {
                    createGame(params);
                } else if (command.equals("list")) {
                    listGames();
                } else if (command.equals("join")) {
                    joinGame(params);
                } else if (command.equals("observe")) {
                    observeGame(params);
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
        String commandColor = "\u001b[32;40m";
        String descriptionColor = "\u001b[31;40m";
        if (loggedIn) {
            System.out.print("" +
                    commandColor + "\thelp " + descriptionColor + "- list possible commands\n" +
                    commandColor + "\tlogout " + descriptionColor + "- logout of client\n" +
                    commandColor + "\tcreate <NAME> " + descriptionColor + "- create a game\n" +
                    commandColor + "\tlist " + descriptionColor + "- lists current games\n" +
                    commandColor + "\tjoin <ID> <WHITE|BLACK> " + descriptionColor + "- join game with id and color\n" +
                    commandColor + "\tobserve <ID> " + descriptionColor + "- observe game with id\n" +
                    defaultColors);
        } else {
            System.out.print("" +
                    commandColor + "\thelp " + descriptionColor + "- list possible commands\n" +
                    commandColor + "\tquit " + descriptionColor + "- exit the program\n" +
                    commandColor + "\tlogin <USERNAME> <PASSWORD> " + descriptionColor + "- login to user\n" +
                    commandColor + "\tregister <USERNAME> <PASSWORD> <EMAIL> " + descriptionColor + "- register a new account\n" +
                    defaultColors);
        }
    }

    public void register(String... params) {
        isLoggedOut();
        if (params.length != 3) {
            throw new InvalidParameters("Incorrect number of parameters, type \"help\" for more information");
        }

        try {
            UserData userData = new UserData(params[0], params[1], params[2]);
            AuthData authData = server.registerUser(userData);
            authToken = authData.getAuthToken();
            loggedIn = Boolean.TRUE;
            System.out.println("Registration successful. Logged in as " + params[0]);
        } catch (ResponseException e) {
            System.out.println(errorJsonToString(e.getMessage()));
        }
    }

    public void login(String... params) {
        isLoggedOut();
        if (params.length != 2) {
            throw new InvalidParameters("Incorrect number of parameters, type \"help\" for more information");
        }

        try {
            LoginData loginData = new LoginData(params[0], params[1]);
            AuthData authData = server.loginUser(loginData);
            authToken = authData.getAuthToken();
            loggedIn = Boolean.TRUE;
            System.out.println("Logged in as " + params[0]);
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

    public void createGame(String... params) {
        isLoggedIn();
        if (params.length != 1) {
            throw new InvalidParameters("Incorrect number of parameters, type \"help\" for more information");
        }

        try {
            Integer gameID = server.createGame(params[0], authToken);
            gameIdMapping.put(clientGameIdCount, gameID);
            System.out.println("Game was created successfully, type list to get gameID");
            clientGameIdCount++;
        } catch (ResponseException e) {
            System.out.println(e.getMessage());
        }
    }

    public void listGames() {
        isLoggedIn();

        try {
            List<GameData> gameDataList = server.listGames(authToken);
            if (gameDataList.isEmpty()) {
                System.out.println("There are currently no games in the database. Use create command to make new game.");
                return;
            }
            List<Integer> foundGameIds = new ArrayList<>();
            for (Integer key : gameIdMapping.keySet()) {
                for (GameData game : gameDataList) {
                    if (gameIdMapping.get(key) == game.getID()) {
                        System.out.println(showGameData(key, game));
                        foundGameIds.add(game.getID());
                        break;
                    }
                }
            }
            if (foundGameIds.size() != gameDataList.size()) {
                for (GameData game : gameDataList) {
                    if (!foundGameIds.contains(game.getID())) {
                        gameIdMapping.put(clientGameIdCount, game.getID());
                        System.out.println(showGameData(clientGameIdCount, game));
                        clientGameIdCount++;
                    }
                }
            }
            scannedGames = true;
        } catch (ResponseException e) {
            System.out.println(e.getMessage());
        }
    }

    public void joinGame(String... params) {
        isLoggedIn();
        if (!scannedGames) {
            throw new InvalidParameters("Must run list games before joining a game");
        }
        if (params.length != 2) {
            throw new InvalidParameters("Incorrect number of parameters, type \"help\" for more information");
        }
        int publicGameId;
        try {
            publicGameId = Integer.parseInt(params[0]);
        } catch (Exception e) {
            throw new InvalidParameters("Game ID must be a int, please try again");
        }
        Integer gameID = gameIdMapping.get(publicGameId);
        if (gameID == null) {
            throw new InvalidParameters("Could not find a game with that ID");
        }

        try {
            String color = null;
            if (Objects.equals(params[1], "white")) {
                color = "WHITE";
            } else if (Objects.equals(params[1], "black")) {
                color = "BLACK";
            }
            JoinGameData joinGameData = new JoinGameData(color, gameID);
            server.joinGame(joinGameData, authToken);
            List<GameData> gameDataList = server.listGames(authToken);
            GameData gameData = null;
            for (GameData game : gameDataList) {
                if (game.getID() == gameIdMapping.get(gameID)) {
                    gameData = game;
                    break;
                }
            }
            System.out.println("Joining the game");
            System.out.println(displayBoard(color, gameData));
        } catch (ResponseException e) {
            System.out.println(errorJsonToString(e.getMessage()));
        }
    }

    public void observeGame(String... params) {
        isLoggedIn();
        if (!scannedGames) {
            throw new InvalidParameters("Must run list games before joining a game");
        }
        if (params.length != 1) {
            throw new InvalidParameters("Incorrect number of parameters, type \"help\" for more information");
        }

        int publicGameId;
        try {
            publicGameId = Integer.parseInt(params[0]);
        } catch (Exception e) {
            throw new InvalidParameters("Game ID must be a int, please try again");
        }
        Integer internalGameId = gameIdMapping.get(publicGameId);

        try {
            List<GameData> gameDataList = server.listGames(authToken);
            for (GameData game : gameDataList) {
                if (internalGameId == game.getID()) {
                    System.out.println("Observing the following game:");
                    System.out.println(displayBoard(null, game));
                }
            }
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

    public String showGameData(Integer visibleID, GameData gameData) {
        String out = "";
        String whiteUser = gameData.getWhiteUsername();
        String blackUser = gameData.getBlackUsername();
        if (whiteUser == null) {
            whiteUser = "None";
        }
        if (blackUser == null) {
            blackUser = "None";
        }
        out += "Game ID: " + visibleID + "\nGame Name: " + gameData.getGameName() + "\n" +
                "\tWhite Player: " + whiteUser + "\n" +
                "\tBlack Player: " + blackUser + "\n";
        return out;
    }

    public String displayBoard(String playerColor, GameData gameData) {
        StringBuilder output = new StringBuilder();
        Integer count = 1;
        Integer countDir = 1;
        List<String> colVal = new ArrayList<>(List.of("h", "g", "f", "e", "d", "c", "b", "a"));
        String boardColor = "\u001b[30;47;1m";
        String endColorCode = "\u001b[0m";
        BoardSpace currentSpace = BoardSpace.LIGHT;

        if (playerColor == null || playerColor.equals("WHITE")) {
            count = 8;
            countDir = -1;
            Collections.reverse(colVal);
        }

        //Top row
        output.append(boardColor);
        output.append(setSpace(" "));
        for (String col : colVal) {
            output.append(setSpace(col));
        }
        output.append(setSpace(" "));
        output.append(endColorCode);
        output.append("\n");

        for (int y = count; (y > 0 && y < 9); y += countDir) {
            for (int x = 0; x < 10; x++) {
                if (x == 0 || x == 9) {
                    output.append(boardColor);
                    output.append(setSpace(String.valueOf(y)));
                    output.append(endColorCode);
                } else {
                    ChessPosition pos = new ChessPosition(y, x);
                    ChessPiece piece = gameData.getGame().getBoard().getPiece(pos);
                    output.append(setBoardSpace(piece, currentSpace));
                    currentSpace = swapSpace(currentSpace);
                }
            }
            output.append("\n");
            currentSpace = swapSpace(currentSpace);
        }

        //Bottom Row
        output.append(boardColor);
        output.append(setSpace(" "));
        for (String col : colVal) {
            output.append(setSpace(col));
        }
        output.append(setSpace(" "));
        output.append("\n");
        output.append(defaultColors);

        return output.toString();
    }

    public String setSpace(String val) {
        return " " + val + " ";
    }

    public String setBoardSpace(ChessPiece piece, BoardSpace space) {
        String escapeSeq = "\u001b[";
        String blackForeground = "32;";
        String whiteForeground = "34;";
        String darkBackground = "40";
        String lightBackground = "100";

        String output = escapeSeq;

        if (piece == null || piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            output += whiteForeground;
        } else {
            output += blackForeground;
        }

        if (space == BoardSpace.LIGHT) {
            output += lightBackground + "m";
        } else {
            output += darkBackground + "m";
        }

        if (piece == null) {
            output += "   ";
        } else {
            output += " " + piece + " ";
        }
        return output;
    }

    public BoardSpace swapSpace(BoardSpace current) {
        if (current == BoardSpace.LIGHT) {
            return BoardSpace.DARK;
        } else {
            return BoardSpace.LIGHT;
        }
    }

    public enum BoardSpace {LIGHT, DARK}

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
