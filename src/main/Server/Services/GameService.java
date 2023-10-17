package Server.Services;

import Server.Models.Game;
import Server.Services.Requests.JoinGameRequest;
import Server.Services.Responses.GameResponse;
import Server.Services.Responses.MessageResponse;
import Server.Services.Responses.ListGameResponse;

/**
 * Class that deals with any Game server functionality
 */
public class GameService {
    /**
     * Constructor that creates a GameService Object
     */
    public GameService() {
    }

    /**
     * Returns a list of all current Games
     * @return ListGameResponse
     */
    public ListGameResponse ListGames(){
        return null;
    }

    /**
     * Creates a new game with any amount of information
     * @param game Game Object that is used to set all game fields
     * @return GameResponse
     */
    public GameResponse createGame(Game game){
        return null;
    }

    /**
     * Add a user to a given game
     * @param joinGameRequest has fields gameId, username, and color
     * @return MessageResponse
     */
    public MessageResponse joinGame(JoinGameRequest joinGameRequest){
        return null;
    }
}