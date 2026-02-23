package com.cs261.app;


import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class RunwayInputController 
{
    int interruption = 0;
    @FXML
    private Label runwayLabel;

    public void setRunwayTitle(String text) 
    {
        runwayLabel.setText(text);
    }

    public Runway parse()
    {
        return null;
    }

    @FXML
    private VBox interruptionBox;

     @FXML
    private void addInterruption() 
    {
        if (interruption < 3) 
        {
            interruption++;
            try 
            {
                FXMLLoader loader = new FXMLLoader
                (
                    getClass().getResource("/com/cs261/app/InterruptionInput.fxml")
                );

                VBox newSection = loader.load();


                // 🔥 Get the controller
                InterruptionInputController controller = loader.getController();

                // 🔥 Modify something inside the FXML
                //controller.setRunwayTitle("Runway " + interruption);

                int size = interruptionBox.getChildren().size();

                interruptionBox.getChildren().add(newSection);
                /*if (size >= 1) 
                {
                    interruptionBox.getChildren().add(size - 1, newSection);
                } 
                else 
                {
                    interruptionBox.getChildren().add(newSection);
                }*/

            } 

            catch (IOException e) {
                e.printStackTrace();
            }
             
        }
       
    }

    @FXML
    private void removeSection() 
    {
        if (interruption > 0) 
        {
            interruption--;
            int size = interruptionBox.getChildren().size();
            interruptionBox.getChildren().remove(size - 1);
        }
    }

    
}
