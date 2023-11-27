import Models.AuthToken;
import Models.Game;
import Models.User;
import Requests.JoinGameRequest;
import Responses.GameResponse;
import Responses.ListGameObject;
import Responses.ListGameResponse;
import chess.ChessGame;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.ServerFacade;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerFacadeTests {
    String url = "http://localhost:4567";
    ServerFacade sf = new ServerFacade(url);
    AuthToken badAuthToken = new AuthToken("authToken", "username");
    User newUser = new User("TestUserName", "Password123", "TestEmail");
    Game newGame = new Game(null, null, null, "TEST GAME NAME", null);
    JoinGameRequest joinGameRequest = new JoinGameRequest(1, "TestUserName", ChessGame.TeamColor.WHITE);

    @BeforeEach
    public void clear() {
        try {
            sf.clear();
        } catch (Throwable e) {
            System.out.println("Error calling clear endpoint");
        }
    }
    @Test
    public void validRegisterCall() {
        AuthToken expectedResponse = new AuthToken("SOME_TEXT", "TestUserName");
        AuthToken response = assertDoesNotThrow(() -> sf.registerUser(newUser));
        Assertions.assertEquals(expectedResponse.getUsername(), response.getUsername(),
                "Incorrect Username returned");
        Assertions.assertEquals(expectedResponse.getAuthToken().getClass(), response.getAuthToken().getClass(),
                "AuthToken is null or not a string");
    }

    @Test
    public void invalidRegisterCall() {
        String expectedResponse = "Failure: 403";
        assertDoesNotThrow(() -> sf.registerUser(newUser));
        try {
            sf.registerUser(newUser);
        } catch (Throwable e) {
            assertEquals(expectedResponse, e.getMessage());
        }
    }

    @Test
    public void validClearCall() {
        String expectedResponse = "Failure: 401";
        AuthToken authToken = assertDoesNotThrow(() -> sf.registerUser(newUser));
        assertDoesNotThrow(() -> sf.createGame(newGame, authToken));
        assertDoesNotThrow(()-> sf.clear());
        try {
            ListGameResponse response = sf.listGame(authToken);
        } catch (Throwable e) {
            assertEquals(expectedResponse, e.getMessage());
        }
    }

    @Test
    public void validLoginCall() {
        AuthToken expectedResponse = new AuthToken("SOME_TEXT", "TestUserName");
        assertDoesNotThrow(() -> sf.registerUser(newUser));
        AuthToken response = assertDoesNotThrow(() -> sf.login(newUser));
        Assertions.assertEquals(expectedResponse.getUsername(), response.getUsername(),
                "Incorrect Username returned");
        Assertions.assertEquals(expectedResponse.getAuthToken().getClass(), response.getAuthToken().getClass(),
                "AuthToken is null or not a string");
    }

    @Test
    public void invalidLoginCall() {
        String expectedResponse = "Failure: 401";
        assertDoesNotThrow(() -> sf.registerUser(newUser));
        try {
            sf.login(new User("TestUserName", "Wrong_Password", null));
        } catch (Throwable e) {
            assertEquals(expectedResponse, e.getMessage());
        }
    }

    @Test
    public void validLogoutCall() {
        AuthToken authToken = assertDoesNotThrow(() -> sf.registerUser(newUser));
        assertDoesNotThrow(() -> sf.logout(authToken));
    }

    @Test
    public void invalidLogoutCall() {
        String expectedResponse = "Failure: 401";
        assertDoesNotThrow(() -> sf.registerUser(newUser));
        try {
            sf.logout(badAuthToken);
        } catch (Throwable e) {
            assertEquals(expectedResponse, e.getMessage());
        }
    }

    @Test
    public void validCreateGameCall() {
        GameResponse expectedResponse = new GameResponse(null, null, 1);
        AuthToken authToken = assertDoesNotThrow(() -> sf.registerUser(newUser));
        GameResponse response = assertDoesNotThrow(() -> sf.createGame(newGame, authToken));
        assertEquals(expectedResponse.getGameID(),response.getGameID());
    }

    @Test
    public void invalidCreateGameCall() {
        String expectedResponse = "Failure: 401";
        try {
            sf.createGame(newGame, badAuthToken);
        } catch (Throwable e) {
            assertEquals(expectedResponse, e.getMessage());
        }
    }

    @Test
    public void validListGameCall() {
        ArrayList<ListGameObject> expectedResponse = new ArrayList<>();
        ListGameObject expectedGame = new ListGameObject(1, null, null, "TEST GAME NAME");
        expectedResponse.add(expectedGame);
        AuthToken authToken = assertDoesNotThrow(() -> sf.registerUser(newUser));
        assertDoesNotThrow(() -> sf.createGame(newGame, authToken));
        ListGameResponse response = assertDoesNotThrow(() -> sf.listGame(authToken));
        assertEquals(expectedResponse,response.getGames());
    }

    @Test
    public void invalidListGameCall() {
        String expectedResponse = "Failure: 401";
        try {
            sf.listGame(badAuthToken);
        } catch (Throwable e) {
            assertEquals(expectedResponse, e.getMessage());
        }
    }

    @Test
    public void validJoinGameCall() {
        ArrayList<ListGameObject> expectedResponse = new ArrayList<>();
        ListGameObject expectedGame = new ListGameObject(1, "TestUserName", null, "TEST GAME NAME");
        expectedResponse.add(expectedGame);
        AuthToken authToken = assertDoesNotThrow(() -> sf.registerUser(newUser));
        assertDoesNotThrow(() -> sf.createGame(newGame, authToken));
        assertDoesNotThrow(() -> sf.joinGame(joinGameRequest, authToken));
        ListGameResponse response = assertDoesNotThrow(() -> sf.listGame(authToken));
        assertEquals(expectedResponse,response.getGames());
    }

    @Test
    public void invalidJoinGameCall() {
        String expectedResponse = "Failure: 401";
        try {
            sf.joinGame(joinGameRequest, badAuthToken);
        } catch (Throwable e) {
            assertEquals(expectedResponse, e.getMessage());
        }
    }
}


