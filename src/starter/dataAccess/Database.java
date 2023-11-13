package dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Database is responsible for creating connections to the database. Connections are
 * managed with a simple pool in order to increase performance. To obtain and
 * use connections represented by this class use the following pattern.
 *
 * <pre>
 *  public boolean example(String selectStatement, Database db) throws DataAccessException{
 *    var conn = db.getConnection();
 *    try (var preparedStatement = conn.prepareStatement(selectStatement)) {
 *        return preparedStatement.execute();
 *    } catch (SQLException ex) {
 *        throw new DataAccessException(ex.toString());
 *    } finally {
 *        db.returnConnection(conn);
 *    }
 *  }
 * </pre>
 */
public class Database {

    // FIXME: Change these fields, if necessary, to match your database configuration
    public static final String DB_NAME = "chess";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "sVv%k&swYhYG*Y7j*hT7";
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306";
    private final LinkedList<Connection> connections = new LinkedList<>();

    public Database() {
    }

    public void Initialize() throws DataAccessException {
        try {
            Connection connection;
            connection = DriverManager.getConnection(CONNECTION_URL, DB_USERNAME, DB_PASSWORD);
            try (var createDbStatement = connection.prepareStatement("CREATE DATABASE IF NOT EXISTS chess")) {
                createDbStatement.executeUpdate();
            }
            connection.setCatalog(DB_NAME);
            var createAuthTable = """
                CREATE TABLE IF NOT EXISTS AuthToken (
                authToken varchar(255), 
                username varchar(255), 
                PRIMARY KEY (authToken));
                    """;
            try (var createTableStatement = connection.prepareStatement(createAuthTable)) {
                createTableStatement.executeUpdate();
            }
            createAuthTable = """
                CREATE TABLE IF NOT EXISTS Game (
                gameId int not null AUTO_INCREMENT, 
                whiteUserName varchar(255), 
                blackUserName varchar(255), 
                gameName varchar(255), 
                game TEXT, 
                PRIMARY KEY (gameId));
                    """;
            try (var createTableStatement = connection.prepareStatement(createAuthTable)) {
                createTableStatement.executeUpdate();
            }
            createAuthTable = """
                CREATE TABLE IF NOT EXISTS User (
                username varchar(255), 
                email varchar(255), 
                password varchar(255), 
                PRIMARY KEY (username));
                    """;
            try (var createTableStatement = connection.prepareStatement(createAuthTable)) {
                createTableStatement.executeUpdate();
            }
            returnConnection(connection);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Get a connection to the database. This pulls a connection out of a simple
     * pool implementation. The connection must be returned to the pool after
     * you are done with it by calling {@link #returnConnection(Connection) returnConnection}.
     *
     * @return Connection
     */
    synchronized public Connection getConnection() throws DataAccessException {
        try {
            Connection connection;
            if (connections.isEmpty()) {
                connection = DriverManager.getConnection(CONNECTION_URL, DB_USERNAME, DB_PASSWORD);
                connection.setCatalog(DB_NAME);
            } else {
                connection = connections.removeFirst();
            }
            return connection;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Return a previously acquired connection to the pool.
     *
     * @param connection previous obtained by calling {@link #getConnection() getConnection}.
     */
    synchronized public void returnConnection(Connection connection) {
        connections.add(connection);
    }
}