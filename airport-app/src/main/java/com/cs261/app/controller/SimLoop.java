package com.cs261.app.controller;

import com.cs261.app.model.AirCraftMap;
import com.cs261.app.model.ArrivalModel;
import com.cs261.app.model.Utils;
import com.cs261.app.model.HoldingQueue;
import com.cs261.app.model.IModelOutput;
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
	private AirCraftMap aircraftMap;
	private HoldingQueue holdingPattern;
	private TakeOffQueue takeOffQueue;
	private RunWayMap runways;
	private ArrivalModel arrivalModel;
	private TrafficController trafficModel;
	private int runtime;
	private int currentTime;
	private DataGen dataGen;
	
	// TODO: add data gen and departure model
	
	public SimLoop(RunWay[] userRunways, int userRuntime){
		this.runways = new RunWayMap(userRunways);
		this.runtime = userRuntime;
		this.currentTime = 0;
		this.aircraftMap = new AirCraftMap();
		this.holdingPattern = new HoldingQueue(500);
		// TODO: take off queue,datagen, departure model
		this.arrivalModel = new ArrivalModel(aircraftMap, holdingPattern);
		this.trafficModel = new TrafficController(aircraftMap, runways, holdingPattern, takeOffQueue);
		this.dataGen = new DataGen(userRuntime, 15, 15); // init with runtime and default flow rate per hour.
	}
	
	@Override
	protected IModelOutput call() throws Exception {
		// TODO Auto-generated method stub
		while (currentTime != runtime) {
			// get flights for this tick
			List<AirCraft> newArrivals = dataGen.getArrivalsForTick(currentTime, currentTime+Utils.timeInc);
			List<AirCraft> newDepartures = dataGen.getDeparturesForTick(currentTime, currentTime+Utils.timeInc);
			
			// arrivals update, add flights 
			arrivalModel.updateArrivals(newArrivals, currentTime);
			// departures update, add flights
			
			// traffic controller update, check runways and stuff
			ArrayList<String> exits = trafficModel.updateTraffic(currentTime); 


			currentTime += Utils.timeInc;
		}
		
		return null;
	}

}
