package Server.Services;

import Server.Models.Responses.AuthTokenResponse;
import Server.Models.User;

/**
 * Class that deals with any User server functionality
 */
public class RegisterService {
    /**
     * Constructor that creates a RegisterService Object
     */
    public RegisterService() {
    }

    /**
     * Registers the given user and adds them to the database
     * @param user User Object that has Username, Password, and Email fields
     * @return AuthTokenResponse
     */
    public AuthTokenResponse registerUser(User user){
        return null;
    }
}
