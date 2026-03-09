package com.cs261.app.UI;

import java.io.IOException;

import com.cs261.app.controller.SimService;
import com.cs261.app.model.ModelOutput;
import com.cs261.app.model.RunWay;

import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

public class SimulationWaitController {
    public SimService myService;
    @FXML
    public ProgressBar simProgressWait;

    /**
     * Initialise the parameters of simulation from input form 
     * @param runways the runway configurations
     * runtime params: runtime is sum of these
     * @param days 
     * @param hours 
     * @param mins 
     * @param failprob the probability of a mechanical or health failure happening
     * @param cancelTime the maximum wait time until departure cancellation
     */
    public void startSim(RunWay[] runways, int days, int hours, int mins, float failprob, int cancelTime){
        myService = new SimService();
        myService.setParams(runways, days, hours, mins, failprob, cancelTime);
        simProgressWait.progressProperty().bind(myService.getTaskProgress());

        // start the simulation once scene is rendered
        Platform.runLater(() -> myService.start());

        myService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            public void handle(WorkerStateEvent event) {
                System.out.println("success event");
                ModelOutput output = myService.getValue();

                try {
                    System.out.println("success");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cs261/app/outputPage.fxml"));
                    Parent p = loader.load();
                    Stage stage = (Stage) simProgressWait.getScene().getWindow();
                    stage.getScene().setRoot(p);

                    OutputPageController outputController = loader.getController();
                    outputController.init(output);
                } catch (Exception e){
                    e.printStackTrace();
                    // code to close program or restart task
                }
            };
        });



    }
}
