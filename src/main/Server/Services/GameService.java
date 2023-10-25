package Server.Services;

import Server.DataAccessObjects.GameDAO;
import Server.Models.Game;
import Server.Services.Requests.JoinGameRequest;
import Server.Services.Responses.AuthTokenResponse;
import Server.Services.Responses.GameResponse;
import Server.Services.Responses.MessageResponse;
import Server.Services.Responses.ListGameResponse;
import chess.ChessGame;
import dataAccess.DataAccessException;

import java.util.ArrayList;

/**
 * Class that deals with any Game server functionality
 */
public class GameService {
    int currentGameID = 0;
    GameDAO gameDAO;
    /**
     * Constructor that creates a GameService Object
     */
    public GameService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    /**
     * Returns a list of all current Games
     * @return ListGameResponse
     */
    public ListGameResponse listGames(){
        ListGameResponse result = null;
        try {
            ArrayList<Game> gameArray = (ArrayList<Game>) gameDAO.findAll();
            result = new ListGameResponse(null, 200, gameArray);
        } catch (DataAccessException e) {
            result = new ListGameResponse("Error: " + e.getMessage(), 500, null);
        }
        return result;
    }

    /**
     * Creates a new game with any amount of information
     * @param game Game Object that is used to set all game fields
     * @return GameResponse
     */
    public GameResponse createGame(Game game){
        GameResponse returnResponse = null;
        game.setGameId(currentGameID);
        try {
            gameDAO.insert(game);
            returnResponse = new GameResponse(null, 200, currentGameID);
            currentGameID++;
        } catch (DataAccessException e) {
            String returnMessage = "Error: " + e.getMessage();
            returnResponse =  new GameResponse(returnMessage, 500, null);
        }
        return returnResponse;
    }

    /**
     * Add a user to a given game
     * @param joinGameRequest has fields gameId, username, and color
     * @return MessageResponse
     */
    public MessageResponse joinGame(JoinGameRequest joinGameRequest){
        MessageResponse result = null;
        try {
            Game game = gameDAO.find(joinGameRequest.getGameID());
            if (joinGameRequest.getPlayerColor() == ChessGame.TeamColor.BLACK && game.getBlackUserName() != null){
                result = new MessageResponse("Error: already taken", 403);
            } else if (joinGameRequest.getPlayerColor() == ChessGame.TeamColor.WHITE && game.getWhiteUserName() != null) {
                result = new MessageResponse("Error: already taken", 403);
            } else {
                if (joinGameRequest.getPlayerColor() == ChessGame.TeamColor.BLACK) {
                    game.setBlackUserName(joinGameRequest.getUsername());
                } else if (joinGameRequest.getPlayerColor() == ChessGame.TeamColor.WHITE) {
                    game.setWhiteUserName(joinGameRequest.getUsername());
                } else {
                    //OBSERVER
                }
                gameDAO.update(game);
                result = new MessageResponse(null, 200);
            }
        } catch (DataAccessException e) {
            result = new MessageResponse("Error: " + e.getMessage(), 500);
        }
        return result;
    }
}
