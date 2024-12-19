

import entities.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class PlayerView {

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
    private Label numberLabel;

    @FXML
    private Label positionLabel;

    @FXML
    private AnchorPane root;

    @FXML
    private Label salaryLabel;

    @FXML
    void onBackButtonPressed(ActionEvent event) {

        GUILauncher.controller.borderPane.setCenter(GUILauncher.controller.playerSearchView);

    }

    public void setPlayer(Player player){
        ageLabel.setText(Integer.toString(player.getAge()));
        clubLabel.setText(player.getClub());
        countryLabel.setText(player.getCountry());
        nameLabel.setText(player.getName());

        if(player.getNumber()>-1)
            numberLabel.setText(Integer.toString(player.getNumber()));
        positionLabel.setText(player.getPosition());
        salaryLabel.setText(String.valueOf(player.getSalary()));
    }

}
