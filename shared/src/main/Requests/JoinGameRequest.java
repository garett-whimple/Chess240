package Requests;

import chess.ChessGame;

/**
 * Request Class for joining a game
 */
public class JoinGameRequest {
    /**
     * ID of the game that is trying to be joined
     */
    Integer gameID;
    /**
     * Username of the user joining
     */
    String username;
    /**
     * Color the user will join as
     */
    ChessGame.TeamColor playerColor;

    /**
     * Constructor that creates a JoinGameRequest Object
     * @param gameID ID of the game that is trying to be joined
     * @param username Username of the user joining
     * @param playerColor Color the user will join as
     */
    public JoinGameRequest(int gameID, String username, ChessGame.TeamColor playerColor) {
        this.gameID = gameID;
        this.username = username;
        this.playerColor = playerColor;
    }

    public Integer getGameID() {
        return gameID;
    }

    public String getUsername() {
        return username;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
