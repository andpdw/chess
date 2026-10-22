package model;

import chess.ChessGame;

public class GameData {
    private int gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private ChessGame game;

    public GameData(int Id, String name) {
        gameName = name;
        gameID = Id;
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

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public void setBlackUserName(String userName) {
        blackUsername = userName;
    }

    public void setWhiteUserName(String userName) {
        whiteUsername = userName;
    }

    public void removeBlackUserName() {
        blackUsername = "";
    }

    public void removeWhiteUserName() {
        whiteUsername = "";
    }
}
