package model;

import chess.ChessGame;

public class GameData {
    private static int gameID;
    private static String whiteUserName;
    private static String blackUserName;
    private static String gameName;
    private static ChessGame game;

    public GameData(int ID, String name) {
        gameName = name;
        gameID = ID;
        game = new ChessGame();
    }

    public int getID() {
        return gameID;
    }

    public String getGameName() {
        return gameName;
    }

    public ChessGame getGame() {
        return game;
    }

    public void setBlackUserName(String userName) {
        if (blackUserName != null || !blackUserName.isBlank()) {
            throw new RuntimeException("Black User Already Set");
        } else {
            blackUserName = userName;
        }
    }

    public void setWhiteUserName(String userName) {
        if (whiteUserName != null || !whiteUserName.isBlank()) {
            throw new RuntimeException("Black User Already Set");
        } else {
            whiteUserName = userName;
        }
    }
}
