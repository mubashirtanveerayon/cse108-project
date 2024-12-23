package client.controller;

import client.components.RequiresUpdate;
import client.components.View;
import entities.Player;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.controlsfx.control.RangeSlider;
import org.w3c.dom.ranges.Range;
import utils.Club;
import utils.attribute.Attribute;
import utils.attribute.RangeAttribute;
import utils.attribute.StringAttribute;
import utils.enums.AttributeKey;
import utils.filter.PlayerSearcher;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.ResourceBundle;

public class PlayerListView extends View implements RequiresUpdate {

    PlayerSearcher search;
    Club club;
    public HashMap<Player, PlayerCard>playerCards;
    public HashMap<CheckBox, Attribute>countryMap,positionMap,clubMap;

    @FXML
    private TextField ageMaxField;

    @FXML
    private TextField ageMinField;
    @FXML
    private TextField heightMaxField;

    @FXML
    private TextField heightMinField;
    @FXML
    private TextField salaryMaxField;

    @FXML
    private TextField salaryMinField;
    @FXML
    public VBox clubContainer;

    @FXML
    public VBox countryContainer;



    @FXML
    private TextField nameSearchField;

    @FXML
    private VBox playerContainer;

    @FXML
    public VBox positionContainer;



//    @FXML
//    private RangeSlider ageSlider,salarySlider,heightSlider;


    @FXML
    void onSearchButtonPressed(ActionEvent event) {

        Attribute name = new StringAttribute(nameSearchField.getText(),AttributeKey.NAME);


//        ArrayList<Attribute>list = new ArrayList<>();
//        list.add(name);
//        requestSearch(list);

        ArrayList<Player>filtered = search.filterPlayers(name);

        clearContainer();
        if(filtered.isEmpty())return;
        addPlayer(filtered.get(0));


    }

    public void clearContainer(){
        playerContainer.getChildren().clear();
        playerCards.clear();

    }


    @FXML
    void onNumericFilterTextFieldAction(ActionEvent event) {
        filter();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentView = this;
        club = Club.getClub();
        search = club.getSearch();
        playerCards = new HashMap<>();

        countryMap =new HashMap<>();
        positionMap = new HashMap<>();
        clubMap = new HashMap<>();


        addPlayers(club.allPlayers);
        for(Player p:club.allPlayers){
            Attribute club = p.getAttribute(AttributeKey.CLUB);
            Attribute country = p.getAttribute(AttributeKey.COUNTRY);
            Attribute position = p.getAttribute(AttributeKey.POSITION);

            if(!clubMap.containsValue(club)){
                CheckBox checkBox = createCheckBox();
                checkBox.setText(club.getContent().toString());
                clubContainer.getChildren().add(checkBox);
                clubMap.put(checkBox, club);
            }
            if(!countryMap.containsValue(country)){
                CheckBox checkBox = createCheckBox();
                checkBox.setText(country.getContent().toString());
                countryContainer.getChildren().add(checkBox);
                countryMap.put(checkBox, country);
            }
            if(!positionMap.containsValue(position)){
                CheckBox checkBox = createCheckBox();
                checkBox.setText(position.getContent().toString());
                positionContainer.getChildren().add(checkBox);

                positionMap.put(checkBox, position);
            }

        }


        //configureRangeSliders();

//        addListenerToSliders(ageSlider);
//        addListenerToSliders(salarySlider);
//        addListenerToSliders(heightSlider);

    }

//    private void addListenerToSliders(RangeSlider slider){
//        slider.lowValueChangingProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                filter();
//            }
//        });
//
//        slider.highValueChangingProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                filter();
//            }
//        });
//    }

    @FXML
    void onResetFilterPressed(ActionEvent event) {
           clearContainer();
           addPlayers(club.allPlayers);

           for(CheckBox cb:countryMap.keySet())cb.setSelected(false);
           for(CheckBox cb:positionMap.keySet())cb.setSelected(false);
           for(CheckBox cb:clubMap.keySet())cb.setSelected(false);

           ageMinField.clear();
           ageMaxField.clear();
           heightMaxField.clear();
           heightMinField.clear();
           salaryMaxField.clear();
           salaryMinField.clear();

    }

    private void addPlayers(ArrayList<Player> allPlayers) {

        for(Player player:allPlayers){
            addPlayer(player);
        }
    }

    public CheckBox createCheckBox(){
        Font font = new Font(18);
        CheckBox checkBox = new CheckBox();
        checkBox.selectedProperty().addListener(
                (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
                    filter();
                }
        );
        checkBox.setFont(font);
        return checkBox;
    }

    public void filter() {

        ArrayList<Attribute>list = new ArrayList<>();
        list.addAll(getSelectedAttributes(countryMap));
        list.addAll(getSelectedAttributes(positionMap));
        list.addAll(getSelectedAttributes(clubMap));


//        list.add(new RangeAttribute((float)ageSlider.getLowValue(),(float)ageSlider.getHighValue(),AttributeKey.AGE));
//        list.add(new RangeAttribute((float)heightSlider.getLowValue(),(float)heightSlider.getHighValue(),AttributeKey.HEIGHT));
//        list.add(new RangeAttribute((float)salarySlider.getLowValue(),(float)salarySlider.getHighValue(),AttributeKey.SALARY));

        // System.out.println(heightSlider.getHighValue());

//
        String minAgeStr = ageMinField.getText();
        String maxAgeStr = ageMaxField.getText();

        list.add(getRangedAttribute(minAgeStr,maxAgeStr,AttributeKey.AGE));

        String minHeightStr = heightMinField.getText();
        String maxHeightStr = heightMaxField.getText();

        list.add(getRangedAttribute(minHeightStr,maxHeightStr,AttributeKey.HEIGHT));

        String minSalaryStr = salaryMinField.getText();
        String maxSalaryStr = salaryMaxField.getText();

        list.add(getRangedAttribute(minSalaryStr,maxSalaryStr,AttributeKey.SALARY));

        Attribute[] attributes = new Attribute[list.size()];
        for(int i=0;i<list.size();i++)
            attributes[i] = list.get(i);

        ArrayList<Player>filtered = search.filterPlayers(attributes);

        clearContainer();
        addPlayers(filtered);


    }


//    public void configureRangeSliders(){
//        ArrayList<Player>allPlayers = Club.getClub().allPlayers;
//        Player firstPlayer = allPlayers.get(0);
//        int minAge=firstPlayer.getAge(),maxAge=firstPlayer.getAge();
//        float minHeight=firstPlayer.getHeight(),maxHeight=firstPlayer.getHeight();
//        long minSalary=firstPlayer.getSalary(),maxSalary=firstPlayer.getSalary();
//
//        for(Player p:allPlayers){
//
//            int age = p.getAge();
//            float height = p.getHeight();
//            long salary = p.getSalary();
//
//            if(age<minAge)minAge=age;
//            if(age>maxAge)maxAge=age;
//            if(height<minHeight)minHeight=height;
//            if(height>maxHeight)maxHeight=height;
//            if(salary<minSalary)minSalary=salary;
//            if(salary>maxSalary)maxSalary=salary;
//
//        }
//
//
//        ageSlider.setMin(minAge);
//        ageSlider.setMax(maxAge);
//        heightSlider.setMin(minHeight);
//        heightSlider.setMax(maxHeight);
//        salarySlider.setMin(minSalary);
//        salarySlider.setMax(maxSalary);
//
//        ageSlider.setLowValue(minAge);
//        ageSlider.setHighValue(maxAge);
//        heightSlider.setLowValue(minHeight);
//        heightSlider.setHighValue(maxHeight);
//        salarySlider.setLowValue(minSalary);
//        salarySlider.setHighValue(maxSalary);
//
//    }





    private RangeAttribute getRangedAttribute(String minStr, String maxStr, AttributeKey key){
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


    private ArrayList<Attribute> getSelectedAttributes(HashMap<CheckBox, Attribute> map) {

        ArrayList<Attribute>attributes = new ArrayList<>();
        for(CheckBox checkBox:map.keySet())
            if(checkBox.isSelected())
                attributes.add(map.get(checkBox));

        return attributes;

    }

    @Override
    public void update(Player player) {

        PlayerCard controller = playerCards.get(player);
        if(controller == null)return;
        controller.update(player);

    }

    public void addPlayer(Player player){


        if(playerCards.containsKey(player))return;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/gui/res/view/player-card.fxml"));
        try{
            Parent root = loader.load();
            PlayerCard controller = loader.getController();
            controller.setPlayer(player);
            controller.actionButton.setVisible(false);
            playerCards.put(player, controller);
            playerContainer.getChildren().add(root);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
