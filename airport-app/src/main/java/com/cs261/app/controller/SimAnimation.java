package com.cs261.app.controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.Utilities;

import com.cs261.app.model.AirCraft;
import com.cs261.app.model.AirCraftMap;
import com.cs261.app.model.ArrivalModel;
import com.cs261.app.model.DataGen;
import com.cs261.app.model.DepartureModel;
import com.cs261.app.model.HoldingQueue;
import com.cs261.app.model.ModelOutput;
import com.cs261.app.model.RunWay;
import com.cs261.app.model.RunWayMap;
import com.cs261.app.model.TakeOffQueue;
import com.cs261.app.model.TrafficController;
import com.cs261.app.model.Utils;

import javafx.animation.AnimationTimer;

public class SimAnimation extends AnimationTimer{
	protected AirCraftMap aircraftMap = new AirCraftMap();
	protected HoldingQueue holdingPattern = new HoldingQueue(500);
	protected TakeOffQueue takeOffQueue = new TakeOffQueue();
	protected RunWayMap runways;
	protected ArrivalModel arrivalModel = new ArrivalModel(aircraftMap, holdingPattern);
	protected DepartureModel departureModel = new DepartureModel(aircraftMap, takeOffQueue);
	protected TrafficController trafficModel;
	protected int runtime;
	protected long currentTime = 0; // technically the last time
	protected DataGen dataGen;
	protected ModelOutput output;

    private int fps = 50;
    private int newTimeInc;

	/**
	 * Model inputs - runways, max cancellation time, probability of mechanical failure emergency and probability of passenger health emegerency, runtime 
	 */
	public SimAnimation(RunWay[] userRunways, int userRuntime){
		this.runways = new RunWayMap(userRunways);
		this.runtime = userRuntime;
		this.trafficModel = new TrafficController(aircraftMap, runways, holdingPattern, takeOffQueue, arrivalModel, departureModel);
		this.dataGen = new DataGen(userRuntime, Utils.flow, Utils.flow); // init with runtime and default flow rate per hour.
		this.output = new ModelOutput(aircraftMap, departureModel, arrivalModel, runways);
	
        newTimeInc = userRuntime / fps;
        Utils.setTimeInc(newTimeInc);
    }

    // one step in the animation
    private void step(){
        System.out.printf("current %d \n", currentTime);
        // get flights for this tick
        List<AirCraft> newArrivals = dataGen.getArrivalsForTick((int) currentTime, (int) currentTime + Utils.timeInc);
        List<AirCraft> newDepartures = dataGen.getDeparturesForTick((int) currentTime, (int) currentTime + Utils.timeInc);
        // arrivals update, add flights 
        arrivalModel.updateArrivals(newArrivals, (int) currentTime);
        // departures update, add flights
        departureModel.updateDepartures(newDepartures, (int) currentTime);
        // traffic controller update, check runways and stuff
        ArrayList<String> exits = trafficModel.updateTraffic((int) currentTime); 
        System.out.println("returned traffic");
        currentTime += Utils.timeInc;
        output.updateOutput(exits, holdingPattern.size(), takeOffQueue.size(), (int) currentTime);
        System.out.println("returned output");
        // now update movement 

        /**
         * 1. change holding queue top position
         * 2. change takeoff queue 
         * 3. move stuff along runways
         * 4. have properties:
         *  - HP top
         *  - planes on runways from runway map
         *  - takeoff queue obs list
         */

    }


    @Override
    public void handle(long now) {
        if (now >= (long) runtime){
            stop();
        }

        if (now - currentTime > Utils.timeInc){
            step();
            currentTime = now;
        }
    }
    
}
