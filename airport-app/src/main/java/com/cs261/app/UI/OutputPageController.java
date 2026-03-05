package com.cs261.app.UI;

import com.cs261.app.model.ModelOutput;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class OutputPageController {
    public ModelOutput output;
    
    public void init(ModelOutput output){
        this.output = output;
        Globals.output = output; 

        //

        //go load the right page after this.
        try {
            switchScene();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private StackPane outputPageStackPane;

    private void switchScene() throws Exception 
    {

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/cs261/app/keyData.fxml")
        );

        Parent root = loader.load();

        Stage stage = (Stage) outputPageStackPane.getScene().getWindow();

        stage.setScene(new Scene(root));

    }
}
