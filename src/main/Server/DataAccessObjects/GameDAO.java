package Server.DataAccessObjects;

import Server.Models.Game;
import dataAccess.DataAccessException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * DAO for the game table
 */
public class GameDAO {
    private Map<Integer, Game> gameMap = new HashMap<>();
    /**
     * Constructor that creates a GameDAO Object
     */
    public GameDAO() {
    }

    /**
     * Finds a game when given the corresponding id
     * @param id id of the Game
     * @return Game
     * @throws DataAccessException problems connecting to the database or fulfilling the corresponding SQL commands
     */
    public Game find(int id) throws DataAccessException {
        return gameMap.get(id);
    }

    /**
     * Finds all the current games
     * @return Collection of Games
     * @throws DataAccessException problems connecting to the database or fulfilling the corresponding SQL commands
     */
    public Collection<Game> findAll() throws DataAccessException {
        ArrayList<Game> gameArray = new ArrayList<>();
        gameMap.forEach((key, value) -> {
            gameArray.add(value);
        });
        return gameArray;
    }

    /**
     * Removes the game with the corresponding id from the database
     * @param id id of the Game
     * @throws DataAccessException problems connecting to the database or fulfilling the corresponding SQL commands
     */
    public void remove(int id) throws DataAccessException {
        gameMap.remove(id);
    }

    /**
     * clears the database of all games
     * @throws DataAccessException problems connecting to the database or fulfilling the corresponding SQL commands
     */
    public void clear() throws DataAccessException {
        gameMap.clear();
    }

    /**
     * Inserts the given game into the database
     * @param game Object that has gameId, whiteUserName, blackUserName, gameName, and game fields
     * @throws DataAccessException problems connecting to the database or fulfilling the corresponding SQL commands
     */
    public void insert(Game game) throws DataAccessException {
        gameMap.put(game.getGameId(), game);
    }

    /**
     * Updates a game in the database
     * @param game Object that has gameId, whiteUserName, blackUserName, gameName, and game fields
     * @throws DataAccessException problems connecting to the database or fulfilling the corresponding SQL commands
     */
    public void update(Game game) throws DataAccessException {
        gameMap.put(game.getGameId(), game);
    }
}
