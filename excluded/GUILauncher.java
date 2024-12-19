import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GUILauncher extends Application {

    public static GUI controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gui.fxml"));
        // primaryStage.initStyle(StageStyle.UNDECORATED);
        try{
            Parent root = loader.load();
            controller = loader.getController();
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
