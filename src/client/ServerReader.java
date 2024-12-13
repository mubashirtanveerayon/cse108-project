package client;

import client.gui.controller.AddPlayerView;
import client.gui.controller.MarketplaceView;
import client.gui.components.View;
import client.gui.controller.PlayerSearchView;
import database.IOWrapper;
import entities.Player;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import utils.response.Action;
import utils.response.Data;
import utils.response.Response;

import java.util.ArrayList;

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
//  Mumbai Indians
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
                if(r.success && r instanceof Data && ((Data)r).data instanceof Player){
                    Player player = (Player)((Data)r).data;
                    Club.getClub().players.add(player);
                }
                Platform.runLater(
                        ()->{
                            Alert alert = new Alert((r.success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR));
                            alert.setContentText(r.message);
                            alert.show();

                            if(r.success && View.currentView instanceof AddPlayerView){
                                View currentView = View.currentView;
                                ((AddPlayerView)currentView).loadSearchView();
                            }

                        }
                );
            }else if(r.action ==Action.BUY){
                if(!(r instanceof Data) || !(((Data)r).data instanceof  Player))return;
                Player bought = (Player)((Data)r).data;
                Club.getClub().forAuction.remove(bought);
                View currentView = View.currentView;

                if(currentView instanceof MarketplaceView){
                    MarketplaceView controller = (MarketplaceView)currentView;

                    Platform.runLater(
                            ()->{
                                controller.remove(bought);
                            }
                    );
                }
            }else if(r.action ==Action.SELL){
                if(!(r instanceof Data) || !(((Data)r).data instanceof  Player))return;
                Data data = (Data)r;
                Player tobeSold = (Player)data.data;
                Club.getClub().forAuction.put(tobeSold,data.message);
                View currentView = View.currentView;
                if(currentView instanceof MarketplaceView){
                    MarketplaceView controller = (MarketplaceView)currentView;

                    Platform.runLater(
                            ()->{
                                controller.add(tobeSold,data.message);
                            }
                    );
                }
            }else if(r.action == Action.SEARCH || r.action == Action.SEARCH_MAX){
                if(!(r instanceof Data) || !(((Data)r).data instanceof ArrayList))return;
                ArrayList<Player> filtered = (ArrayList<Player>)((Data)r).data;
                View currentView = View.currentView;
                if(currentView instanceof PlayerSearchView){
                    Platform.runLater(()->{
                        PlayerSearchView controller = (PlayerSearchView)currentView;
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
