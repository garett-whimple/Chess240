package Responses;

import java.util.Objects;

/**
 * Response Object that holds information for an AuthToken
 */
public class AuthTokenResponse {
    /**
     * Is the error message of the response if there is one
     */
    String message;
    /**
     * Is the return Code of the response
     */
    Integer returnCode;
    /**
     * Username of the user
     */
    String username;
    /**
     * AuthToken connected to the user
     */
    String authToken;

    /**
     * Constructor that creates a AuthTokenResponse Object
     * @param message Is the error message of the response if there is one
     * @param returnCode Is the return Code of the response
     * @param username Username of the user
     * @param authToken AuthToken connected to the user
     */
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
    public void setReturnCodeNull() {
        this.returnCode = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthTokenResponse response)) return false;
        return Objects.equals(getMessage(), response.getMessage()) && Objects.equals(getReturnCode(), response.getReturnCode()) && Objects.equals(getUsername(), response.getUsername()) && Objects.equals(getAuthToken(), response.getAuthToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMessage(), getReturnCode(), getUsername(), getAuthToken());
    }
}
