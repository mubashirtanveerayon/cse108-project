package client.controller;

import utils.Club;
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

public class PlayerCard extends  View{

    private Player player;
    @FXML
    public Button actionButton;


    @FXML
    private MaterialIconView actionButtonIcon;

    @FXML
    private Label clubLabel;

    @FXML
    private Button detailsButton;

    @FXML
    private ImageView positionImage;

    @FXML
    private Label nameLabel;
    private boolean inMarketPlace;


    Club club;



    @FXML
    void onActionButtonPressed(ActionEvent event) {
        if(inMarketPlace) {



            club.forAuction.remove(player);
            ServerReader sr = ServerReader.getInstance();
            if (sr == null) return;
            IOWrapper io = sr.getIO();
            io.write(new Data(Action.BUY, "buy", false, player));

            player.toggleForSale();
            club.players.add(player);

            if (currentView instanceof MarketplaceView) {
                ((MarketplaceView) currentView).remove(player);


                player.removeAttribute(AttributeKey.CLUB);
                player.addAttribute(club.clubAttribute);
            }


        }else{
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setTitle("Sell this player");
            inputDialog.setContentText("For how much you would like to sell?");
            Optional<String> result = inputDialog.showAndWait();
            if(result.isPresent()) {
                try{
                    long price = Long.parseLong(result.get());

                    ServerReader sr = ServerReader.getInstance();
                    if (sr == null) return;
                    IOWrapper io = sr.getIO();

                    io.write(new Data(Action.SELL,String.valueOf(price),false,player));

                    club.forAuction.put(player,result.get());
                    player.toggleForSale();
                    club.players.remove(player);


                    PlayerSearchView searchView = (PlayerSearchView)View.currentView;
                    searchView.removePlayer(player);


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

    public void setPlayer(Player player,boolean inMarketPlace){
        this.inMarketPlace = inMarketPlace;
        this.player = player;
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

        if(!inMarketPlace)actionButtonIcon.setGlyphName("MONETIZATION_ON");

    }

}
