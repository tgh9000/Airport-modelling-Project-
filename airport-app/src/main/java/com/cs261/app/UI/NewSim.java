package com.cs261.app.UI;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cs261.app.model.RunWay;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class NewSim 
{
    int runways = 0;
    int maxRun = 10;
    int[] len  = new int[3];
    RunWay[] runwayArray ;
    int dayNum;
    int hourNum;
    int minuteNum;
    float failureProb;
    int cancellationTime;

    @FXML
    private Label errorMessageLabel;
    /**
     * displays an error message to the user in the errorMessageLabel Label control.
     * @param message the error message to be displayed to the user
     */
    @FXML
    private void errorMessage(String message)
    {
        errorMessageLabel.setText(message);
    }

    @FXML
    private TextField simDaysField;
    @FXML
    private TextField simHoursField;
    @FXML
    private TextField simMinutesField;


    @FXML
    private TextField failureProbField;
    @FXML
    private TextField cancellationTimeField;

    @FXML
    private List<RunwayInputController> runwaysList = new ArrayList<>();
    /**
     * validates the user input for the simulation parameters and runway configurations, and if all inputs are valid, it loads the simulation waiting screen and starts the simulation with the specified parameters.
      * @param event the action event that triggered the validation and simulation start process
      * @throws IOException if an error occurs while loading the FXML file for the simulation waiting screen
     */
    @FXML
    private void  checkSim(ActionEvent event) throws IOException
    {

        errorMessage("loading...");
        String days = simDaysField.getText();
        String hours = simHoursField.getText();
        String minutes = simMinutesField.getText();
        

        //parse the initial simulation length input and check for validity
        try 
        {
            dayNum = Integer.parseInt(days);
            //System.out.println("Parsed value: " + days);
        } 
        catch (NumberFormatException e) 
        {
            errorMessage("Invalid number of days for simulation length");
            return;
        }
        try 
        {
            hourNum = Integer.parseInt(hours);
            //System.out.println("Parsed value: " + hours);
            if (hourNum < 0 || hourNum >= 24) 
            {
                errorMessage("Hours must be between 0 and 23");
                return;
            }
        } 
        catch (NumberFormatException e) 
        {
            errorMessage("Invalid number of hours for simulation length");
            return;
        }
        try 
        {
            minuteNum = Integer.parseInt(minutes);
            //System.out.println("Parsed value: " + minutes);

            if (minuteNum < 0 || minuteNum >= 60) 
            {
                errorMessage("Minutes must be between 0 and 59");
                return;
            }
        } 
        catch (NumberFormatException e) 
        {
            errorMessage("Invalid number of minutes for simulation length");
            return;
        }



        // failure probability
        try 
        {
            failureProb = Float.parseFloat(failureProbField.getText());
            if (failureProb < 0.0 || failureProb > 1.0) 
            {
                errorMessage("Failure probability must be between 0.0 and 1.0");
                return;
            }
        } 
        catch (NumberFormatException e) 
        {
            errorMessage("Invalid failure probability");
            return;
        }
        
        // cancellation time
        try 
        {
            if (cancellationTimeField.getText() == null || cancellationTimeField.getText().trim().isEmpty()) 
            {
                cancellationTime = 30;  // default value
            }
            else
            {
                cancellationTime = Integer.parseInt(cancellationTimeField.getText());
            
                if (cancellationTime < 20) 
                {
                    errorMessage("Cancellation time must be at least 20 minutes");
                    return;
                }
            }
            
        } 
        catch (NumberFormatException e) 
        {
            errorMessage("Invalid cancellation time");
            return;
        }
        int[] modeCounts = new int[3]; // 0 = landing, 1 = takeoff, 2 = mixed
        //parse each runway information
        runwayArray = new RunWay[runways];
        for(int i = 0 ; i < runwaysList.size(); i ++)
        {
            RunwayInputController temp = runwaysList.get(i);
            RunWay r = temp.parse(new int[]{dayNum, hourNum, minuteNum} );
            if (r == null)
            {
                errorMessage("Invalid input for runway " + (i + 1));
                return;
            }
            switch (r.getMode()) {
                case LANDING:
                    modeCounts[0]++;
                    break;
                case TAKEOFF:
                    modeCounts[1]++;
                    break;
                case MIXED:
                    modeCounts[2]++;
                    break;
                default:
                    break;
                    
            }

            runwayArray[i] = r;

        }
        Globals.runwayModes = modeCounts;
        errorMessage("All inputs valid! Ready to run simulation.");
        Globals.duration = dayNum + " days, " + hourNum + " hours, " + minuteNum + " minutes";
        Globals.totalTicks = String.valueOf((dayNum * 24 * 60 + hourNum * 60 + minuteNum));

        // load waiting screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cs261/app/simulationWait.fxml"));
        Parent root = loader.load();

        SimulationWaitController controller = loader.getController();
        controller.startSim(runwayArray, dayNum, hourNum, minuteNum, failureProb, cancellationTime);
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }

    

    @FXML
    private ComboBox<String> statusBox;

    /**
     * initializes the statusBox ComboBox with predefined interruption statuses such as "Runway Inspection", "Snow Clearance", and "Equipment Failure".
     */
    @FXML
    public void initialize() {
        errorMessage("");
        addSection();

    }
    
    /**
     *  navigates back to the start page of the application.
     * @param event the action event that triggered the navigation
     * @throws IOException if an error occurs while loading the FXML file for the start page
     */
    public void goBack(ActionEvent event) throws IOException 
    {

        Parent root = FXMLLoader.load(
        getClass().getResource("/com/cs261/app/start.fxml"));

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.getScene().setRoot(root);
    }   

    @FXML
    private VBox runwayBox;

    /**
     * adds a new runway configuration section to the UI, allowing the user to input information for an additional runway, up to a maximum of 10 runways. 
     */
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


                // Get the controller
                RunwayInputController controller = loader.getController();
                runwaysList.add(controller);
                //  Modify something inside the FXML
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

    
    /**
     * removes the most recently added runway configuration section from the UI, allowing the user to reduce the number of runways for the simulation, but ensuring that at least one runway configuration remains.
     */
    @FXML
    private void removeSection() 
    {
        if (runways > 1) 
        {
            runways--;
            int size = runwayBox.getChildren().size();
            runwayBox.getChildren().remove(size - 2);
            runwaysList.remove((runwaysList.size() - 1));
        }
    }





}
