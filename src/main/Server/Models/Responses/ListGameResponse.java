package Server.Models.Responses;

import Server.Models.Game;

import java.util.ArrayList;

/**
 * Response Object that holds information for a list of Games
 */
public class ListGameResponse {
    String message;
    int returnCode;
    private ArrayList<Game> games;

    /**
     * Constructor that creates a ListGameResponse Object
     * @param message
     * @param returnCode
     * @param games
     */
    public ListGameResponse(String message, int returnCode, ArrayList<Game> games) {
        this.message = message;
        this.returnCode = returnCode;
        this.games = games;
    }

    /**
     * Returns message
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns returnCode
     * @return returnCode
     */
    public int getReturnCode() {
        return returnCode;
    }

    /**
     * Returns games
     * @return games
     */
    public ArrayList<Game> getGames() {
        return games;
    }
}
