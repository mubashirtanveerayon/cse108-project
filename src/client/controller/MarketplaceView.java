package client.controller;

import utils.Club;
import client.components.View;
import entities.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MarketplaceView extends View {


    @FXML
    private VBox playerContainer;

    HashMap<Player ,Parent>playerCards=new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        currentView = this;

        load();
    }

    public void remove(Player player){
        Club club = Club.getClub();
        if(club == null)return;
        Parent view = playerCards.get(player);

        if(view == null)return;
        playerContainer.getChildren().remove(view);
        playerCards.remove(player);

    }

    public void add(Player player,String price){
        Club club = Club.getClub();
        if(club == null)return;

        try{
            FXMLLoader loader = new FXMLLoader (getClass().getResource("/client/gui/res/view/player-card.fxml"));
            Parent root = loader.load();
            PlayerCard controller = loader.getController();

            controller.setPlayer(player);
            controller.inMarketPlace = true;



            controller.actionButton.setText("Buy for $"+price);


            player.toggleForSale();
            controller.actionButton.setDisable(player.containsAttribute(club.clubAttribute));

            player.toggleForSale();

            playerContainer.getChildren().add(root);

            playerCards.put(player,root);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void load(){
        playerContainer.getChildren().clear();
        playerCards.clear();
        Club club = Club.getClub();
        if(club == null)return;


        for(Player player:club.forAuction.keySet()){
            add(player,club.forAuction.get(player));
        }

    }
}
