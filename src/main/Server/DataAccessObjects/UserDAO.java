package Server.DataAccessObjects;

import Server.Models.User;
import dataAccess.DataAccessException;

import javax.xml.crypto.Data;
import java.util.Collection;

public class UserDAO {

    /**
     * Returns the user with the given username
     * @param username
     * @return
     * @throws DataAccessException
     */
    public User find(String username) throws DataAccessException {
        return null;
    }

    /**
     * Returns all of the users
     * @return
     * @throws DataAccessException
     */
    public Collection<User> findAll() throws DataAccessException {
        return null;
    }

    /**
     * Removes a user with the given username
     * @param username
     * @throws DataAccessException
     */
    public void remove(String username) throws DataAccessException {

    }

    /**
     * clears all users from the database
     * @throws DataAccessException
     */
    public void clear() throws DataAccessException {

    }

    /**
     * Inserts the given user into the database
     * @param user
     * @throws DataAccessException
     */
    public void insert(User user) throws DataAccessException {

    }

    /**
     * Updates the given user in the database
     * @param user
     * @throws DataAccessException
     */
    public void update(User user) throws DataAccessException {

    }
}
