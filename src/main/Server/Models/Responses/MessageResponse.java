package Server.Models.Responses;

public class MessageResponse {
    String message;
    int returnCode;

    public MessageResponse(String message, int returnCode) {
        this.message = message;
        this.returnCode = returnCode;
    }

    public String getMessage() {
        return message;
    }

    public int getReturnCode() {
        return returnCode;
    }
}
