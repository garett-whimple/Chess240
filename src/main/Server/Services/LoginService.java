package Server.Services;

import Server.Models.Responses.AuthTokenResponse;
import Server.Models.Responses.MessageResponse;
import Server.Models.User;

/**
 * Class that deals with any Session server functionality
 */
public class LoginService {
    /**
     * Constructor that creates a LoginService Object
     */
    public LoginService() {
    }

    /**
     * Login the given User
     * @param user User Object that has Username, Password, and Email fields
     * @return AuthTokenResponse
     */
    public AuthTokenResponse loginUser(User user){
        return null;
    }

    /**
     * Logout the given user
     * @param user User Object that has Username, Password, and Email fields
     * @return MessageResponse
     */
    public MessageResponse LogoutUser(User user){
        return null;
    }
}
