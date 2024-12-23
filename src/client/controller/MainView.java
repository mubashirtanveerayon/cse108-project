package client.controller;

import utils.Club;
import utils.network.ServerReader;
import client.components.View;
import utils.IOWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import utils.response.Action;
import utils.response.Data;

import java.net.URL;
import java.util.ResourceBundle;

public class MainView extends View {


    double mouseX = 0, mouseY = 0;

    @FXML
    private Button myClubButton;

    @FXML
    BorderPane borderPane;

    @FXML
    Button searchPlayerButton;

    @FXML
    Button addPlayerButton;

    @FXML
    Button changePasswordButton;

    @FXML
    Label clubNameLabel;

    @FXML
    Button logoutButton;

    @FXML
    Button marketPlaceButton;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setMainView(this);
        currentView = this;
//        setBorderPane(borderPane);
        loadLoginView();
    }

    private void loadLoginView() {

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/gui/res/view/login-view.fxml"));
            Parent root = loader.load();
//            LoginView controller = loader.getController();
//            controller.setBorderPane(borderPane);
//            controller.setMainView(this);
            borderPane.setCenter(root);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onTopBarMouseDragged(MouseEvent event) {
        Stage mainStage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        mainStage.setX(event.getScreenX() - mouseX);
        mainStage.setY(event.getScreenY() - mouseY);
    }

    @FXML
    void onTopBarMousePressed(MouseEvent event) {
        mouseX = event.getSceneX();
        mouseY = event.getSceneY();
    }

    @FXML
    void onCloseClicked(MouseEvent event) {



        ServerReader sr = ServerReader.getInstance();
        if(sr != null){
            IOWrapper io = sr.getIO();
            io.write(new Data(Action.LOGOUT,"logout",false,null));
            sr.terminate();
        }

        System.exit(0);

    }
    @FXML
    void onMinimizeClicked(MouseEvent event) {
        Stage mainStage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        mainStage.setIconified(true);

    }

    @FXML
    void onAddPlayerPressed(ActionEvent event) {

        try{
            FXMLLoader loader =new FXMLLoader(getClass().getResource("/client/gui/res/view/add-player-view.fxml"));
            Parent root =loader.load();
//            ChangePasswordView controller = loader.getController();
//            controller.setBorderPane(borderPane);
            borderPane.setCenter(root);

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    void onChangePasswordPressed(ActionEvent event) {


        try{
            FXMLLoader loader =new FXMLLoader(getClass().getResource("/client/gui/res/view/change-password-view.fxml"));
            Parent root =loader.load();
//            ChangePasswordView controller = loader.getController();
//            controller.setBorderPane(borderPane);
            borderPane.setCenter(root);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void onLogoutPressed(ActionEvent event) {



        ServerReader sr = ServerReader.getInstance();
        if(sr == null)return;
        IOWrapper io =sr.getIO();

        io.write(new Data(Action.LOGOUT,"logout",false,null));

        sr.terminate();

        onLog(null);
        loadLoginView();
    }

    @FXML
    void onMarketPlacePressed(ActionEvent event) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/gui/res/view/marketplace-view.fxml"));
        try{
            Parent root = loader.load();
//            MarketplaceView controller = loader.getController();
//            controller.setBorderPane(borderPane);
            borderPane.setCenter(root);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    void onSearchPlayerPressed(ActionEvent event) {


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/gui/res/view/playerlist-view.fxml"));
        try{
            Parent root = loader.load();
//            MarketplaceView controller = loader.getController();
//            controller.setBorderPane(borderPane);
            borderPane.setCenter(root);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void onLog(String clubName){
        boolean loggedOut = clubName==null;
        searchPlayerButton.setDisable(loggedOut);
        logoutButton.setDisable(loggedOut);
        marketPlaceButton.setDisable(loggedOut);
        addPlayerButton.setDisable(loggedOut);
        changePasswordButton.setDisable(loggedOut);
        myClubButton.setDisable(loggedOut);
        if(loggedOut){
            clubNameLabel.setText("Login as club manager");
            Club.setClub(null);

        }else{
            clubNameLabel.setText(clubName);
        }
    }

    @FXML
    void onMyClubPressed(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/gui/res/view/myclub-view.fxml"));
        try{
            Parent root = loader.load();
//            MarketplaceView controller = loader.getController();
//            controller.setBorderPane(borderPane);
            borderPane.setCenter(root);
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
