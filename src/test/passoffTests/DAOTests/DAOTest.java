package passoffTests.DAOTests;

import ChessImpl.ChessGameImpl;
import Server.DataAccessObjects.AuthDAO;
import Server.DataAccessObjects.GameDAO;
import Server.DataAccessObjects.UserDAO;
import Models.AuthToken;
import Models.Game;
import Models.User;
import dataAccess.DataAccessException;
import dataAccess.Database;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collection;

public class DAOTest {
    static Database db = new Database();
    AuthToken newAuth = new AuthToken("TestAuth", "TestUser");
    User newUser = new User("TestUsername", "Password123", "TestEmail");

    static Game newGame = new Game(null, "WHTIE_USERNAME", "BLACK_USERNAME", "GAME_NAME", new ChessGameImpl());
    GameDAO gameDAO = new GameDAO(db);
    AuthDAO authDAO = new AuthDAO(db);
    UserDAO userDAO = new UserDAO(db);
//    @BeforeAll
//    public static void setupDB() throws DataAccessException{
//        try {
//            db = new Database();
//            newGame.getGame().getBoard().resetBoard();
//        } catch (DataAccessException e) {
//            throw new DataAccessException(e.getMessage());
//        }
//    }
    @BeforeEach
    public void reset() throws DataAccessException{
        try {
            authDAO.clear();
            userDAO.clear();
            gameDAO.clear();
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void validAuthInsert() throws DataAccessException{
        AuthToken expectedResponse = new AuthToken("TestAuth", "TestUser");
        try {
            authDAO.insert(newAuth);
            AuthToken response = authDAO.find(newAuth.getAuthToken());
            Assertions.assertEquals(expectedResponse, response);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void invalidAuthInsert() throws DataAccessException{
        AuthToken expectedResponse = null;
        try {
            authDAO.insert(new AuthToken("EMPTY USERNAME", null));
            AuthToken response = authDAO.find("EMPTY USERNAME");
            Assertions.assertEquals(expectedResponse, response);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void validAuthFind() throws DataAccessException{
        AuthToken expectedResponse = new AuthToken("TestAuth", "TestUser");
        try {
            authDAO.insert(newAuth);
            AuthToken response = authDAO.find(newAuth.getAuthToken());
            Assertions.assertEquals(expectedResponse, response);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void invalidAuthFind() throws DataAccessException{
        AuthToken expectedResponse = null;
        try {
            AuthToken response = authDAO.find(newAuth.getAuthToken());
            Assertions.assertEquals(expectedResponse, response);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void validAuthClear() throws DataAccessException{
        AuthToken expectedResponse = null;
        try {
            authDAO.insert(newAuth);
            authDAO.clear();
            AuthToken response = authDAO.find(newAuth.getAuthToken());
            Assertions.assertEquals(expectedResponse, response);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void validAuthRemove() throws DataAccessException{
        AuthToken expectedResponse = null;
        try {
            authDAO.insert(newAuth);
            authDAO.remove(newAuth);
            AuthToken response = authDAO.find(newAuth.getAuthToken());
            Assertions.assertEquals(expectedResponse, response);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void invalidAuthRemove() throws DataAccessException{
        AuthToken expectedResponse = newAuth;
        try {
            authDAO.insert(newAuth);
            authDAO.remove(new AuthToken("WRONG AUTHTOKEN", newAuth.getUsername()));
            AuthToken response = authDAO.find(newAuth.getAuthToken());
            Assertions.assertEquals(expectedResponse, response);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void validGameInsert() throws DataAccessException{
        Game expectedResponse = new Game(1, newGame.getWhiteUsername(), newGame.getBlackUsername(), newGame.getGameName(), newGame.getGame());
        try {
            int gameID = gameDAO.insert(newGame);
            Game response = gameDAO.find(gameID);
            Assertions.assertEquals(expectedResponse, response);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void invalidGameInsert() throws DataAccessException{
        Integer expectedResponse = null;
        try {
            int id = gameDAO.insert(newGame);
            Integer response = gameDAO.insert(new Game(id, newGame.getWhiteUsername(),newGame.getBlackUsername(),newGame.getGameName(), newGame.getGame()));
            Assertions.assertEquals(expectedResponse, response);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void validGameUpdate() throws DataAccessException{
        Game expectedResponse = new Game(1, "NEW USERNAME", newGame.getBlackUsername(), newGame.getGameName(), newGame.getGame());
        try {
            int gameID = gameDAO.insert(newGame);
            gameDAO.update(new Game(gameID, "NEW USERNAME", newGame.getBlackUsername(), newGame.getGameName(), newGame.getGame()));
            Game response = gameDAO.find(gameID);
            Assertions.assertEquals(expectedResponse, response);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void invalidGameUpdate() throws DataAccessException{
        Game expectedResponse = new Game(1, newGame.getWhiteUsername(), newGame.getBlackUsername(), newGame.getGameName(), newGame.getGame());
        try {
            int gameID = gameDAO.insert(newGame);
            gameDAO.update(new Game(3, "NEW USERNAME", newGame.getBlackUsername(), newGame.getGameName(), newGame.getGame()));
            Game response = gameDAO.find(gameID);
            Assertions.assertEquals(expectedResponse, response);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void validGameFind() throws DataAccessException{
        Game expectedResponse = new Game(1, newGame.getWhiteUsername(), newGame.getBlackUsername(), newGame.getGameName(), newGame.getGame());
        try {
            Integer id = gameDAO.insert(newGame);
            Game response = gameDAO.find(id);
            Assertions.assertEquals(expectedResponse, response);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void invalidGameFind() throws DataAccessException{
        Game expectedResponse = null;
        try {
            Game response = gameDAO.find(null);
            Assertions.assertEquals(expectedResponse, response);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void validGameFindAll() throws DataAccessException{
        Collection<Game> expectedResponse = new ArrayList<>();
        expectedResponse.add(new Game(1, newGame.getWhiteUsername(), newGame.getBlackUsername(), newGame.getGameName(), newGame.getGame()));
        expectedResponse.add(new Game(2, newGame.getWhiteUsername(), newGame.getBlackUsername(), newGame.getGameName(), newGame.getGame()));
        try {
            gameDAO.insert(newGame);
            gameDAO.insert(newGame);
            Collection<Game> response = gameDAO.findAll();
            Assertions.assertEquals(expectedResponse, response);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void invalidGameFindAll() throws DataAccessException{
        Collection<Game> expectedResponse = new ArrayList<>();
        try {
            Collection<Game> response = gameDAO.findAll();
            Assertions.assertEquals(expectedResponse, response);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void validGameClear() throws DataAccessException{
        Game expectedResponse = null;
        try {
            int id = gameDAO.insert(newGame);
            gameDAO.clear();
            Game response = gameDAO.find(id);
            Assertions.assertEquals(expectedResponse, response);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void validGameRemove() throws DataAccessException{
        Game expectedResponse = null;
        try {
            int id = gameDAO.insert(newGame);
            gameDAO.remove(id);
            Game response = gameDAO.find(id);
            Assertions.assertEquals(expectedResponse, response);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void invalidGameRemove() throws DataAccessException{
        Game expectedResponse = new Game(1, newGame.getWhiteUsername(), newGame.getBlackUsername(), newGame.getGameName(), newGame.getGame());
        try {
            int id = gameDAO.insert(newGame);
            gameDAO.remove(null);
            Game response = gameDAO.find(id);
            Assertions.assertEquals(expectedResponse, response);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void validUserInsert() throws DataAccessException{
        User expectedResponse = newUser;
        try {
            userDAO.insert(newUser);
            User response = userDAO.find(newUser.getUsername());
            Assertions.assertEquals(expectedResponse, response);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void invalidUserInsert() throws DataAccessException{
        AuthToken expectedResponse = null;
        try {
            userDAO.insert(new User(null, null, null));
            User response = userDAO.find(newUser.getUsername());
            Assertions.assertEquals(expectedResponse, response);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void validUserFind() throws DataAccessException{
        User expectedResponse = newUser;
        try {
            userDAO.insert(newUser);
            User response = userDAO.find(newUser.getUsername());
            Assertions.assertEquals(expectedResponse, response);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void invalidUserFind() throws DataAccessException{
        AuthToken expectedResponse = null;
        try {
            userDAO.insert(newUser);
            User response = userDAO.find(null);
            Assertions.assertEquals(expectedResponse, response);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void validUserClear() throws DataAccessException{
        User expectedResponse = null;
        try {
            userDAO.insert(newUser);
            userDAO.clear();
            User response = userDAO.find(newUser.getUsername());
            Assertions.assertEquals(expectedResponse, response);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
