package Server.Models.Responses;

/**
 * Response Object that holds information for a Game
 */
public class GameResponse {
    /**
     * Is the error message of the response if there is one
     */
    String message;
    /**
     * Is the return Code of the response
     */
    int returnCode;
    /**
     * ID of the game
     */
    int gameID;

    /**
     * Constructor to make a GameResponse Object
     * @param message Is the error message of the response if there is one
     * @param returnCode Is the return Code of the response
     * @param gameID ID of the game
     */
    public GameResponse(String message, int returnCode, int gameID) {
        this.message = message;
        this.returnCode = returnCode;
        this.gameID = gameID;
    }

    /**
     * Returns the message
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the returnCode
     * @return returnCode
     */
    public int getReturnCode() {
        return returnCode;
    }

    /**
     * Returns the gameID
     * @return gameID
     */
    public int getGameID() {
        return gameID;
    }
}
