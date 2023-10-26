package Server.Services;

import Server.DataAccessObjects.GameDAO;
import Server.Models.Game;
import Server.Services.Requests.JoinGameRequest;
import Server.Services.Responses.*;
import chess.ChessGame;
import dataAccess.DataAccessException;

import java.util.ArrayList;

/**
 * Class that deals with any Game server functionality
 */
public class GameService {
    int currentGameID = 1;
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
            ArrayList<ListGameObject> listGameObjectArray = new ArrayList<>();
            for(Game game : gameArray) {
                if (game.getWhiteUsername() == null) {
                    game.setWhiteUserName(null);
                }
                if (game.getBlackUsername() == null) {
                    game.setBlackUserName(null);
                }
                ListGameObject currentListGameObject = new ListGameObject(game.getGameId(),game.getWhiteUsername(), game.getBlackUsername(),game.getGameName());
                listGameObjectArray.add(currentListGameObject);
            }
            result = new ListGameResponse(null, 200, listGameObjectArray);
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
            if (game.getGameName() == null) {
                returnResponse = new GameResponse("Error: bad request", 400, null);
            } else {
                gameDAO.insert(game);
                returnResponse = new GameResponse(null, 200, currentGameID);
                currentGameID++;
            }
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
            if (game == null) {
                result = new MessageResponse("Error: bad request", 400);
            } else if (joinGameRequest.getPlayerColor() == ChessGame.TeamColor.BLACK && game.getBlackUsername() != null){
                result = new MessageResponse("Error: already taken", 403);
            } else if (joinGameRequest.getPlayerColor() == ChessGame.TeamColor.WHITE && game.getWhiteUsername() != null) {
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
