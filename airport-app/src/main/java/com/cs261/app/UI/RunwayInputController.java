package com.cs261.app.UI;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cs261.app.model.RunWay;
import com.cs261.app.model.Utils;

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
    /**
     * Loads the correct values into the Combo Box.
     */
    @FXML
    public void initialize() 
    {
        modeBox.getItems().addAll(
            "Landing",
            "Takeoff",
            "Mixed"
        );
    }

    
    /**
     * Parses all the correct data for a given runway input.
     */
    public RunWay parse(int[] end)
    {
        int duration = getTick(end);

        //length
        String lengthText = lengthField.getText();
        int length;
        try 
        {
            length = Integer.parseInt(lengthText);
        }
        catch (NumberFormatException e)
        {
            return null;
        }


        //bearing
        String bearingText = bearingField.getText();
        int bearing;

        try 
        {
            bearing = Integer.parseInt(bearingText);
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
        RunWay.OperatingMode modeEnum= null; 
        if (mode.equals("Landing"))
        {
            modeEnum = RunWay.OperatingMode.LANDING;
        }
        else if (mode.equals("Takeoff"))
        {
            modeEnum = RunWay.OperatingMode.TAKEOFF;
        }
        else if (mode.equals("Mixed"))
        {
            modeEnum = RunWay.OperatingMode.MIXED;
        }


        
        
        //Interruption[] interruptions = new Interruption[interruptionList.size()];
        RunWay.OperationStatus[] changeStatusTo = new RunWay.OperationStatus[interruptionList.size()];
        int[][] changeStatusTimes = new int[interruptionList.size()][2];
        for(int i = 0 ; i < interruptionList.size(); i++)
        {
            Interruption temp = interruptionList.get(i).parse();

            if (temp == null)
            {
                
                return null;
            }
            switch (temp.getStatus()) 
            {
                case "INSPECTION":
                    changeStatusTo[i] = RunWay.OperationStatus.INSPECTION;
                    break;
                case "SNOW": 
                    changeStatusTo[i] = RunWay.OperationStatus.SNOW;
                    break;
                
                default:
                    changeStatusTo[i] = RunWay.OperationStatus.EQUIPFAIL;
                    break;
            }
            changeStatusTimes[i][0] = getTick( temp.getTime());
            changeStatusTimes[i][1] = changeStatusTimes[i][0] + temp.getDuration()/2;
            if (changeStatusTimes[i][1] > duration)
            {
                System.out.println("Interruption " + (i+1) + " ends after the simulation ends. Please adjust the duration or the interruption time/duration.");
                return null;
            }

            //returning.addInterruption(interruptions[i]);
            //interruptions[i].print();
        }
        RunWay returning = new RunWay(length, bearing, modeEnum, changeStatusTo, changeStatusTimes);
        return returning;


        // new Runway(interruptions);
    }
    /**
     * turns the number of days, minutes 
     */
    public int getTick( int[] end)
    {

        return (end[0] * 24 * 60 + end[1] * 60 + end[2])/Utils.timeInc;
    }

    @FXML
    private VBox interruptionBox;
    /**
     * loads an extra Interuption input to the form when the user presses a button 
     */
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

                

                int size = interruptionBox.getChildren().size();

                interruptionBox.getChildren().add(newSection);
                

            } 

            catch (IOException e) {
                e.printStackTrace();
            }
             
        }
       
    }

    @FXML
    /**
     * removes the last interuption input from the Runway.
     */
    private void removeSection() 
    {
        if (interruption > 0) 
        {
            interruption--;
            int size = interruptionBox.getChildren().size();
            interruptionBox.getChildren().remove(size - 1);
            interruptionList.remove(interruption);
        }
    }

    
}
