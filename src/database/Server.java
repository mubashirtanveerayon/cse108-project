package database;

import client.Club;
import entities.Player;
import utils.HelperFunctions;
import utils.attribute.StringAttribute;
import utils.enums.AttributeKey;
import utils.response.Action;
import utils.response.Data;
import utils.response.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Server extends Thread{
    public static final String IP = "127.0.0.1";
    public static final long DELAY = 1000;
    public static final int TIMEOUT = 60;
    private Map<String, ClientReader>clients;
    HashMap<String,String>loginCredentials;
    HashMap<Player,String>forAuction;
    public static final int PORT = 8000;
    private boolean running=false;

    public Server(){
        PlayerDatabase.initialize();
        clients = new HashMap<String,ClientReader>();
        loginCredentials = new HashMap<>();
        forAuction = new HashMap<>();
//        Random r = new Random();
//        for(Player p:PlayerDatabase.getInstance().players) {
//            if(forAuction.size()<13)forAuction.put(p,String.valueOf(r.nextLong()));
//            String club = p.getClub();
//            if (club == null || loginCredentials.containsKey(club)) continue;
//            else loginCredentials.put(club, HelperFunctions.sha256(club));
//}


    }

    public static void main(String[] args) {
        Server server = new Server();
        // server.start();
        server.begin();
    }



    public void begin(){
        try{
            running = true;
            ServerSocket ss = new ServerSocket(PORT);

            while(running) {


                    Socket socket = ss.accept();


                    serve(socket);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void serve(Socket socket){

        if(!running)return;
        Server server = this;
        Thread thread = new Thread() {
        public void run() {

            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Object o = ois.readObject();
                if (!(o instanceof Data)) return;

                Data received = (Data) o;
                if (received.fromServer) return;


                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                if (received.action == Action.LOGIN) {
                    if (!(received.data instanceof String)) {
                        oos.writeObject(new Response(received.action, "Incompatible data. String expected",false,true));
                        socket.close();
                        return;
                    }

                    String credentials = (String) received.data;
                    String[] parts = credentials.split(":");
                    if (parts.length != 2) {
                        oos.writeObject(new Response(received.action,"Invalid credentials",false,true));
                        socket.close();
                        return;
                    }

                    if (clients.containsKey(parts[0])) {
                        oos.writeObject(new Response(received.action,"Club is already logged in",false,true));
                        socket.close();
                        return;
                    }

                    if (!loginCredentials.containsKey(parts[0])) {
                        oos.writeObject(new Response(received.action,"This club does not exists",false,true));
                        socket.close();
                        return;
                    }
                    if (!loginCredentials.get(parts[0]).equals(parts[1])) {
                        oos.writeObject(new Response(received.action,"Incorrect password",false,true));
                        socket.close();
                        return;
                    }

                    ArrayList<Player> players = PlayerDatabase.getInstance().filterPlayers(new StringAttribute(parts[0], AttributeKey.CLUB));



                    Club club = new Club(parts[0], players, forAuction);

                    oos.writeObject(new Data(received.action,"club", true, club));


                    IOWrapper io = new IOWrapper(ois, oos);



                    ClientReader reader =  new ClientReader( parts[0],io,server);
                    reader.start();
                    clients.put(parts[0],reader);

                }else if(received.action == Action.SIGNUP){
                    if (!(received.data instanceof String)) {
                        oos.writeObject(new Response(received.action, "Incompatible data. String expected",false,true));
                        socket.close();
                        return;
                    }

                    String credentials = (String) received.data;
                    String[] parts = credentials.split(":");
                    if (parts.length != 2) {
                        oos.writeObject(new Response(received.action,"Invalid credentials",false,true));
                        socket.close();
                        return;
                    }

                    if (loginCredentials.containsKey(parts[0])) {
                        oos.writeObject(new Response(received.action,"Club is already registered",false,true));
                        socket.close();
                        return;
                    }

                   loginCredentials.put(parts[0],parts[1]);

                    oos.writeUnshared(new Response(received.action,"Registration successful. Login with this credential to continue.",true,true));
                    socket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        };
        thread.start();

    }

    public void run(){
        System.out.println("Server is ready!");
        int idleCounter = 0;
        while(true){
            try{
                Thread.sleep(Server.DELAY);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }

            System.out.println(forAuction.size());
//            if (clients.isEmpty()){
//                idleCounter ++;
//                System.out.println("Server will shutdown in "+(TIMEOUT-idleCounter)+" seconds...");
//            }
//            else idleCounter=0;
//
//
//            if(idleCounter==Server.TIMEOUT)break;
        }

        System.out.println("Disconnecting since no client is logged in!");
        running=false;
        PlayerDatabase.close();
    }

    public void broadcast(String clubSendingMessage,Data data){
        for(String club:clients.keySet())

            if(!club.equals(clubSendingMessage)) {
                ClientReader reader = clients.get(club);
                System.out.println(club);
                reader.io.write(data);
            }

    }


    public boolean updatePassword(String club, String currentPassword, String newPassword) {

        if(!loginCredentials.containsKey(club)||!loginCredentials.get(club).equals(currentPassword))return false;
        loginCredentials.put(club,newPassword);
        return true;
    }

    public void removeClient(String club) {
        clients.remove(club);
    }
}
