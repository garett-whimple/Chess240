package Models;

import java.util.Objects;

/**
 * Object that represents a User
 */
public class User {
    /**
     * Identifier of the User Object
     */
    String username;
    /**
     * Password used to validate the User
     */
    String password;
    /**
     * Email of the user
     */
    String email;

    /**
     * Constructor that creates a User Object
     * @param username Identifier of the User Object
     * @param password Password used to validate the User
     * @param email Email of the user
     */
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User() {
    }

    /**
     * Returns username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns password
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getUsername(), user.getUsername()) && Objects.equals(getPassword(), user.getPassword()) && Objects.equals(getEmail(), user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword(), getEmail());
    }
}
