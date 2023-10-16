package Server.Models;

/**
 * Object that represents a User
 */
public class User {
    String username;
    String password;
    String email;

    /**
     * Constructor that creates a User Object
     * @param username
     * @param password
     * @param email
     */
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
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
}
