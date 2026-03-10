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
    /**
    * navigates back to the start page of the application.
    * @param event the action event that triggered the navigation
    * @throws IOException if an error occurs while loading the FXML file
    */
    public void goBack(ActionEvent event) throws IOException 
    {

        Parent root = FXMLLoader.load(
        getClass().getResource("start.fxml"));
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.getScene().setRoot(root);
    }   

    /**
     * navigates to the second page of the application.
     * @param event the action event that triggered the navigation
     * @throws IOException if an error occurs while loading the FXML file
     */
    @FXML
    public void goToSecondPage(ActionEvent event) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/cs261/app/inputForm.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.getScene().setRoot(root);  
    }

    /**
     * navigates to the compare simulation page of the application.
     * @param event the action event that triggered the navigation
     * @throws IOException if an error occurs while loading the FXML file
     */
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
    /**
     * closes the application when the user clicks the close button.
     * @param event the action event that triggered the close operation
     * @throws IOException if an error occurs while closing the application
     */
    public void closeApp(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.close();
    }
}
