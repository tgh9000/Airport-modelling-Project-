package com.cs261.app.controller;

import com.cs261.app.model.AirCraftMap;
import com.cs261.app.model.ArrivalModel;
import com.cs261.app.model.Utils;
import com.cs261.app.model.HoldingQueue;
import com.cs261.app.model.IModelOutput;
import com.cs261.app.model.ModelOutput;
import com.cs261.app.model.RunWay;
import com.cs261.app.model.RunWayMap;
import com.cs261.app.model.TakeOffQueue;
import com.cs261.app.model.TrafficController;
import com.cs261.app.model.AirCraft;
import com.cs261.app.model.DataGen;
import com.cs261.app.model.DepartureModel;

import javafx.concurrent.Task;
import java.util.List;
import java.util.ArrayList;

public class SimLoop extends Task<IModelOutput>{
	protected AirCraftMap aircraftMap = new AirCraftMap();
	protected HoldingQueue holdingPattern = new HoldingQueue(500);
	protected TakeOffQueue takeOffQueue = new TakeOffQueue();
	protected RunWayMap runways;
	protected ArrivalModel arrivalModel = new ArrivalModel(aircraftMap, holdingPattern);
	protected DepartureModel departureModel = new DepartureModel(aircraftMap, takeOffQueue);
	protected TrafficController trafficModel;
	protected int runtime;
	protected int currentTime;
	protected DataGen dataGen;
	protected ModelOutput output = new ModelOutput(departureModel, arrivalModel, runways, holdingPattern, takeOffQueue);

	// TODO: add data gen and departure model
	
	public SimLoop(RunWay[] userRunways, int userRuntime){
		this.runways = new RunWayMap(userRunways);
		this.runtime = userRuntime;
		this.currentTime = 0;

		this.trafficModel = new TrafficController(aircraftMap, runways, holdingPattern, takeOffQueue, arrivalModel, departureModel);
		this.dataGen = new DataGen(userRuntime, 15, 15); // init with runtime and default flow rate per hour.
	}
	
	@Override
	protected ModelOutput call() throws Exception {
		while (currentTime != runtime) {
			// get flights for this tick
			List<AirCraft> newArrivals = dataGen.getArrivalsForTick(currentTime, currentTime+Utils.timeInc);
			List<AirCraft> newDepartures = dataGen.getDeparturesForTick(currentTime, currentTime+Utils.timeInc);
			// arrivals update, add flights 
			arrivalModel.updateArrivals(newArrivals, currentTime);
			// departures update, add flights
			departureModel.updateDepartures(newDepartures, currentTime);
			// traffic controller update, check runways and stuff
			ArrayList<String> exits = trafficModel.updateTraffic(currentTime); 
			currentTime += Utils.timeInc;
			output.updateOutput(exits, holdingPattern.size(), takeOffQueue.size());
		}
		
		return output;
	}

}
