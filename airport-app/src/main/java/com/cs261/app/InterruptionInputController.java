package com.cs261.app;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class InterruptionInputController 
{
    @FXML
    private ComboBox<String> statusBox;

    @FXML
    private TextField daysField;
    @FXML
    private TextField hoursField;
    @FXML
    private TextField minutesField;

    @FXML
    private TextField durationField;


    @FXML
    public void initialize() 
    {
        statusBox.getItems().addAll(
            "Available",
            "Runway Inspection",
            "Snow Clearance",
            "Equipment Failure"
        );
    }

    public Interruption parse()
    {
        try 
        {
            int day = Integer.parseInt(daysField.getText());

            int hour = Integer.parseInt(hoursField.getText());
            if (hour < 0 || hour > 23)
            {
                return null;
            }   
            int minute = Integer.parseInt(minutesField.getText());
            if (minute < 0 || minute > 59)
            {
                return null;
            }
            int duration = Integer.parseInt(durationField.getText());
            String status = statusBox.getValue();
            if (status == null)
            {
                return null;
            }
            return new Interruption(new int[]{day, hour, minute}, duration, status);
        }
        catch (NumberFormatException e)
        {
            return null;
        }


        //return null;
    }
    
}
