package client.controller;

import client.components.RequiresUpdate;
import client.components.View;
import entities.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import utils.enums.AttributeKey;

import java.net.URL;
import java.util.ResourceBundle;

public class PlayerView extends View implements RequiresUpdate {

    @FXML
    private Label ageLabel;

    @FXML
    private Label clubLabel;

    @FXML
    private Label countryLabel;

    @FXML
    private Label heightLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label positionLabel;

    @FXML
    private Label salaryLabel;

    @FXML
    private Label numberLabel;

    private boolean fromMarketPlace;

    Parent previous;

    View previousView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        previous = currentView.root;
        previousView = currentView;
        currentView = this;


    }

    public void setPlayer(Player player,boolean fromMarketplace){
        update(player);
        this.fromMarketPlace = fromMarketplace;
    }

    public void onBackButtonPressed(ActionEvent e){


        if(fromMarketPlace){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/gui/res/view/marketplace-view.fxml"));
            try{
                Parent root = loader.load();
                getMainView().borderPane.setCenter(root);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }else{

//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/gui/res/view/myclub-view.fxml"));
//            try{
//                Parent root = loader.load();
//                getMainView().borderPane.setCenter(root);
//            }catch(Exception ex){
//                ex.printStackTrace();
//            }

            getMainView().borderPane.setCenter(previous);
            currentView = previousView;

        }


    }


    @Override
    public void update(Player player) {
        ageLabel.setText("Age: "+player.getAge());
        clubLabel.setText(player.getClub());
        countryLabel.setText(player.getCountry());
        nameLabel.setText(player.getName());
        positionLabel.setText(player.getPosition());
        heightLabel.setText(String.format("Height: %.2f",player.getHeight()));
        salaryLabel.setText("Salary: "+player.getSalary());

        numberLabel.setText("");
        if(player.hasAttribute(AttributeKey.NUMBER))
            numberLabel.setText(String.valueOf(player.getNumber()));
    }
}
