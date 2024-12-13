package client.gui.controller;

import client.Club;

// import client.ServerReader;
import client.ServerReader;
import client.gui.components.View;
import database.IOWrapper;
import database.Server;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import utils.HelperFunctions;
import utils.response.Action;
import utils.response.Data;
import utils.response.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginView extends View {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentView = this;
    }


    @FXML
    private TextField clubNameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private MaterialIconView passwordVisibilityIcon;

    @FXML
    private TextField textField;

    @FXML
    private TextField signUpClubNameField;

    @FXML
    private PasswordField signUpPasswordField;

    @FXML
    private TextField signUpTextField;

    @FXML
    private MaterialIconView signUpPasswordVisibilityIcon;

    @FXML
    void onSignUpButtonPressed(ActionEvent event) {

        String signUpClubName = signUpClubNameField.getText();
        String signUpPassword = signUpPasswordVisibilityIcon.getGlyphName().contains("OFF") ? signUpPasswordField.getText() : signUpTextField.getText();


        try{
            Socket socket = new Socket(Server.IP, Server.PORT);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(new Data(Action.SIGNUP,"signup",false,signUpClubName+":"+ HelperFunctions.sha256(signUpPassword)));
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Object o = ois.readObject();
            if(o instanceof Response){
                Response r = (Response)o;
                String message = r.message;
                Alert alert = new Alert(r.success ? Alert.AlertType.INFORMATION:Alert.AlertType.ERROR);
                alert.setTitle("Sign up response");
                alert.setContentText(message);
                alert.showAndWait();
                socket.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }



    }

    @FXML
    void onSignUpPasswordVisibilityIconClicked(MouseEvent event) {
        if(signUpPasswordVisibilityIcon.getGlyphName().contains("OFF")){
            String text = signUpPasswordField.getText();
            signUpTextField.setText(text);

            signUpPasswordField.setVisible(false);
            signUpTextField.setVisible(true);
            signUpPasswordVisibilityIcon.setGlyphName("VISIBILITY");
        }else{
            String text = signUpTextField.getText();
            signUpPasswordField.setText(text);

            signUpPasswordField.setVisible(true);
            signUpTextField.setVisible(false);
            signUpPasswordVisibilityIcon.setGlyphName("VISIBILITY_OFF");
        }
    }
    @FXML
    void onLoginButtonPressed(ActionEvent event) {

        String clubName = clubNameField.getText();
        String password = passwordVisibilityIcon.getGlyphName().contains("OFF") ? passwordField.getText() : textField.getText();


        try{
            Socket socket = new Socket(Server.IP, Server.PORT);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(new Data(Action.LOGIN,"login",false,clubName+":"+ HelperFunctions.sha256(password)));
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Object o = ois.readObject();
            if(o instanceof Data){

                Club club = (Club) ((Data)o).data;
                IOWrapper io = new IOWrapper(ois,oos);

                Club.setClub(club);
                ServerReader serverReader = new ServerReader(io);
                serverReader.start();
                getMainView().onLog(club.name);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/gui/res/view/player-search-view.fxml"));
                try{
                    Parent root =loader.load();
                    getMainView().borderPane.setCenter(root);
                }catch(Exception e){
                    e.printStackTrace();
                }


            }else if(o instanceof Response){

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText(((Response)o).message);
                alert.showAndWait();
                socket.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @FXML
    void onPasswordVisibilityIconClicked(MouseEvent event) {


        if(passwordVisibilityIcon.getGlyphName().contains("OFF")){
            String text = passwordField.getText();
            textField.setText(text);

            passwordField.setVisible(false);
            textField.setVisible(true);
            passwordVisibilityIcon.setGlyphName("VISIBILITY");
        }else{
            String text = textField.getText();
            passwordField.setText(text);

            passwordField.setVisible(true);
            textField.setVisible(false);
            passwordVisibilityIcon.setGlyphName("VISIBILITY_OFF");
        }

    }





}
