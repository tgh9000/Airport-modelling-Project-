package com.cs261.app.UI;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Navigation 
{
    public void goBack(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(
        getClass().getResource("start.fxml"));
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.getScene().setRoot(root);
    }   

    @FXML
    public void goToSecondPage(ActionEvent event) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/cs261/app/inputForm.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.getScene().setRoot(root);  
    }

    @FXML
    public void goToCompare(ActionEvent event) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/cs261/app/compareSimPage.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        CompareSimController controller = loader.getController();
        controller.init();
        stage.getScene().setRoot(root);  
    }

    public void closeApp(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.close();
    }
}
