package com.cs261.app.UI;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Navigation 
{
    public void goBack(ActionEvent event) throws IOException 
    {

        Parent root = FXMLLoader.load(
        getClass().getResource("start.fxml"));

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.getScene().setRoot(root);
    }   

    public void goToSecondPage(ActionEvent event) throws IOException 
    {

        
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("inputForm.fxml"));

        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.getScene().setRoot(root);   
    }


    public void closeApp(ActionEvent event) throws IOException 
    {
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.close();
    }

}
