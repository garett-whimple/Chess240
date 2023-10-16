package Server.Models.Responses;

/**
 * Response Object that holds information for an AuthToken
 */
public class AuthTokenResponse {
    String message;
    int returnCode;
    String username;
    String authToken;

    public AuthTokenResponse(String message, int returnCode, String username, String authToken) {
        this.message = message;
        this.returnCode = returnCode;
        this.username = username;
        this.authToken = authToken;
    }

    /**
     * Returns the message
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * returns the returnCode
     * @return returnCode
     */
    public int getReturnCode() {
        return returnCode;
    }

    /**
     * Returns the username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the authToken
     * @return authToken
     */
    public String getAuthToken() {
        return authToken;
    }
}
