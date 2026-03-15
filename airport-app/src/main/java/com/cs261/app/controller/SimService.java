package com.cs261.app.controller;

import java.time.LocalDateTime;

import com.cs261.app.model.ModelOutput;
import com.cs261.app.model.RunWay;
import com.cs261.app.model.Utils;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class SimService extends Service<ModelOutput>{
    private RunWay[] runwayInput;
    private int runtimeInput; // in ticks
    private DoubleProperty taskProgress = new SimpleDoubleProperty(0);
    /**
     * Create background task, the simulation itself
     */
    @Override
    protected Task<ModelOutput> createTask() {
        SimLoop myTask = new SimLoop(runwayInput, runtimeInput);
        taskProgress.bind(myTask.progressProperty());
        return myTask;
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
    public void setParams(RunWay[] runways, int days, int hours, int mins, float failprob, int cancelTime, int arrivalFlow, int departureFlow){
        this.runwayInput = runways;
        Utils.setCancelTime(cancelTime);
        Utils.setEmergencyProbability((double) failprob);
        Utils.setArrivalFlow(arrivalFlow);
        Utils.setDepartureFlow(departureFlow);
        // make the date
        LocalDateTime endtime = LocalDateTime.of(2026, 1, 1, 0,0,0).plusDays((long) days).plusHours((long) hours).plusMinutes((long) mins);
        this.runtimeInput = Utils.convertDateToTicks(endtime);
    }

    public DoubleProperty getTaskProgress(){
        return taskProgress;
    }
}
