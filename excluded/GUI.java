
import database.PlayerDatabase;
import entities.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import utils.attribute.Attribute;
import utils.enums.AttributeKey;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.HashMap;
public class GUI implements Initializable {
    PlayerDatabase db;
    HashMap<CheckBox, Attribute>positions,clubs,countries;

    @FXML
    SplitPane playerSearchView;
    @FXML
    BorderPane borderPane;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        positions = new HashMap<>();
        countries = new HashMap<>();
        clubs = new HashMap<>();

        PlayerDatabase.initialize();

        db = PlayerDatabase.getInstance();





        for(Player p:db.players()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("guest-player-card.fxml"));

            Attribute position = p.getAttribute(AttributeKey.POSITION);
            Attribute club = p.getAttribute(AttributeKey.CLUB);
            Attribute country = p.getAttribute(AttributeKey.COUNTRY);

            if(!positions.containsValue(position)){
                CheckBox checkBox = new CheckBox((String)position.getContent());
                positionContainer.getChildren().add(checkBox);
                positions.put(checkBox, position);
            }
            if(!clubs.containsValue(club)){
                CheckBox checkBox = new CheckBox((String)club.getContent());
                clubContainer.getChildren().add(checkBox);
                clubs.put(checkBox, club);
            }
            if(!countries.containsValue(country)){
                CheckBox checkBox = new CheckBox((String)country.getContent());
                countryContainer.getChildren().add(checkBox);
                countries.put(checkBox, country);
            }

            loadPlayer(loader,p);


        }

    }


    private void loadPlayer(FXMLLoader loader,Player player){

        try{
            Parent root= loader.load();
            PlayerCard controller = loader.getController();
            controller.setPlayer(player);
            playerContainer.getChildren().add(root);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void loadPlayers(FXMLLoader loader,ArrayList<Player> players){
        for(Player player:players)
            try{
                Parent root= loader.load();
                PlayerCard controller = loader.getController();
                controller.setPlayer(player);
                playerContainer.getChildren().add(root);
            }catch(Exception e){
                e.printStackTrace();
            }
    }


    @FXML
    private TextField ageMaxField;

    @FXML
    private TextField ageMinField;

    @FXML
    private VBox clubContainer;

    @FXML
    private VBox countryContainer;

    @FXML
    private TextField heightMaxField;

    @FXML
    private TextField heightMinField;

    @FXML
    private TextField nameSearchField;

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

    @FXML
    void onCloseClicked(MouseEvent event) {

    }

    @FXML
    void onFilterButtonPressed(ActionEvent event) {



    }

    @FXML
    void onMinimizeClicked(MouseEvent event) {

    }

    @FXML
    void onNameSearchFieldAction(ActionEvent event) {

    }

    @FXML
    void onResetButtonPressed(ActionEvent event) {

    }

    @FXML
    void onSearchButtonPressed(ActionEvent event) {

    }

    @FXML
    void onStatComboBoxAction(ActionEvent event) {

    }

    @FXML
    void onTopBarMouseDragged(MouseEvent event) {

    }

    @FXML
    void onTopBarMousePressed(MouseEvent event) {

    }

}
