package com.cs261.app;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


public class RunwayInputController 
{
    int interruption = 0;
    @FXML
    private Label runwayLabel;
    List<InterruptionInputController> interruptionList = new ArrayList<>();

    public void setRunwayTitle(String text) 
    {
        runwayLabel.setText(text);
    }

    @FXML
    private TextField lengthField;

    @FXML
    private TextField bearingField;

    @FXML 
    private ComboBox<String> modeBox;

    @FXML
    public void initialize() 
    {
        modeBox.getItems().addAll(
            "Landing",
            "Takeoff",
            "Mixed"
        );
    }

    public Runway parse()
    {
        //length
        String lengthText = lengthField.getText();
        float length;
        try 
        {
            length = Float.parseFloat(lengthText);
        }
        catch (NumberFormatException e)
        {
            return null;
        }


        //bearing
        String bearingText = bearingField.getText();
        float bearing;

        try 
        {
            bearing = Float.parseFloat(bearingText);
        }
        catch (NumberFormatException e)
        {
            return null;
        }

        //operating mode
        String mode = modeBox.getValue();
        if (mode == null)
        {
            return null;
        }

        

        Runway returning = new Runway(length, bearing, mode);
        Interruption[] interruptions = new Interruption[interruptionList.size()];
        for(int i = 0 ; i < interruptionList.size(); i++)
        {
            interruptions[i] = interruptionList.get(i).parse();
            if (interruptions[i] == null)
            {
                
                return null;
            }
            returning.addInterruption(interruptions[i]);
        }
        return returning;


        // new Runway(interruptions);
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


                InterruptionInputController controller = loader.getController();
                interruptionList.add(controller);

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
