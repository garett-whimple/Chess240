package Server.DataAccessObjects;

import Models.User;
import dataAccess.DataAccessException;
import dataAccess.Database;
import java.sql.Connection;
import java.sql.SQLException;

public class UserDAO {
    Database db;

    public UserDAO(Database db) {
        this.db = db;
    }

    public User find(String username) throws DataAccessException {
        Connection connection = db.getConnection();
        User returnUser = null;
        try {
            if (username == null) {
                return null; //INVALID REQUEST
            }
            try (var preparedStatement = connection.prepareStatement("SELECT * FROM User WHERE username = ?" )) {
                preparedStatement.setString(1,username);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        String usernameString = rs.getString("username");
                        String email = rs.getString("email");
                        String password = rs.getString("password");
                        returnUser = new User(usernameString, password, email);
                    }
                }
            }
            return returnUser;
        }
        catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            db.returnConnection(connection);
        }
    }

    public void insert(User user) throws DataAccessException{
        Connection connection = db.getConnection();
        try {
            if (find(user.getUsername()) != null) {
                return; //ALREADY IN DATABASE
            }
            if (user.getUsername() == null || user.getPassword() == null) {
                return; //INVALID REQUEST
            }
            try (var preparedStatement = connection.prepareStatement("INSERT INTO User (username, password, email) VALUE(?, ?, ?)" )) {
                preparedStatement.setString(1,user.getUsername());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getEmail());
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
            try (var preparedStatement = connection.prepareStatement("TRUNCATE TABLE User" )) {
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
