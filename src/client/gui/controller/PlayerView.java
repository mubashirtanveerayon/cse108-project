package client.gui.controller;

import client.gui.components.View;
import entities.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class PlayerView extends View {

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
        ageLabel.setText("Age: "+player.getAge());
        clubLabel.setText(player.getClub());
        countryLabel.setText(player.getCountry());
        nameLabel.setText(player.getName());
        positionLabel.setText(player.getPosition());
        heightLabel.setText(String.format("Height: %.2f",player.getHeight()));
        salaryLabel.setText("Salary: "+player.getSalary());

        numberLabel.setText("");
        if(player.getNumber()>-1)
            numberLabel.setText(String.valueOf(player.getNumber()));
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

//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/gui/res/view/player-search-view.fxml"));
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



}
