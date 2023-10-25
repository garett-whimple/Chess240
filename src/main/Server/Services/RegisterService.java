package Server.Services;

import Server.DataAccessObjects.AuthDAO;
import Server.DataAccessObjects.UserDAO;
import Server.Models.AuthToken;
import Server.Services.Responses.AuthTokenResponse;
import Server.Models.User;
import dataAccess.DataAccessException;

import java.util.UUID;

/**
 * Class that deals with any User server functionality
 */
public class RegisterService {
    UserDAO userDAO;
    AuthDAO authDAO;
    /**
     * Constructor that creates a RegisterService Object
     */
    public RegisterService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    /**
     * Registers the given user and adds them to the database
     * @param user User Object that has Username, Password, and Email fields
     * @return AuthTokenResponse
     */
    public AuthTokenResponse registerUser(User user){
        AuthTokenResponse returnResponse = null;
        try {
            if (user == null|| user.getPassword() == null || user.getUsername() == null || user.getEmail() == null) {
                returnResponse =  new AuthTokenResponse("Error: bad request", 400, null, null);
                return returnResponse;
            }
            User foundUser = userDAO.find(user.getUsername());
            if (foundUser != null) {
                returnResponse =  new AuthTokenResponse("Error: already taken", 403, null, null);
            } else {
                String authToken = UUID.randomUUID().toString();
                AuthToken newAuthToken = new AuthToken(authToken, user.getUsername());
                authDAO.insert(newAuthToken);
                userDAO.insert(user);
                returnResponse =  new AuthTokenResponse(null, 200, user.getUsername(), authToken);
            }
        } catch (DataAccessException e) {
            String returnMessage = "Error: " + e.getMessage();
            returnResponse =  new AuthTokenResponse(returnMessage, 500, null, null);
        }
        return returnResponse;
    }
}
