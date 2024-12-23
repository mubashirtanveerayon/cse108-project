package utils;

import utils.response.Action;
import utils.response.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class IOWrapper {
    ObjectInputStream ois;
    ObjectOutputStream oos;



    private boolean closed = false;




    public IOWrapper(ObjectInputStream ois, ObjectOutputStream oos) {
        this.ois = ois;
        this.oos = oos;

    }


    public void write(Response r){
        if(closed)return;
        try{
            oos.writeUnshared(r);
        }catch(Exception e){
            // e.printStackTrace();
        }
    }

    public void write(Action action, String message, boolean success, boolean fromServer) {

            write(new Response(action,message,success,fromServer));

    }

    public Object read() {
        if(closed)return null;
        try{
            return ois.readUnshared();

        }catch(Exception e){
            // e.printStackTrace();
        }

        return null;
    }

    public void close(){
        if(closed)return;

        try{
            oos.close();
            ois.close();
            closed = true;
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }
}
