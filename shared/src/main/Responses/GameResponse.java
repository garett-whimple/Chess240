package Responses;

import java.util.Objects;

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
    Integer returnCode;
    /**
     * ID of the game
     */
    Integer gameID;

    /**
     * Constructor to make a GameResponse Object
     * @param message Is the error message of the response if there is one
     * @param returnCode Is the return Code of the response
     * @param gameID ID of the game
     */
    public GameResponse(String message, int returnCode, Integer gameID) {
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
    public Integer getGameID() {
        return gameID;
    }
    public void setReturnCodeNull() {
        this.returnCode = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameResponse that)) return false;
        return Objects.equals(getMessage(), that.getMessage()) && Objects.equals(getReturnCode(), that.getReturnCode()) && Objects.equals(getGameID(), that.getGameID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMessage(), getReturnCode(), getGameID());
    }
}
