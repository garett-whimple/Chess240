package Server.DataAccessObjects;

import Server.Models.AuthToken;
import com.google.gson.GsonBuilder;
import com.google.protobuf.Internal;
import dataAccess.DataAccessException;
import dataAccess.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLDataException;
import java.sql.SQLException;

public class DBAuthDAO {
    Database db;
    void makeSQLCalls() throws SQLException {
        try (var connection = getConnection()) {
            //EXECUTE SQL STATEMENT
            try (var createDbStatement = connection.prepareStatement("CREATE DATABASE IF NOT EXISTS chess")) {
                createDbStatement.executeUpdate();
            }
            connection.setCatalog("chess");

            var createAuthTable = """
                CREATE TABLE IF NOT EXISTS AuthToken (
                authToken varchar(255), 
                username varchar(255), 
                PRIMARY KEY (authToken));
                    """;
            try (var createTableStatement = connection.prepareStatement(createAuthTable)) {
                createTableStatement.executeUpdate();
            }
        }
    }

    void insert(Connection connection, AuthToken authToken) throws SQLException {
        try (var preparedStatement = connection.prepareStatement("INSERT INTO AuthToken (authToken, username) VALUE(?, ?)" )) {
            preparedStatement.setString(1,authToken.getAuthToken());
            preparedStatement.setString(2,authToken.getUsername());
            preparedStatement.executeUpdate();
            var resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                int rowID = resultSet.getInt(1); //USED FOR GETTING BACK GENERATED ID
            }
        }
    }

    void find(Connection connection, AuthToken authToken) throws SQLException {
        try (var preparedStatement = connection.prepareStatement("INSERT INTO AuthToken (authToken, username) VALUE(?, ?)" )) {
            preparedStatement.setString(1,authToken.getAuthToken());
            preparedStatement.setString(2,authToken.getUsername());
            preparedStatement.executeUpdate();
            var resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                int rowID = resultSet.getInt(1); //USED FOR GETTING BACK GENERATED ID
            }

            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    var id = rs.getInt("id");
                    var name = rs.getString("name");
                }
            }

//            var builder = new GsonBuilder();
//            builder.registerTyperAdapter(List.class, new ListAdapter());
//            var friends = builder.create().fromJson()
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306",
                "root",
                "sVv%k&swYhYG*Y7j*hT7");
    }

    public void createTable() throws DataAccessException{
        Connection connection = db.getConnection();
        try {
            try (var createDbStatement = connection.prepareStatement("CREATE DATABASE IF NOT EXISTS chess")) {
                createDbStatement.executeUpdate();
            }
            connection.setCatalog("chess");

            var createAuthTable = """
                    CREATE TABLE IF NOT EXISTS AuthToken (
                    authToken varchar(255), 
                    username varchar(255), 
                    PRIMARY KEY (authToken));
                        """;
            try (var createTableStatement = connection.prepareStatement(createAuthTable)) {
                createTableStatement.executeUpdate();
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            db.returnConnection(connection);
        }
    }

    public AuthToken find(AuthToken authToken) throws DataAccessException{
        Connection connection = db.getConnection();
        try {
            try (var preparedStatement = connection.prepareStatement("SELECT * FROM AuthToken WHERE authToken = ?" )) {
                preparedStatement.setString(1,authToken.getAuthToken());
                try (var rs = preparedStatement.executeQuery()) {
                    rs.next();
                    String authTokenString = rs.getString("authToken");
                    String username = rs.getString("username");
                    return new AuthToken(authTokenString, username);
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
