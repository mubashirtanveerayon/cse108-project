package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import utils.IOWrapper;
import utils.network.ServerReader;
import utils.response.Action;
import utils.response.Data;

public class Client extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.initStyle(StageStyle.UNDECORATED);
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/gui/res/view/main-view.fxml"));
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root));

            primaryStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
            primaryStage.show();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void closeWindowEvent(WindowEvent event) {

        ServerReader sr = ServerReader.getInstance();
        if(sr == null) {
            System.exit(0);
            return;
        }
        IOWrapper io =sr.getIO();

        io.write(new Data(Action.LOGOUT,"logout",false,null));

        sr.terminate();

        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }
}
