package utils.response;

public class Data extends Response implements java.io.Serializable{

    public final Object data;

    public Data(Action action, String message, boolean fromServer, Object o) {
        super(action, message, true, fromServer);
        this.data= o;
    }
}
