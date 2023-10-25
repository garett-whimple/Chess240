package Server.Services.Responses;

import Server.Models.Game;

import java.util.ArrayList;

/**
 * Response Object that holds information for a list of Games
 */
public class ListGameResponse {
    /**
     * Is the error message of the response if there is one
     */
    String message;
    /**
     * Is the return Code of the response
     */
    Integer returnCode;
    /**
     * An array of game Objects
     */
    private ArrayList<Game> games;

    /**
     * Constructor that creates a ListGameResponse Object
     * @param message Is the error message of the response if there is one
     * @param returnCode Is the return Code of the response
     * @param games An array of game Objects
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
    public void setReturnCodeNull() {
        this.returnCode = null;
    }
}
