package client.gui.controller;

import client.Club;
import client.ServerReader;
import client.gui.components.View;
import database.IOWrapper;
import entities.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import utils.HelperFunctions;
import utils.response.Action;
import utils.response.Data;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddPlayerView extends View {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentView = this;

        positionComboBox.getItems().addAll("Batsman","Bowler","Wicketkeeper","Allrounder");
    }

    @FXML
    private TextField ageField;

    @FXML
    private TextField countryField;

    @FXML
    private TextField heightField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField numberField;

    @FXML
    private ComboBox<String> positionComboBox;

    @FXML
    private TextField salaryField;

    @FXML
    void onAddButtonPressed(ActionEvent event) {


        String name = nameField.getText();
        String country = countryField .getText();

        String position = positionComboBox.getValue();

        String ageStr = ageField.getText();
        String heightStr = heightField.getText();
        String salaryStr = salaryField.getText();

        if(name.isBlank() || country.isBlank()|| position == null || position.isBlank() || ageStr.isBlank() || heightStr.isBlank() || salaryStr.isBlank()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please fill all the mandatory ('*' marked) fields");
            alert.showAndWait();
            return;

        }


        int age = (int) HelperFunctions.getValue(ageStr);

        float height = HelperFunctions.getValue(heightStr);
        long salary = (long)HelperFunctions.getValue(salaryStr);
        if(age<0 || height <0 || salary<0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Invalid input");
            alert.showAndWait();
            return;
        }

        String numberStr = numberField.getText();
        int number = (int)HelperFunctions.getValue(numberStr);




        String club = Club.getClub().name;

        Player player = null;
        if(number<0)
            player = new Player(name,country,age,height,club,position,salary);
        else player = new Player(name,country,age,height,club,position,number,salary);

        IOWrapper io = ServerReader.getInstance().getIO();
        io.write(new Data(Action.ADD,"add",false,player));

    }


    public void loadSearchView() {

        FXMLLoader loader =new FXMLLoader(getClass().getResource("/client/gui/res/view/player-search-view.fxml"));
        try{
            Parent root = loader.load();
            getMainView().borderPane.setCenter(root);

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
