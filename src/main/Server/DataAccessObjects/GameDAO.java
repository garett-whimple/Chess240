package Server.DataAccessObjects;

import Server.Models.Game;
import dataAccess.DataAccessException;

import java.util.Collection;

public class GameDAO {
    /**
     * Finds a game when given the corresponding id
     * @param id
     * @return Game
     * @throws DataAccessException
     */
    public Game find(int id) throws DataAccessException {
        return null;
    }

    /**
     * Finds all of the current games
     * @return Collection of Games
     * @throws DataAccessException
     */
    public Collection<Game> findAll() throws DataAccessException {
        return null;
    }

    /**
     * Removes the game with the corresponding id from the database
     * @param id
     * @throws DataAccessException
     */
    public void remove(int id) throws DataAccessException {

    }

    /**
     * clears the database of all games
     * @throws DataAccessException
     */
    public void clear() throws DataAccessException {

    }

    /**
     * Inserts the given game into the database
     * @param game
     * @throws DataAccessException
     */
    public void insert(Game game) throws DataAccessException {

    }

    /**
     * Updates a game in the database
     * @param game
     * @throws DataAccessException
     */
    public void update(Game game) throws DataAccessException {

    }
}
