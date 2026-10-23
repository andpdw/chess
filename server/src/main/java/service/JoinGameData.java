package service;

import java.util.Objects;

public class JoinGameData {
    private String playerColor;
    private int gameID;
    private String username;

    public JoinGameData(String playerColorIn, int gameIDin) {
        playerColor = playerColorIn;
        gameID = gameIDin;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public int getGameID() {
        return gameID;
    }

    public boolean validData() {
        return gameID > 0 && (Objects.equals(playerColor, "WHITE") || Objects.equals(playerColor, "BLACK"));
    }
}
