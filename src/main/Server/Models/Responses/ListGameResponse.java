package Server.Models.Responses;

import Server.Models.Game;

import java.util.ArrayList;

public class ListGameResponse {
    String message;
    int returnCode;
    private ArrayList<Game> games;

    public ListGameResponse(String message, int returnCode, ArrayList<Game> games) {
        this.message = message;
        this.returnCode = returnCode;
        this.games = games;
    }

    public String getMessage() {
        return message;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public ArrayList<Game> getGames() {
        return games;
    }
}
