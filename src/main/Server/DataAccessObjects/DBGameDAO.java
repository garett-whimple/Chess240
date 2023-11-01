package Server.DataAccessObjects;

import Server.Models.Game;
import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class DBGameDAO {
    Database db;
    public Game find(int id) throws DataAccessException {
        Connection connection = db.getConnection();
        Game returnGame = null;
        try {
            try (var preparedStatement = connection.prepareStatement("SELECT * FROM Game WHERE gameId = ?")) {
                preparedStatement.setInt(1, id);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        String whiteUsername = rs.getString("whiteUserName");
                        String blackUsername = rs.getString("blackUserName");
                        String gameName = rs.getString("gameName");
                        String gameBoardString = rs.getString("game");
                        Gson gson = new Gson();
                        ChessGame gameBoard = (ChessGame)gson.fromJson(gameBoardString, ChessGame.class);
                        returnGame = new Game(id,whiteUsername, blackUsername, gameName, gameBoard);
                    }
                }
            }
            return returnGame;
        }
        catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            db.returnConnection(connection);
        }
    }

    public Collection<Game> findAll() throws DataAccessException {
        Connection connection = db.getConnection();
        ArrayList<Game> returnGameArray = new ArrayList<>();
        try {
            try (var preparedStatement = connection.prepareStatement("SELECT * FROM Game")) {
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        int gameId = rs.getInt("gameId");
                        String whiteUsername = rs.getString("whiteUserName");
                        String blackUsername = rs.getString("blackUserName");
                        String gameName = rs.getString("gameName");
                        String gameBoardString = rs.getString("game");
                        Gson gson = new Gson();
                        ChessGame gameBoard = (ChessGame)gson.fromJson(gameBoardString, ChessGame.class);
                        returnGameArray.add(new Game(gameId,whiteUsername, blackUsername, gameName, gameBoard));
                    }
                }
            }
            return returnGameArray;
        }
        catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            db.returnConnection(connection);
        }
    }

    public int insert(Game game) throws DataAccessException{
        Connection connection = db.getConnection();
        Integer returnInt = null;
        try {
            try (var preparedStatement = connection.prepareStatement("INSERT INTO Game (whiteUserName, blackUserName, gameName, game) VALUE(?, ?)" )) {
                preparedStatement.setString(1, game.getWhiteUsername());
                preparedStatement.setString(2, game.getBlackUsername());
                preparedStatement.setString(3, game.getGameName());
                Gson gson = new Gson();
                String gameString = gson.toJson(game.getGame());
                preparedStatement.setString(4, gameString);
                preparedStatement.executeUpdate();
                var resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    returnInt = resultSet.getInt(1); //USED FOR GETTING BACK GENERATED ID
                }
            }
            return returnInt;
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
            try (var preparedStatement = connection.prepareStatement("TRUNCATE TABLE Game" )) {
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            db.returnConnection(connection);
        }
    }

    public void remove(int id) throws DataAccessException{
        Connection connection = db.getConnection();
        try {
            try (var preparedStatement = connection.prepareStatement("DELETE FROM Game WHERE gameId = ?" )) {
                preparedStatement.setInt(1,id);
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            db.returnConnection(connection);
        }
    }

    public void update(Game game) throws DataAccessException{
        Connection connection = db.getConnection();
        try {
            try (var preparedStatement = connection.prepareStatement("UPDATE Game SET whiteUserName = ?, blackUserName = ?, gameName = ?, game = ?, WHERE gameId = ?" )) {
                preparedStatement.setString(1, game.getWhiteUsername());
                preparedStatement.setString(2, game.getBlackUsername());
                preparedStatement.setString(3, game.getGameName());
                Gson gson = new Gson();
                String gameString = gson.toJson(game.getGame());
                preparedStatement.setString(4, gameString);
                preparedStatement.setInt(5, game.getGameId());
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
