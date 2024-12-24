package utils.network;

import client.components.RequiresUpdate;
import client.controller.PlayerListView;
import javafx.scene.control.CheckBox;
import utils.Club;
import client.controller.AddPlayerView;
import client.controller.MarketplaceView;
import client.components.View;
import client.controller.MyClubView;
import utils.IOWrapper;
import entities.Player;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import utils.attribute.Attribute;
import utils.enums.AttributeKey;
import utils.response.Action;
import utils.response.Data;
import utils.response.Response;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerReader extends Thread{

    public static ServerReader getInstance() {
        return instance;
    }

    private static ServerReader instance;

    private IOWrapper io;
    private boolean stop = false;

    public ServerReader(IOWrapper io){
        instance =this;
        this.io = io;
    }

    public void run(){
        while(!stop ){

            Object o = io.read();
            if(!(o instanceof Response))

                continue;

            Response r = (Response)o;

            if(r.action == Action.RESET_PASSWORD){
                Platform.runLater(
                        ()->{
                            Alert alert = new Alert((r.success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR));
                            alert.setContentText(r.message);
                            alert.show();

                        }
                );
            }else if(r.action == Action.ADD){

                if(r instanceof Data){

                    Data data = (Data)r;
                    Player player = (Player)data.data;

                    if(player.containsAttribute(Club.getClub().clubAttribute)){
                        ArrayList<Player>players = Club.getClub().players;
                        synchronized (players){
                            players.add(player);
                        }

                        Platform.runLater(()->{
                            Alert alert = new Alert((r.success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR));
                            alert.setContentText(r.message);
                            alert.show();
                            ((AddPlayerView)View.currentView).loadClubView();
                        });
                    }
                    ArrayList<Player>allPlayers=Club.getClub().allPlayers;
                    synchronized (allPlayers){
                        allPlayers.add(player);
                    }

                    if(View.currentView instanceof PlayerListView){


                        PlayerListView controller = (PlayerListView)View.currentView;
                        Platform.runLater(()->{
                            controller.update(player);
                            controller.filter();
                        });


                    }



                }else{

                    Platform.runLater(()->{
                        Alert alert = new Alert((r.success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR));
                        alert.setContentText(r.message);
                        alert.show();
                    });

                }

//                if(r.success && r instanceof Data && ((Data)r).data instanceof Player) {
//                    Player player = (Player) ((Data) r).data;
//                    ArrayList<Player>players=Club.getClub().players,allPlayers=Club.getClub().allPlayers;
//                    synchronized (players){
//                        players.add(player);
//
//                    }
//                    synchronized (allPlayers) {
//                        allPlayers.add(player);
//                    }
//                }
//                Platform.runLater(
//                        () -> {
//                            Alert alert = new Alert((r.success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR));
//                            alert.setContentText(r.message);
//                            alert.show();
//
//                            if (r.success && View.currentView instanceof AddPlayerView) {
//                                View currentView = View.currentView;
//                                ((AddPlayerView) currentView).loadSearchView();
//                            }
//
//                        }
//                );
            }else if(r.action ==Action.BUY){
                if(!(r instanceof Data) || !(((Data)r).data instanceof  Player))return;
                Player bought = (Player)((Data)r).data;



                View currentView = View.currentView;

                if(currentView instanceof MarketplaceView){
                    MarketplaceView controller = (MarketplaceView)currentView;

                    Platform.runLater(
                            ()->{
                                controller.remove(bought);
                            }
                    );
                }else if(currentView instanceof RequiresUpdate){
                    Platform.runLater(
                            ()->{
                            ((RequiresUpdate) currentView).update(bought);
                        }
                    );
                }
                Club.getClub().forAuction.remove(bought);

                Player fromDatabase = Club.getClub().getPlayer(bought);
                fromDatabase.toggleForSale();
                fromDatabase.addAttribute(bought.getAttribute(AttributeKey.CLUB));
                HashMap<Player,String> forAuction = Club.getClub().forAuction;
                synchronized(forAuction) {
                    forAuction.remove(bought);
                }
            }else if(r.action ==Action.SELL){
                if(!(r instanceof Data) || !(((Data)r).data instanceof  Player))return;
                Data data = (Data)r;
                Player tobeSold = (Player)data.data;
                HashMap<Player,String> forAuction = Club.getClub().forAuction;

                View currentView = View.currentView;
                if(currentView instanceof MarketplaceView){
                    MarketplaceView controller = (MarketplaceView)currentView;

                    Platform.runLater(
                            ()->{
                                controller.add(tobeSold,data.message);
                            }
                    );
                }
                synchronized(forAuction) {
                    forAuction.put(tobeSold, data.message);
                }
            }else if(r.action == Action.SEARCH || r.action == Action.SEARCH_MAX){
                if(!(r instanceof Data) || !(((Data)r).data instanceof ArrayList))return;
                ArrayList<Player> filtered = (ArrayList<Player>)((Data)r).data;
                View currentView = View.currentView;
                if(currentView instanceof MyClubView){
                    Platform.runLater(()->{
                        MyClubView controller = (MyClubView)currentView;
                        controller.clearContainer();
                        controller.addPlayers(filtered);

                    });
                }

            }else if(r.action == Action.TOTAL_YEARLY_SALARY){
                Platform.runLater(()->{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Total yearly salary");
                    alert.setContentText(r.message);
                    alert.show();
                });
            }




        }


        io.close();
        instance=null;
    }

    public void terminate(){
        stop=true;
    }

    public IOWrapper getIO(){
        return io;
    }

}
