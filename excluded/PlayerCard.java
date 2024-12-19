import entities.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PlayerCard {

    private Player player;

    @FXML
    private Label clubLabel;

    @FXML
    private Button detailsButton;

    @FXML
    private Label nameLabel;

    @FXML
    private VBox root;

    PlayerView playerViewController;

    Parent playerView;

    @FXML
    void onDetailsButtonPressed(ActionEvent event) {

        if(playerViewController==null){
            FXMLLoader loader =new FXMLLoader(getClass().getResource("guest-player-view.fxml"));
            try{
                playerView=loader.load();
                PlayerView controller = loader.getController();
                controller.setPlayer(player);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        GUILauncher.controller.borderPane.setCenter(playerView);

    }

    public void setPlayer(Player player){


        this.player = player;
        nameLabel.setText(player.getName());
        clubLabel.setText(player.getClub());

    }

}
