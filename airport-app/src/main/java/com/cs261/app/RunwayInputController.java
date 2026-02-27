package com.cs261.app;


import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.cs261.app.model.RunWay;

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

    public int getTick(int[]start, int[] end)
    {
        int dayDiff = end[0] - start[0];
        int hourDiff = end[1] - start[1];
        int minuteDiff = end[2] - start[2];

        return (dayDiff * 24 * 60 + hourDiff * 60 + minuteDiff)/2;
    }

    public RunWay parse(int[] start,int duration)
    {
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
            changeStatusTimes[i][0] = getTick(start, temp.getTime());
            changeStatusTimes[i][1] = changeStatusTimes[i][0] + temp.getDuration()/2;

            //returning.addInterruption(interruptions[i]);
            //interruptions[i].print();
        }
        RunWay returning = new RunWay(length, bearing, modeEnum, changeStatusTo, changeStatusTimes);
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


                // 🔥 Get the controller
                InterruptionInputController controller = loader.getController();
                interruptionList.add(controller);

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
            interruptionList.remove(interruption);
        }
    }

    
}
