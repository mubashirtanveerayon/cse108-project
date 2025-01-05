package client.controller;

import client.components.RequiresUpdate;
import utils.Club;
import utils.attribute.Attribute;
import utils.network.ServerReader;
import client.components.View;
import utils.IOWrapper;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import entities.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utils.enums.AttributeKey;
import utils.response.Action;
import utils.response.Data;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class PlayerCard extends  View implements RequiresUpdate {

    private Player player;
    @FXML
    public Button actionButton;


    @FXML
    public MaterialIconView actionButtonIcon;

    @FXML
    private Label clubLabel;

    @FXML
    private Button detailsButton;

    @FXML
    private ImageView positionImage;

    @FXML
    private Label nameLabel;
    public boolean inMarketPlace;


    Club club;



    @FXML
    void onActionButtonPressed(ActionEvent event) {
        if(inMarketPlace) {



            club.forAuction.remove(player);

            player.toggleForSale();
            player.addAttribute(Club.getClub().clubAttribute);
            club.players.add(player);

            Player fromDatabase = Club.getClub().getPlayer(player);
            fromDatabase.addAttribute(Club.getClub().clubAttribute);
            fromDatabase.toggleForSale();

            ((MarketplaceView) currentView).remove(player);


//            player.removeAttribute(AttributeKey.CLUB);
//            player.addAttribute(club.clubAttribute);

            ServerReader sr = ServerReader.getInstance();

            IOWrapper io = sr.getIO();
            io.write(new Data(Action.BUY, "buy", false, player));


        }else{
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setTitle("Sell this player");
            inputDialog.setContentText("For how much you would like to sell?");
            Optional<String> result = inputDialog.showAndWait();
            if(result.isPresent()) {
                try{
                    long price = Long.parseLong(result.get());


                    club.forAuction.put(player,result.get());
                    player.toggleForSale();
                    club.players.remove(player);


                    MyClubView searchView = (MyClubView)View.currentView;
                    searchView.removePlayer(player);

                    ServerReader sr = ServerReader.getInstance();

                    IOWrapper io = sr.getIO();

                    io.write(new Data(Action.SELL,String.valueOf(price),false,player));

                }catch(Exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Invalid input");
                    alert.show();
                }
            }


        }

    }

    @FXML
    void onDetailsButtonPressed(ActionEvent event) {


        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/gui/res/view/player-view.fxml"));
            Parent root = loader.load();
            PlayerView controller = loader.getController();

            controller.setPlayer(player,inMarketPlace);




            getMainView().borderPane.setCenter(root);

        }catch(Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        club = Club.getClub();
    }

    public void setPlayer(Player player){
        // this.inMarketPlace = inMarketPlace;
        this.player = player;
        update(player);

//        if(!inMarketPlace)actionButtonIcon.setGlyphName("MONETIZATION_ON");

    }

    @Override
    public void update(Player player) {
        nameLabel.setText(player.getName());
        clubLabel.setText(player.getClub());

        // set image to positionImage image view
        String fileName = "bat";
        String position = player.getPosition();
        if(position.equalsIgnoreCase("bowler"))fileName = "ball";
        else if(position.equalsIgnoreCase("wicketkeeper"))fileName = "helmet";
        else if(position.equalsIgnoreCase("allrounder"))fileName = "bat and ball";

        Image positionImg = new Image(getClass().getResourceAsStream(String.format("/client/gui/res/images/%s.png",fileName)));
        positionImage.setImage(positionImg);
    }
}
