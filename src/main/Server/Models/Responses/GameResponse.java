package Server.Models.Responses;

public class GameResponse {
    String message;
    int returnCode;
    int gameID;

    public GameResponse(String message, int returnCode, int gameID) {
        this.message = message;
        this.returnCode = returnCode;
        this.gameID = gameID;
    }

    public String getMessage() {
        return message;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public int getGameID() {
        return gameID;
    }
}
