package model;

import chess.ChessGame;

public class GameData {
    private int gameID;
    private String whiteUserName;
    private String blackUserName;
    private String gameName;
    private ChessGame game;

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
