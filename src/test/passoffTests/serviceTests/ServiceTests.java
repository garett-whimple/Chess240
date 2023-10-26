package passoffTests.serviceTests;

import Server.DataAccessObjects.AuthDAO;
import Server.DataAccessObjects.GameDAO;
import Server.DataAccessObjects.UserDAO;
import Server.Models.User;
import Server.Services.AdminService;
import Server.Services.GameService;
import Server.Services.LoginService;
import Server.Services.RegisterService;
import Server.Services.Responses.AuthTokenResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

public class ServiceTests {
    GameDAO gameDAO = new GameDAO();
    AuthDAO authDAO = new AuthDAO();
    UserDAO userDAO = new UserDAO();
    GameService gameService = new GameService(gameDAO);
    LoginService loginService = new LoginService(authDAO, userDAO);
    RegisterService registerService = new RegisterService(userDAO, authDAO);
    AdminService adminService = new AdminService(authDAO, userDAO, gameDAO);
    User newUser = new User("TestUserName", "Password123", "TestEmail");


//    @BeforeAll
//    public void setup() {
//    }
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



}
