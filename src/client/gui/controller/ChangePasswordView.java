package client.gui.controller;

import client.ServerReader;
import client.gui.components.View;
import database.IOWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import utils.HelperFunctions;
import utils.response.Action;
import utils.response.Data;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswordView extends View {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentView = this;
    }

    @FXML
    private PasswordField currentPassPasswordField;

    @FXML
    private TextField currentPassTextField;

    @FXML
    private MaterialIconView currentPassVisibilityIcon;

    @FXML
    private PasswordField newPassPasswordField;

    @FXML
    private TextField newPassTextField;

    @FXML
    private MaterialIconView newPassVisibilityIcon;

    @FXML
    void onPasswordVisibilityIconClicked(MouseEvent event) {

        MaterialIconView icon = (MaterialIconView) event.getSource();
        TextField textField;
        PasswordField passwordField;

        if(icon == currentPassVisibilityIcon){
            textField = currentPassTextField;
            passwordField = currentPassPasswordField;
        }else{
            textField = newPassTextField;
            passwordField = newPassPasswordField;
        }
        if(icon.getGlyphName().contains("OFF")){
            String text = passwordField.getText();
            textField.setText(text);

            passwordField.setVisible(false);
            textField.setVisible(true);
            icon.setGlyphName("VISIBILITY");
        }else{
            String text = textField.getText();
            passwordField.setText(text);

            passwordField.setVisible(true);
            textField.setVisible(false);
            icon.setGlyphName("VISIBILITY_OFF");
        }

    }

    @FXML
    void onResetButtonPressed(ActionEvent event) {

        String currentPassword = currentPassVisibilityIcon.getGlyphName().contains("OFF") ? currentPassPasswordField.getText() : currentPassTextField.getText();
        String newPassword = newPassVisibilityIcon.getGlyphName().contains("OFF") ? newPassPasswordField.getText() : newPassTextField.getText();

        currentPassTextField.setText("");
        newPassTextField.setText("");
        currentPassPasswordField.setText("");
        newPassPasswordField.setText("");


        Data data = new Data(Action.RESET_PASSWORD,"change",false, HelperFunctions.sha256(currentPassword)+":"+HelperFunctions.sha256(newPassword));




        ServerReader sr = ServerReader.getInstance();
        if(sr == null)return;
        IOWrapper  io  = sr.getIO();
        io.write(data);
        System.out.println( "Request sent");



//        Object o=io.read();
//        if(!(o instanceof Response))return;
//
//        Response r = (Response) o;
//        Alert alert = new Alert((r.isSuccess() ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR));
//        alert.setContentText(r.getMessage());
//        alert.showAndWait();






    }



}
