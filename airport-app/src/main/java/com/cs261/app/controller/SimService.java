package com.cs261.app.controller;

import com.cs261.app.model.ModelOutput;
import com.cs261.app.model.RunWay;
import com.cs261.app.model.Utils;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class SimService extends Service<ModelOutput>{

    // properties go here
    // inputs
    private RunWay[] runwayInput;
    private int runtimeInput;

    @Override
    protected Task<ModelOutput> createTask() {
        return new SimLoop(runwayInput, runtimeInput);  
    }
    
    public void start(RunWay[] r, int t, int c, double e){
        runwayInput = r;
        runtimeInput = t;
        Utils.setCancelTime(c);
        Utils.setEmergencyProbability(e);
        start();
    }
}
