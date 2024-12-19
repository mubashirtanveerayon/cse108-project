package client.controller;

import utils.Club;
import utils.network.ServerReader;
import client.components.View;
import utils.IOWrapper;
import entities.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import utils.filter.PlayerSearcher;
import utils.attribute.Attribute;
import utils.attribute.RangeAttribute;
import utils.attribute.StringAttribute;
import utils.enums.AttributeKey;
import utils.response.Action;
import utils.response.Data;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class PlayerSearchView extends View {

    @FXML
    private TextField ageMaxField;

    @FXML
    private TextField ageMinField;

    @FXML
    private TextField nameSearchField;

    @FXML
    private VBox countryContainer;

    @FXML
    private TextField heightMaxField;

    @FXML
    private TextField heightMinField;

    @FXML
    private VBox playerContainer;

    @FXML
    private VBox positionContainer;

    @FXML
    private TextField salaryMaxField;

    @FXML
    private TextField salaryMinField;

    @FXML
    private ComboBox<String> statComboBox;
    private Club club   ;
    private HashMap<CheckBox, Attribute> positionMap,countryMap;

    HashMap<Player,Parent>playerCards;

    private PlayerSearcher search;

    public void requestSearch(ArrayList<Attribute> attributes){

        IOWrapper io = ServerReader.getInstance().getIO();
        io.write(new Data(Action.SEARCH,"search",false,attributes));
    }

    @FXML
    void onNameSearchFieldAction(ActionEvent event) {

        Attribute name = new StringAttribute(nameSearchField.getText(),AttributeKey.NAME);

//        ArrayList<Attribute>list = new ArrayList<>();
//        list.add(name);
//        requestSearch(list);


        ArrayList<Player>filtered = search.filterPlayers(name);
        if(filtered.isEmpty())return;

        clearContainer();
        addPlayer(filtered.get(0));


    }

    @FXML
    void onFilterButtonPressed(ActionEvent event) {
        nameSearchField.clear();

        ArrayList<Attribute>list = new ArrayList<>();

        for(CheckBox cb:positionMap.keySet())
            if(cb.isSelected())list.add(positionMap.get(cb));
        for(CheckBox cb:countryMap.keySet())

            if (cb.isSelected()) list.add(countryMap.get(cb));




        String minAgeStr = ageMinField.getText();
        String maxAgeStr = ageMaxField.getText();

        list.add(getRangedAttribute(minAgeStr,maxAgeStr,AttributeKey.AGE));

        String minHeightStr = heightMinField.getText();
        String maxHeightStr = heightMaxField.getText();

        list.add(getRangedAttribute(minHeightStr,maxHeightStr,AttributeKey.HEIGHT));

        String minSalaryStr = salaryMinField.getText();
        String maxSalaryStr = salaryMaxField.getText();

        list.add(getRangedAttribute(minSalaryStr,maxSalaryStr,AttributeKey.SALARY));
        // requestSearch(list);
        Attribute[] attributes = new Attribute[list.size()];
        for(int i=0;i<list.size();i++)
            attributes[i] = list.get(i);

        ArrayList<Player>filtered = search.filterPlayers(attributes);
        clearContainer();
        addPlayers(filtered);


    }

    private RangeAttribute getRangedAttribute(String minStr,String maxStr,AttributeKey key){
        float min = getValue(minStr);
        float max = getValue(maxStr);
        min = Math.max(0,min);
        max = max < 0 ? Float.MAX_VALUE : max;
        return new RangeAttribute(min,max,key);
    }



    private float getValue(String valueStr){
        if(valueStr.isEmpty() || valueStr.isBlank())return -1;
        try{
            float value = Float.parseFloat(valueStr);
            return value;
        }catch(Exception e){}
        return -1;
    }

    @FXML
    void onResetButtonPressed(ActionEvent event) {
        nameSearchField.clear();
        clearContainer();
        addPlayers(club.players);

        for(CheckBox cb:positionMap.keySet())cb.setSelected(false);
        for(CheckBox cb:countryMap.keySet())cb.setSelected(false);

        ageMinField.clear();
        ageMaxField.clear();
        heightMinField.clear();
        heightMaxField.clear();
        salaryMinField.clear();
        salaryMaxField.clear();

        statComboBox.setPromptText("Statistics");

    }

    @FXML
    void onSearchButtonPressed(ActionEvent event) {

        Attribute name = new StringAttribute(nameSearchField.getText(),AttributeKey.NAME);


//        ArrayList<Attribute>list = new ArrayList<>();
//        list.add(name);
//        requestSearch(list);

        ArrayList<Player>filtered = search.filterPlayers(name);
        if(filtered.isEmpty())return;

        clearContainer();
        addPlayer(filtered.get(0));


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentView = this;

        club = Club.getClub();
        playerCards=new HashMap<>();



        positionMap = new HashMap<CheckBox,Attribute>();
        countryMap = new HashMap<>();

        statComboBox.getItems().clear();
        statComboBox.getItems().addAll("Players with maximum age","Players with maximum salary","Players with maximum height","Total yearly salary");




        if(club.players.isEmpty())return;

        Font font = new Font(18);

//        int maxAge = club.players.get(0).getAge();
//        float maxHeight = club.players.get(0).getHeight();
//        long maxSalary = club.players.get(0).getSalary();

        for(Player player:club.players){
            Attribute position = player.getAttribute(AttributeKey.POSITION);
            Attribute country = player.getAttribute(AttributeKey.COUNTRY);
            if(!positionMap.containsValue(position)){
                CheckBox checkBox = new CheckBox(position.getContent().toString());
                checkBox.setFont(font);
                positionMap.put(checkBox, position);

                positionContainer.getChildren().add(checkBox);

            }
            if(!countryMap.containsValue(country)){
                CheckBox checkBox = new CheckBox(country.getContent().toString());
                checkBox.setFont(font);
                countryMap.put(checkBox,country);
                countryContainer.getChildren().add(checkBox);
            }


            addPlayer(player);

//            if(maxAge<player.getAge())maxAge=player.getAge();
//            if(maxHeight<player.getHeight())maxHeight=player.getHeight();
//            if(maxSalary<player.getSalary())maxSalary=player.getSalary();

        }

        search = Club.getClub().getSearch();



    }

    public void onStatComboBoxAction(ActionEvent e){
        String selected = statComboBox.getValue();

//        IOWrapper io = ServerReader.getInstance().getIO();
        if(selected.contains("maximum")){
            AttributeKey key = AttributeKey.AGE;
            if(selected.contains("height"))key = AttributeKey.HEIGHT;
            else if(selected.contains("salary"))key=AttributeKey.SALARY;
//            if(io != null){
//                io.write(new Data(Action.SEARCH_MAX,"max search",false,key));
//            }

            clearContainer();
            addPlayers(search.getPlayersWithMaximum(club.name,key));

        }else{
//            io.write(new Data(Action.TOTAL_YEARLY_SALARY,"total yearly salary",false,Action.TOTAL_YEARLY_SALARY));


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Total yearly salary");
            alert.setContentText(""+club.getTotalYearlySalary());
            alert.showAndWait();

        }


    }

    public void addPlayer(Player player){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/gui/res/view/player-card.fxml"));
        try{
            Parent root = loader.load();
            PlayerCard controller = loader.getController();
            controller.setPlayer(player,false);
            controller.actionButton.setText("Sell");
            playerContainer.getChildren().add(root);

            playerCards.put(player,root);

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void clearContainer(){
        playerCards.clear();
        playerContainer.getChildren().clear();
    }

    public void addPlayers(ArrayList<Player>players){
        for(Player player:players)
            addPlayer(player);
    }

    public void removePlayer(Player player){
        Parent view = playerCards.get(player);


        playerContainer.getChildren().remove(view);
        playerCards.remove(player);
    }
}
