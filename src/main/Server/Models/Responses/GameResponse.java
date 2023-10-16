package Server.Models.Responses;

public class GameResponse {
    String message;
    int returnCode;
    int gameID;

    /**
     * Constructor to make a GameResponse Object
     * @param message
     * @param returnCode
     * @param gameID
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
