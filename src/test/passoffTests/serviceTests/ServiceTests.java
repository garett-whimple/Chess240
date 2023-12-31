package passoffTests.serviceTests;

import Responses.*;
import Server.DataAccessObjects.AuthDAO;
import Server.DataAccessObjects.GameDAO;
import Server.DataAccessObjects.UserDAO;
import Models.AuthToken;
import Models.Game;
import Models.User;
import Server.Services.AdminService;
import Server.Services.GameService;
import Server.Services.LoginService;
import Server.Services.RegisterService;
import Requests.JoinGameRequest;
import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.Database;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class ServiceTests {
    static Database db = new Database();
    GameDAO gameDAO = new GameDAO(db);
    AuthDAO authDAO = new AuthDAO(db);
    UserDAO userDAO = new UserDAO(db);
    GameService gameService = new GameService(gameDAO);
    LoginService loginService = new LoginService(authDAO, userDAO);
    RegisterService registerService = new RegisterService(userDAO, authDAO);
    AdminService adminService = new AdminService(authDAO, userDAO, gameDAO);
    User newUser = new User("TestUserName", "Password123", "TestEmail");


    @BeforeAll
    public static void setupDB() throws DataAccessException {
        try {
            db.Initialize();
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    @BeforeEach
    public void reset() {
        adminService.clearDatabase();
    }

    @Test
    public void validRegisterService() {
        AuthTokenResponse expectedResponse = new AuthTokenResponse(null,200, "TestUserName", "SOME_AUTH_TOKEN" );
        AuthTokenResponse response =registerService.registerUser(newUser);
        Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage(),
                "Message was not set to null");
        Assertions.assertEquals(expectedResponse.getReturnCode(), response.getReturnCode(),
                "Incorrect returnCode returned");
        Assertions.assertEquals(expectedResponse.getUsername(), response.getUsername(),
                "Incorrect Username returned");
        Assertions.assertEquals(expectedResponse.getAuthToken().getClass(), response.getAuthToken().getClass(),
                "AuthToken is null or not a string");
    }

    @Test
    public void invalidRegisterService() {
        AuthTokenResponse expectedResponse = new AuthTokenResponse("Error: already taken",403, null, null );
        registerService.registerUser(newUser);
        AuthTokenResponse response =registerService.registerUser(newUser);
        Assertions.assertEquals(expectedResponse, response,
                "response does not match expected response");
    }

    @Test
    public void validClearService() {
        MessageResponse expectedResponse = new MessageResponse(null,200);
        MessageResponse response =adminService.clearDatabase();
        Assertions.assertEquals(expectedResponse, response,
                "response does not match expected response");
    }

    @Test
    public void validLoginService() {
        AuthTokenResponse authResponse = registerService.registerUser(newUser);
        AuthTokenResponse expectedResponse = new AuthTokenResponse(null, 200, authResponse.getUsername(), "SOME_AUTH_TOKEN");
        AuthTokenResponse response = loginService.loginUser(newUser);
        Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage(),
                "Message was not set to null");
        Assertions.assertEquals(expectedResponse.getReturnCode(), response.getReturnCode(),
                "Incorrect returnCode returned");
        Assertions.assertEquals(expectedResponse.getUsername(), response.getUsername(),
                "Incorrect Username returned");
        Assertions.assertEquals(expectedResponse.getAuthToken().getClass(), response.getAuthToken().getClass(),
                "AuthToken is null or not a string");
    }

    @Test
    public void invalidLoginService() {
        AuthTokenResponse expectedResponse = new AuthTokenResponse("Error: unauthorized", 401, null, null);
        AuthTokenResponse response = loginService.loginUser(new User(newUser.getUsername(), "INCORRECT_PASSWORD", "SOME_EMAIL"));
        Assertions.assertEquals(expectedResponse, response,
                "response does not match expected response");
    }

    @Test
    public void validLogoutService() {
        AuthTokenResponse authResponse = registerService.registerUser(newUser);
        MessageResponse expectedResponse = new MessageResponse(null, 200);
        MessageResponse response = loginService.LogoutUser(new AuthToken(authResponse.getAuthToken(), authResponse.getUsername()));
        Assertions.assertEquals(expectedResponse, response,
                "response does not match expected response");
    }

    @Test
    public void invalidLogoutService() {
        MessageResponse expectedResponse = new MessageResponse("Error: authToken does not exist", 500);
        MessageResponse response = loginService.LogoutUser(new AuthToken("INCORRECT_AUTHTOKEN", newUser.getUsername()));
        Assertions.assertEquals(expectedResponse, response,
                "response does not match expected response");
    }

    @Test
    public void validCreateGameService() {
        GameResponse expectedResponse = new GameResponse(null, 200, 1);
        GameResponse response = gameService.createGame(new Game(null, null, null, "TEST GAME NAME", null));
        Assertions.assertEquals(expectedResponse, response,
                "response does not match expected response");
    }

    @Test
    public void invalidCreateGameService() {
        GameResponse expectedResponse = new GameResponse("Error: bad request", 400, null);
        GameResponse response = gameService.createGame(new Game(null, null, null, null, null));
        Assertions.assertEquals(expectedResponse, response,
                "response does not match expected response");
    }

    @Test
    public void validJoinGameService() {
        GameResponse createGameResponse = gameService.createGame(new Game(null, null, null, "TEST GAME NAME", null));
        MessageResponse expectedResponse = new MessageResponse(null, 200);
        MessageResponse response = gameService.joinGame(new JoinGameRequest(createGameResponse.getGameID(),"TEST USERNAME", ChessGame.TeamColor.WHITE));
        Assertions.assertEquals(expectedResponse, response,
                "response does not match expected response");
    }

    @Test
    public void invalidJoinGameService() {
        GameResponse createGameResponse = gameService.createGame(new Game(null, null, null, "TEST GAME NAME", null));
        MessageResponse expectedResponse = new MessageResponse("Error: already taken", 403);
        gameService.joinGame(new JoinGameRequest(createGameResponse.getGameID(),"TEST USERNAME", ChessGame.TeamColor.WHITE));
        MessageResponse response = gameService.joinGame(new JoinGameRequest(createGameResponse.getGameID(),"OTHER USERNAME", ChessGame.TeamColor.WHITE));
        Assertions.assertEquals(expectedResponse, response,
                "response does not match expected response");
    }

    @Test
    public void validListGameService() {
        GameResponse createGameResponse = gameService.createGame(new Game(null, null, null, "TEST GAME NAME", null));
        gameService.createGame(new Game(null, null, null, "OTHER GAME NAME", null));
        gameService.joinGame(new JoinGameRequest(createGameResponse.getGameID(),"TEST USERNAME", ChessGame.TeamColor.WHITE));
        ArrayList<ListGameObject> expectedArray = new ArrayList<>();
        expectedArray.add(new ListGameObject(1, "TEST USERNAME", null, "TEST GAME NAME"));
        expectedArray.add(new ListGameObject(2, null, null, "OTHER GAME NAME"));
        ListGameResponse expectedResponse = new ListGameResponse(null, 200, expectedArray);
        ListGameResponse response = gameService.listGames();
        Assertions.assertEquals(expectedResponse, response,
                "response does not match expected response");
    }

    @Test
    public void invalidListGameService() {
        adminService.clearDatabase();
        ListGameResponse expectedResponse = new ListGameResponse(null, 200, new ArrayList<>());
        ListGameResponse response = gameService.listGames();
        Assertions.assertEquals(expectedResponse, response,
                "response does not match expected response");
    }
}
