package Server.DataAccessObjects;

import Server.Models.AuthToken;
import dataAccess.DataAccessException;
import dataAccess.Database;
import java.sql.Connection;
import java.sql.SQLException;

public class DBAuthDAO {
    Database db;

    public DBAuthDAO(Database db) {
        this.db = db;
    }

    public AuthToken find(AuthToken authToken) throws DataAccessException{
        Connection connection = db.getConnection();
        try {
            if (authToken.getAuthToken() == null) {
                return null; //INVALID REQUEST
            }
            try (var preparedStatement = connection.prepareStatement("SELECT * FROM AuthToken WHERE authToken = ?" )) {
                preparedStatement.setString(1,authToken.getAuthToken());
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        String authTokenString = rs.getString("authToken");
                        String username = rs.getString("username");
                        return new AuthToken(authTokenString, username);
                    } else {
                        return null;
                    }
                }
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            db.returnConnection(connection);
        }
    }

    public void insert(AuthToken authToken) throws DataAccessException{
        Connection connection = db.getConnection();
        try {
            if (find(authToken) != null) {
                return; //ALREADY IN DATABASE
            }
            if (authToken.getUsername() == null || authToken.getAuthToken() == null) {
                return; //INVALID REQUEST
            }
            try (var preparedStatement = connection.prepareStatement("INSERT INTO AuthToken (authToken, username) VALUE(?, ?)" )) {
                preparedStatement.setString(1,authToken.getAuthToken());
                preparedStatement.setString(2,authToken.getUsername());
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            db.returnConnection(connection);
        }
    }

    public void clear() throws DataAccessException{
        Connection connection = db.getConnection();
        try {
            try (var preparedStatement = connection.prepareStatement("TRUNCATE TABLE AuthToken" )) {
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            db.returnConnection(connection);
        }
    }

    public void remove(AuthToken authToken) throws DataAccessException{
        Connection connection = db.getConnection();
        try {
            if (authToken.getAuthToken() == null) {
                return; //INVALID REQUEST
            }
            try (var preparedStatement = connection.prepareStatement("DELETE FROM AuthToken WHERE authToken = ?" )) {
                preparedStatement.setString(1,authToken.getAuthToken());
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            db.returnConnection(connection);
        }
    }
}
