package database;

import entities.Player;
import utils.attribute.Attribute;
import utils.attribute.StringAttribute;
import utils.enums.AttributeKey;
import utils.response.Action;
import utils.response.Data;
import utils.response.Response;

import java.util.ArrayList;
import java.util.Arrays;

public class ClientReader extends Thread{

    final PlayerDatabase db;
    final String club;
    final IOWrapper io;

    Attribute clubAttribute;

    private Server server;
    public ClientReader(String clubName, IOWrapper io,Server server) {
        this.db = PlayerDatabase.getInstance();
        this.club = clubName;
        this.io = io;
        this.server = server;
        clubAttribute = new StringAttribute(club,AttributeKey.CLUB);
    }

    public void run(){
        System.out.println(club+" logged in!");
        try{
            while(true){
                Object o = io.read();




                if(!(o instanceof Data))continue;

                Data data = (Data) o;
                if(data.fromServer)continue;

                System.out.println("Request for "+data.action+" has been received from "+club);
                if(data.action == Action.RESET_PASSWORD){
                    if(!(data.data instanceof String)){

                        io.write(data.action,"Incompatible data. String expected",false,true);
                        continue;

                    }

                    String credential = (String)data.data;
                    String [] parts = credential.split(":");
                    if(parts.length!=2){
                        io.write(data.action,"Incompatible data. Expected format: <current password:new password>",false,true);
                        continue;
                    }
                    if(server.updatePassword(club,parts[0],parts[1])){
                        io.write(data.action,"Successfully updated",true,true);
                    }else{
                        io.write(data.action,"Failed to update password",false,true);
                    }




                }else if(data.action == Action.ADD){
                    if(!(data.data instanceof Player)){
                        io.write(data.action,"Incompatible data. Player expected",false,true);
                        continue;
                    }
                    Player player = (Player)data.data;

                    synchronized(db){
                        if(db.addPlayer(player)){
                            io.write(new Data( data.action,"Successfully added player",true,player));
                        }else{
                            io.write(data.action,"Failed to add player.",false,true);
                        }
                    }
                }else if(data.action == Action.SELL){
                    if(!(data.data instanceof Player)){
                        io.write(data.action,"Incompatible data. Player expected",false,true);
                        continue;
                    }

                    Player player = (Player)data.data;
                    player.toggleForSale();
                    server.broadcast(club,new Data(Action.SELL,data.message,true,player));
                    server.forAuction.put(player,data.message);

                    Player fromDatabase = db.filterPlayers(new StringAttribute(player.getName(),AttributeKey.NAME)).get(0);
                    fromDatabase.toggleForSale();

                }else if(data.action == Action.LOGOUT){
                    break;
                }


                else if(data.action == Action.BUY){
                    if(!(data.data instanceof Player)){
                        io.write(data.action,"Incompatible data. Player expected",false,true);
                        continue;
                    }
                    Player player = (Player)data.data;
                    Attribute clubAttribute  = new StringAttribute(club, AttributeKey.CLUB);
                    Player fromDatabase = db.filterPlayers(new StringAttribute(player.getName(),AttributeKey.NAME)).get(0);
                    fromDatabase.removeAttribute(AttributeKey.CLUB);
                    fromDatabase.addAttribute(clubAttribute);
                    fromDatabase.toggleForSale();
                    player.toggleForSale();
                    server.forAuction.remove(player);
                    server.broadcast(club,new Data(Action.BUY,"buy",true,player));
                }else if(data.action == Action.SEARCH){
                    if(!(data.data instanceof ArrayList)){
                        io.write(data.action,"Incompatible data. ArrayList<Attribute> expected",false,true);
                        continue;
                    }

                    ArrayList<Attribute>attributes = (ArrayList<Attribute>)data.data;
                    Attribute[] attributesArray = new Attribute[attributes.size()+1];
                    attributesArray[0] = clubAttribute  ;
                    for(int i=0;i<attributes.size();i++)
                        attributesArray[i+1] = attributes.get(i);
                    ArrayList<Player>filtered = db.filterPlayers(attributesArray);
                    Data response = new Data(data.action,"search result",true,filtered);
                    io.write(response);
                }else if(data.action == Action.SEARCH_MAX){
                    if(!(data.data instanceof AttributeKey)){
                        io.write(data.action,"Incompatible data. AttributeKey expected",false,true);
                        continue;
                    }

                    AttributeKey key = (AttributeKey)data.data;
                    ArrayList<Player>filtered = db.getPlayersWithMaximum(club,key);
                    io.write(new Data(data.action,"search result",true,filtered));
                }else if(data.action == Action.TOTAL_YEARLY_SALARY){
                    long totalSalary = db.getTotalYearlySalary(club);
                    io.write(new Response(data.action,""+totalSalary,true,true));
                }



            }
        }catch(Exception e){
            e.printStackTrace();
        }
        io.close();
        server.removeClient(club);

        System.out.println(club+" logged out!");

    }
}
