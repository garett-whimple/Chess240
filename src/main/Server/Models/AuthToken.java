package Server.Models;

/**
 * Object that represents a AuthToken
 */
public class AuthToken {
    /**
     * authToken connected to the user
     */
    String authToken;
    /**
     * Username of the user
     */
    String username;

    /**
     * Constructor to create a AuthToken Object
     * @param authToken String of the AuthToken
     * @param username String of the username
     */
    public AuthToken(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    /**
     * Returns authToken
     * @return authToken
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * Returns username
     * @return username
     */
    public String getUsername() {
        return username;
    }
}
