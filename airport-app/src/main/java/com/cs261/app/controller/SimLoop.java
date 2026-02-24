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
import com.cs261.app.model.RunWay;
import com.cs261.app.model.DataGen;

import javafx.concurrent.Task;
import java.util.List;
import java.util.ArrayList;

public class SimLoop extends Task<IModelOutput>{
	protected AirCraftMap aircraftMap = new AirCraftMap();
	protected HoldingQueue holdingPattern = new HoldingQueue(500);
	protected TakeOffQueue takeOffQueue = new TakeOffQueue();
	protected RunWayMap runways;
	protected ArrivalModel arrivalModel = new ArrivalModel(aircraftMap, holdingPattern);
	protected TrafficController trafficModel;
	protected int runtime;
	protected int currentTime;
	protected DataGen dataGen;
	
	// TODO: add data gen and departure model
	
	public SimLoop(RunWay[] userRunways, int userRuntime){
		this.runways = new RunWayMap(userRunways);
		this.runtime = userRuntime;
		this.currentTime = 0;

		this.trafficModel = new TrafficController(aircraftMap, runways, holdingPattern, takeOffQueue);
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
			// TODO: departureModel.update..
			// traffic controller update, check runways and stuff
			ArrayList<String> exits = trafficModel.updateTraffic(currentTime); 
			// sort by exit time. check this sorts the right thing
			exits.sort((String a, String b) -> aircraftMap.get(a).getExitTime().compareTo(aircraftMap.get(b).getExitTime()));
			currentTime += Utils.timeInc;
			// TODO: output.update...
		}
		
		return null;
	}

}
