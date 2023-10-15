package Server;

public class Response<T> {
    T response;
    String message;
    int returnCode;

    public Response(T response, String message, int returnCode) {
        this.response = response;
        this.message = message;
        this.returnCode = returnCode;
    }

    public Response(String message, int returnCode) {
        this.message = message;
        this.returnCode = returnCode;
        this.response = null;
    }

    public T getResponse() {
        return response;
    }

    public String getMessage() {
        return message;
    }

    public int getReturnCode() {
        return returnCode;
    }
}
