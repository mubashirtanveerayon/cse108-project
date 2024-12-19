package client.components;

import client.controller.MainView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public abstract class View  implements Initializable {

    private static MainView mainView;
    public static View currentView;


    @FXML
    public Parent root;

    protected void setMainView(MainView mainView) {
        if(mainView == null)return;
        View.mainView = mainView;
    }

    protected MainView getMainView() {
        return mainView;
    }


//    public BorderPane getBorderPane(){
//        if(mainView == null)return null;
//        return mainView.getBorderPane();
//    }

//    protected BorderPane root;
//    public BorderPane getBorderPane(){
//        return this.root;
//    }
//
//    public void setBorderPane(BorderPane borderPane){
//        root=borderPane;
//    }
//
//
//
//
//
//    public static View getCurrentView(){
//        return currentView;
//    }
}
