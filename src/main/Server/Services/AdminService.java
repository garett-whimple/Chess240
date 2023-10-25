package Server.Services;

import Server.DataAccessObjects.AuthDAO;
import Server.DataAccessObjects.GameDAO;
import Server.DataAccessObjects.UserDAO;
import Server.Services.Responses.MessageResponse;
import dataAccess.DataAccessException;

/**
 * Class that deals with any administration server functionality
 */
public class AdminService {
    AuthDAO authDAO;
    UserDAO userDAO;
    GameDAO gameDAO;
    /**
     * Constructor that creates a AdminService Object
     */
    public AdminService() {
    }

    public AdminService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    /**
     * Clears all the tables in the database
     * @return MessageResponse
     */
    public MessageResponse clearDatabase() {
        try {
            authDAO.clear();
            userDAO.clear();
            gameDAO.clear();
            return new MessageResponse(null, 200);
        } catch (DataAccessException e) {
            String returnString = "Error: " + e.getMessage();
            return new MessageResponse(returnString, 500);
        }
    }
}
