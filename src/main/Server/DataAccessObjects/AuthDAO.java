package Server.DataAccessObjects;

import Server.Models.AuthToken;
import dataAccess.DataAccessException;

import javax.xml.crypto.Data;
import java.util.Collection;

/**
 * DAO for the authToken table
 */
public class AuthDAO {
    /**
     * Constructor that creates a AuthDAO Object
     */
    public AuthDAO() {
    }

    /**
     * Finds the authToken of a given User
     * @param username
     * @return AuthToken
     * @throws DataAccessException
     */
    public AuthToken find(String username) throws DataAccessException {
        return null;
    }

    /**
     * Finds all the authTokens in the database
     * @return Collection of AuthToken
     * @throws DataAccessException
     */
    public Collection<AuthToken> findAll() throws DataAccessException {
        return null;
    }

    /**
     * Removes a given authToken
     * @param username
     * @throws DataAccessException
     */
    public void remove(String username) throws DataAccessException {

    }

    /**
     * Clears the database of all authTokens
     * @throws DataAccessException
     */
    public void clear() throws DataAccessException {

    }

    /**
     * Inserts the given authToken into the database
     * @param authToken
     * @throws DataAccessException
     */
    public void insert(AuthToken authToken) throws DataAccessException {

    }

    /**
     * Updates the given authToken in the database
     * @param authToken
     * @throws DataAccessException
     */
    public void update(AuthToken authToken) throws DataAccessException {

    }
}
