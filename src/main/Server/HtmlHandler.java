package Server;

import Server.DataAccessObjects.AuthDAO;
import Server.DataAccessObjects.GameDAO;
import Server.DataAccessObjects.UserDAO;
import Server.Models.AuthToken;
import Server.Models.Game;
import Server.Models.User;
import Server.Services.AdminService;
import Server.Services.GameService;
import Server.Services.LoginService;
import Server.Services.RegisterService;
import Server.Services.Requests.JoinGameRequest;
import Server.Services.Responses.AuthTokenResponse;
import Server.Services.Responses.GameResponse;
import Server.Services.Responses.ListGameResponse;
import Server.Services.Responses.MessageResponse;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import spark.Request;
import spark.Response;

public class HtmlHandler {
    private static AuthDAO authDAO = new AuthDAO();
    private static UserDAO userDAO = new UserDAO();
    private static GameDAO gameDAO = new GameDAO();
    private static AdminService adminService = new AdminService(authDAO, userDAO, gameDAO);
    private static GameService gameService = new GameService(gameDAO);
    private static LoginService loginService = new LoginService(authDAO, userDAO);
    private static RegisterService registerService = new RegisterService(userDAO, authDAO);
    private static Gson gson = new Gson();

    public static String registerUserHandler(Request req, Response res) {
        User user = (User)gson.fromJson(req.body(), User.class);

        AuthTokenResponse result = registerService.registerUser(user);

        res.type("application/json");
        res.status(result.getReturnCode());
        result.setReturnCodeNull();
        return gson.toJson(result);
    }

    public static String loginHandler(Request req, Response res) {
        User user = (User)gson.fromJson(req.body(), User.class);

        AuthTokenResponse result = loginService.loginUser(user);
        res.type("application/json");
        res.status(result.getReturnCode());
        result.setReturnCodeNull();
        return gson.toJson(result);
    }

    public static String clearHandler(Request req, Response res) {
        MessageResponse result = adminService.clearDatabase();
        res.type("application/json");
        res.status(result.getReturnCode());
        return gson.toJson(result);
    }

    public static String logoutHandler(Request req, Response res) {
        String authHeader = req.headers("authorization");
        MessageResponse result = null;
        try {
            AuthToken authToken = checkAuth(authHeader);
            if(authToken==null) {
                result = new MessageResponse("Error: unauthorized", 401);
            } else {
                result = loginService.LogoutUser(authToken);
            }
        } catch (DataAccessException e) {
            result = new MessageResponse("Error: " + e.getMessage(), 500);
        }
        res.type("application/json");
        res.status(result.getReturnCode());
        result.setReturnCodeNull();
        return gson.toJson(result);
    }

    public static String listGameHandler(Request req, Response res) {
        String authHeader = req.headers("authorization");
        ListGameResponse result = null;
        try {
            AuthToken authToken = checkAuth(authHeader);
            if(authToken==null) {
                result = new ListGameResponse("Error: unauthorized", 401, null);
            } else {
                result = gameService.listGames();
            }
        } catch (DataAccessException e) {
            result = new ListGameResponse("Error: " + e.getMessage(), 500, null);
        }
        res.type("application/json");
        res.status(result.getReturnCode());
        result.setReturnCodeNull();
        return gson.toJson(result);
    }

    public static String createGameHandler(Request req, Response res) {
        String authHeader = req.headers("authorization");
        GameResponse result = null;
        try {
            AuthToken authToken = checkAuth(authHeader);
            if(authToken==null) {
                result = new GameResponse("Error: unauthorized", 401, null);
            } else {
                Game game = (Game)gson.fromJson(req.body(), Game.class);
                if (game.getGameName() == null) {
                    result = new GameResponse("Error: bad request", 400, null);
                } else {
                    result = gameService.createGame(game);
                }
            }
        } catch (DataAccessException e) {
            result = new GameResponse("Error: " + e.getMessage(), 500, null);
        }
        res.type("application/json");
        res.status(result.getReturnCode());
        result.setReturnCodeNull();
        return gson.toJson(result);
    }

    public static String joinGameHandler(Request req, Response res) {
        String authHeader = req.headers("authorization");
        MessageResponse result = null;
        try {
            AuthToken authToken = checkAuth(authHeader);
            if(authToken==null) {
                result = new MessageResponse("Error: unauthorized", 401);
            } else {
                JoinGameRequest joinGameRequest = (JoinGameRequest)gson.fromJson(req.body(), JoinGameRequest.class);
                if (joinGameRequest.getGameID()== null){
                    result = new MessageResponse("Error: bad request", 400);
                } else {
                    joinGameRequest.setUsername(authToken.getUsername());
                    result = gameService.joinGame(joinGameRequest);
                }
            }
        } catch (DataAccessException e) {
            result = new MessageResponse("Error: " + e.getMessage(), 500);
        }
        res.type("application/json");
        res.status(result.getReturnCode());
        result.setReturnCodeNull();
        return gson.toJson(result);
    }

    private static AuthToken checkAuth(String auth) throws DataAccessException {
        AuthToken authToken = authDAO.find(auth);
        if (authToken == null) {
            return null;
        } else {
            return authToken;
        }
    }

}
