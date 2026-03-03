package com.cs261.app.UI;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cs261.app.controller.ResultsContext;
import com.cs261.app.controller.SimService;
import com.cs261.app.model.ModelOutput;
import com.cs261.app.model.RunWay;
import com.cs261.app.model.Utils;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
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

    /** Tracks the runway section nodes so we remove the correct one. */
    private final List<VBox> runwaySectionNodes = new ArrayList<>();

    @FXML
    private Label errorMessageLabel;

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

    private final List<RunwayInputController> runwaysList = new ArrayList<>();

    @FXML
    private void  checkSim()
    {

        errorMessage("loading...");
        if (runwaysList.isEmpty()) {
            errorMessage("Add at least one runway.");
            return;
        }
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
            runwayArray[i] = r;

        }

        long totalMinutes = (long) dayNum * 24 * 60 + hourNum * 60 + minuteNum;
        int runtimeTicks = (int) (totalMinutes / Utils.timeInc);
        errorMessage("Starting simulation...");
        SimService simService = new SimService();
        simService.setOnSucceeded((WorkerStateEvent e) -> {
            ModelOutput output = simService.getValue();
            if (output != null) {
                String durationText = dayNum + " day(s) " + hourNum + " hour(s) " + minuteNum + " minute(s)";
                if (dayNum >= 14) {
                    durationText = (dayNum / 7) + " week(s)";
                }
                ResultsContext.setLastResult(output, durationText);
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/com/cs261/app/kpiHome.fxml"));
                    Stage stage = (Stage) (errorMessageLabel.getScene().getWindow());
                    stage.getScene().setRoot(root);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    String msg = "Simulation complete.\nLanded: " + output.getTotalAircraftLanded()
                        + "  Taken off: " + output.getTotalAircraftTakenOff()
                        + "\nMax holding queue: " + output.getMaxHoldingQueueLength()
                        + "  Max take-off queue: " + output.getMaxTakeOffQueueLength();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Simulation complete");
                    alert.setHeaderText(null);
                    alert.setContentText(msg);
                    alert.showAndWait();
                }
            } else {
                errorMessage("Simulation finished (no output).");
            }
        });
        simService.setOnFailed((WorkerStateEvent e) -> {
            errorMessage("Simulation failed: " + (simService.getException() != null ? simService.getException().getMessage() : "unknown"));
        });
        simService.start(runwayArray, runtimeTicks, cancellationTime, (double) failureProb);
    }

    

    private ComboBox<String> statusBox;


    @FXML
    public void initialize() {
        if (errorMessageLabel != null) errorMessageLabel.setText("");
        // Don't add a runway here — let user click "Add Runway" (avoids load error)
    }
    
    public void goBack(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(
        getClass().getResource("/com/cs261/app/start.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }

    @FXML
    private VBox runwayBox;

    @FXML
    private void addSection() 
    {
        if (runwayBox == null) {
            if (errorMessageLabel != null) errorMessageLabel.setText("Form not ready.");
            return;
        }
        if (runways >= 10) {
            if (errorMessageLabel != null) errorMessageLabel.setText("Maximum 10 runways.");
            return;
        }
        runways++;
        try 
        {
            java.net.URL url = getClass().getResource("/com/cs261/app/RunwayInput.fxml");
            if (url == null) {
                errorMessage("RunwayInput.fxml not found.");
                runways--;
                return;
            }
            FXMLLoader loader = new FXMLLoader(url);
            VBox newSection = loader.load();

            RunwayInputController controller = loader.getController();
            if (controller != null) {
                runwaysList.add(controller);
                controller.setRunwayTitle("Runway " + runways);
            }

            runwaySectionNodes.add(newSection);
            int insertIndex = runwayBox.getChildren().size() - 1;
            if (insertIndex < 0) insertIndex = 0;
            runwayBox.getChildren().add(insertIndex, newSection);
            if (errorMessageLabel != null) errorMessageLabel.setText("");
        } catch (Exception e) {
            e.printStackTrace();
            runways--;
            String msg = e.getMessage();
            if (msg == null) msg = e.getClass().getSimpleName();
            Throwable cause = e.getCause();
            if (cause != null) msg += " — " + cause.getClass().getSimpleName() + ": " + cause.getMessage();
            errorMessage("Add runway failed: " + msg);
        }
    }

    

    @FXML
    private void removeSection() 
    {
        if (runwaySectionNodes.isEmpty()) return;
        VBox lastSection = runwaySectionNodes.remove(runwaySectionNodes.size() - 1);
        runwayBox.getChildren().remove(lastSection);
        if (!runwaysList.isEmpty()) runwaysList.remove(runwaysList.size() - 1);
        runways--;
    }





}
