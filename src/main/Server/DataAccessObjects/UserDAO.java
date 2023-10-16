package Server.DataAccessObjects;

import Server.Models.User;
import dataAccess.DataAccessException;

import javax.xml.crypto.Data;
import java.util.Collection;

/**
 * DAO for the User table
 */
public class UserDAO {
    /**
     * Constructor that creates a UserDAO Object
     */
    public UserDAO() {
    }

    /**
     * Returns the user with the given username
     * @param username username of the User
     * @return User
     * @throws DataAccessException problems connecting to the database or fulfilling the corresponding SQL commands
     */
    public User find(String username) throws DataAccessException {
        return null;
    }

    /**
     * clears all users from the database
     * @throws DataAccessException problems connecting to the database or fulfilling the corresponding SQL commands
     */
    public void clear() throws DataAccessException {

    }

    /**
     * Inserts the given user into the database
     * @param user Object that has username, password, and email fields
     * @throws DataAccessException problems connecting to the database or fulfilling the corresponding SQL commands
     */
    public void insert(User user) throws DataAccessException {

    }
}
