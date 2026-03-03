package com.cs261.app.controller;

import java.time.LocalDateTime;

import com.cs261.app.model.ModelOutput;
import com.cs261.app.model.RunWay;
import com.cs261.app.model.Utils;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class SimService extends Service<ModelOutput>{

    // properties go here
    // inputs
    private RunWay[] runwayInput;
    private int runtimeInput; // in ticks
    private double failrate;
    private int cancelTime;


    /**
     * Create background task, the simulation itself
     */
    @Override
    protected Task<ModelOutput> createTask() {
        return new SimLoop(runwayInput, runtimeInput);  
    }

    /**
     * Set parameters for task
     * @param runways
     * @param days
     * @param hours
     * @param mins
     * @param failprob
     * @param cancelTime
     */
    public void setParams(RunWay[] runways, int days, int hours, int mins, float failprob, int cancelTime){
        this.runwayInput = runways;
        this.cancelTime = cancelTime;
        this.failrate = failprob;
        Utils.setCancelTime(cancelTime);
        Utils.setEmergencyProbability((double) failprob);

        // make the date
        LocalDateTime endtime = LocalDateTime.of(2026, 1, 1, 0,0,0).plusDays((long) days).plusHours((long) hours).plusMinutes((long) mins);
        this.runtimeInput = Utils.convertDateToTicks(endtime);
    }
}
