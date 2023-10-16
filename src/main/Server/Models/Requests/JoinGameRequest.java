package Server.Models.Requests;

import chess.ChessGame;

/**
 * Request Class for joining a game
 */
public class JoinGameRequest {
    /**
     * ID of the game that is trying to be joined
     */
    int gameId;
    /**
     * Username of the user joining
     */
    String username;
    /**
     * Color the user will join as
     */
    ChessGame.TeamColor color;

    /**
     * Constructor that creates a JoinGameRequest Object
     * @param gameId ID of the game that is trying to be joined
     * @param username Username of the user joining
     * @param color Color the user will join as
     */
    public JoinGameRequest(int gameId, String username, ChessGame.TeamColor color) {
        this.gameId = gameId;
        this.username = username;
        this.color = color;
    }

    public int getGameId() {
        return gameId;
    }

    public String getUsername() {
        return username;
    }

    public ChessGame.TeamColor getColor() {
        return color;
    }
}
