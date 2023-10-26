package Server.Services.Responses;

import java.util.Objects;

/**
 * Response Object that holds information for void responses
 */
public class MessageResponse {
    /**
     * Is the error message of the response if there is one
     */
    String message;
    /**
     * Is the return Code of the response
     */
    Integer returnCode;

    /**
     * Constructor to create a MessageResponse Object
     * @param message Is the error message of the response if there is one
     * @param returnCode Is the return Code of the response
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
    public void setReturnCodeNull() {
        this.returnCode = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageResponse response)) return false;
        return Objects.equals(getMessage(), response.getMessage()) && Objects.equals(getReturnCode(), response.getReturnCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMessage(), getReturnCode());
    }
}
