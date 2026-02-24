package com.cs261.app.model;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ArrivalModel {
	private AirCraftMap aircraftMap;
	private HoldingQueue holdingPattern;
	private IModelOutput output = null;
	private int totalArrivals;


	public ArrivalModel(AirCraftMap map, HoldingQueue queue) {
		this.aircraftMap = map;
		this.holdingPattern = queue;
		totalArrivals = 0;
	}
	
	public void updateArrivals(List<AirCraft> newPlanes, int currentTime) {
		
		/**
		 * TODO: (Rhys) need to think about what to set emergency time as when it enters the sim as health/mech ie how to pass in current time
		 * TODO: need to add stuff for output
		*/
		
		// add new planes 
		Iterator<AirCraft> i = newPlanes.listIterator();
		while (i.hasNext()){
			AirCraft a = i.next();
			holdingPattern.enqueue(totalArrivals++, a);	 // increment total arrivals and add plane to holding queue
			aircraftMap.put(a);	
		}
		i = null;
		holdingPattern.updateHolding(currentTime);
	}

	public Set<String> getHoldingPatternAircraftIds() {
		return holdingPattern.getAircraftIds();
	}
}
