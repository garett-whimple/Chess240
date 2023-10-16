package Server.Services;

import Server.Models.Game;
import Server.Models.Responses.GameResponse;
import Server.Models.Responses.MessageResponse;
import Server.Models.Responses.ListGameResponse;
import chess.ChessGame;

/**
 * Class that deals with any Game server functionality
 */
public class JoinGameService {
    /**
     * Constructor that creates a JoinGameService Object
     */
    public JoinGameService() {
    }

    /**
     * Returns a list of all current Games
     * @return ListGameResponse
     */
    public ListGameResponse ListGames(){
        return null;
    }

    /**
     * Creates a new game
     * @return GameResponse
     */
    public GameResponse createGame(){
        return null;
    }

    /**
     * Add a user to a given game
     * @param game
     * @param color
     * @return
     */
    public MessageResponse joinGame(Game game, ChessGame.TeamColor color){
        return null;
    }
}
