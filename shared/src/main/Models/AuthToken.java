package Models;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthToken authToken1)) return false;
        return Objects.equals(getAuthToken(), authToken1.getAuthToken()) && Objects.equals(getUsername(), authToken1.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAuthToken(), getUsername());
    }
}
