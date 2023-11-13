package Server.Services;

import Server.DataAccessObjects.AuthDAO;
import Server.DataAccessObjects.UserDAO;
import Models.AuthToken;
import Responses.AuthTokenResponse;
import Responses.MessageResponse;
import Models.User;
import dataAccess.DataAccessException;

import java.util.Objects;
import java.util.UUID;

/**
 * Class that deals with any Session server functionality
 */
public class LoginService {
    private AuthDAO authDAO;
    private UserDAO userDAO;
    /**
     * Constructor that creates a LoginService Object
     */
    public LoginService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    /**
     * Login the given User
     * @param user User Object that has Username, Password, and Email fields
     * @return AuthTokenResponse
     */
    public AuthTokenResponse loginUser(User user){
        AuthTokenResponse returnResponse = null;
        try {
            User foundUser = userDAO.find(user.getUsername());
            if (foundUser == null || !Objects.equals(foundUser.getPassword(), user.getPassword())) {
                returnResponse =  new AuthTokenResponse("Error: unauthorized", 401, null, null);
            } else {
                String authToken = UUID.randomUUID().toString();
                AuthToken newAuthToken = new AuthToken(authToken, user.getUsername());
                authDAO.insert(newAuthToken);
                returnResponse = new AuthTokenResponse(null, 200, newAuthToken.getUsername(), newAuthToken.getAuthToken());
            }
        } catch (DataAccessException e) {
            String returnMessage = "Error: " + e.getMessage();
            returnResponse =  new AuthTokenResponse(returnMessage, 500, null, null);
        }
        return returnResponse;
    }

    /**
     * Logout the given user
     * @param  authToken authToken Object that has Username and authToken fields
     * @return MessageResponse
     */
    public MessageResponse LogoutUser(AuthToken authToken){
        MessageResponse returnResponse = null;
        try {
            AuthToken foundToken = authDAO.find(authToken.getAuthToken());
            if (foundToken == null) {
                returnResponse =  new MessageResponse("Error: authToken does not exist", 500);
            } else {
                authDAO.remove(authToken);
                returnResponse = new MessageResponse(null, 200);
            }
        } catch (DataAccessException e) {
            String returnMessage = "Error: " + e.getMessage();
            returnResponse =  new MessageResponse(returnMessage, 500);
        }
        return returnResponse;
    }
}
