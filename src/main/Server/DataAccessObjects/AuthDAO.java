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
     * @param authToken authToken of the User
     * @return AuthToken
     * @throws DataAccessException problems connecting to the database or fulfilling the corresponding SQL commands
     */
    public AuthToken find(String authToken) throws DataAccessException {
        return null;
    }

    /**
     * Removes a given authToken
     * @param authToken AuthToken of the User
     * @throws DataAccessException problems connecting to the database or fulfilling the corresponding SQL commands
     */
    public void remove(String authToken) throws DataAccessException {

    }

    /**
     * Clears the database of all authTokens
     * @throws DataAccessException problems connecting to the database or fulfilling the corresponding SQL commands
     */
    public void clear() throws DataAccessException {

    }

    /**
     * Inserts the given authToken into the database
     * @param authToken Object that has AuthToken and Username fields
     * @throws DataAccessException problems connecting to the database or fulfilling the corresponding SQL commands
     */
    public void insert(AuthToken authToken) throws DataAccessException {

    }
}
