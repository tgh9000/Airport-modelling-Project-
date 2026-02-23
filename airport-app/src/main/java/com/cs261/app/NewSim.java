package com.cs261.app;


import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class NewSim 
{
    int runways = 0;
    int maxRun = 10;
    int[] len  = new int[3];
    Runway[] runwayList ;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private void errorMessage(String message)
    {
        errorMessageLabel.setText(message);
    }

    @FXML
    private void checkSim()
    {

    }

    @FXML
    private ComboBox<String> modeBox;

    @FXML
    private ComboBox<String> statusBox;


    @FXML
    public void initialize() {
        /*statusBox.getItems().addAll(
            "Available",
            "Runway Inspection",
            "Snow Clearance",
            "Equipment Failure"
        );
        modeBox.getItems().addAll(
            "Landing",
            "Takeoff",
            "Mixed"
        );*/
        errorMessage("");
        addSection();

    }
    
    public void goBack(ActionEvent event) throws IOException 
    {

        Parent root = FXMLLoader.load(
        getClass().getResource("start.fxml"));

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.getScene().setRoot(root);
    }   

    @FXML
    private VBox runwayBox;

    @FXML
    private void addSection() 
    {
        if (runways < 10) 
        {
            runways++;
            try 
            {
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/cs261/app/RunwayInput.fxml")
                );

                VBox newSection = loader.load();


                // 🔥 Get the controller
                RunwayInputController controller = loader.getController();

                // 🔥 Modify something inside the FXML
                controller.setRunwayTitle("Runway " + runways);

                int size = runwayBox.getChildren().size();

                if (size >= 1) 
                {
                    runwayBox.getChildren().add(size - 1, newSection);
                } 
                else 
                {
                    runwayBox.getChildren().add(newSection);
                }

            } 

            catch (IOException e) {
                e.printStackTrace();
            }
             
        }
       
    }

    

    @FXML
    private void removeSection() 
    {
        if (runways > 1) 
        {
            runways--;
            int size = runwayBox.getChildren().size();
            runwayBox.getChildren().remove(size - 2);
        }
    }





}
