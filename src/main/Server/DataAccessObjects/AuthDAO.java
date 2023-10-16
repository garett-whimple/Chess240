package Server.DataAccessObjects;

import Server.Models.AuthToken;
import dataAccess.DataAccessException;

import javax.xml.crypto.Data;
import java.util.Collection;

public class AuthDAO {
    public AuthToken find(String username) throws DataAccessException {
        return null;
    }

    public Collection<AuthToken> findAll() throws DataAccessException {
        return null;
    }

    public void remove(String username) throws DataAccessException {

    }

    public void clear() throws DataAccessException {

    }

    public void insert(AuthToken authToken) throws DataAccessException {

    }

    public void update(AuthToken authToken) throws DataAccessException {

    }
}
