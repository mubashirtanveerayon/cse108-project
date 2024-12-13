package utils.response;

public class Response implements java.io.Serializable{


    public final Action action;
    public final String message;
    public final boolean success;
    public final boolean fromServer;


    public Response(Action action, String message, boolean success, boolean fromServer) {
        this.action = action;
        this.message = message;
        this.success = success;
        this.fromServer = fromServer;
    }
}
