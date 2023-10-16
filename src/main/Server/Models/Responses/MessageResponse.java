package Server.Models.Responses;

public class MessageResponse {
    String message;
    int returnCode;

    /**
     * Constructor to create a MessageResponse Object
     * @param message
     * @param returnCode
     */
    public MessageResponse(String message, int returnCode) {
        this.message = message;
        this.returnCode = returnCode;
    }

    /**
     * Returns message
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns returnCode
     * @return returnCode
     */
    public int getReturnCode() {
        return returnCode;
    }
}
