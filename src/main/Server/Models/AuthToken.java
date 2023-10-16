package Server.Models;

public class AuthToken {
    String authToken;
    String username;

    /**
     * Constructor to create a AuthToken Object
     * @param authToken
     * @param username
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
