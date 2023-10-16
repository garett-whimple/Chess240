package Server.Models.Responses;

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

    public String getMessage() {
        return message;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }
}
