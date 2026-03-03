package com.cs261.app.UI;

import com.cs261.app.controller.SimService;
import com.cs261.app.model.RunWay;

import javafx.application.Platform;

public class SimulationWait {
    SimService myService;

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
    
        // start the simulation once scene is rendered
        Platform.runLater(() -> myService.start());
    }
}
