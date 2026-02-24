package com.cs261.app.model;

import java.util.Set;

// TODO need to add where time enter queue stored
// TODO test getTakeoffQueueAircraftIds

public class DepartureModel {
	private AirCraftMap aircraftMap;
	private TakeOffQueue takeoffQueue;
	private IModelOutput output = null;
	private int planesTakenOff = 0;
	
	public DepartureModel(AirCraftMap map, TakeOffQueue queue) {
		this.aircraftMap = map;
		this.takeoffQueue = queue;
	}
	
	// public void updateDepartures(AirCraft newPlane, int currentTime, int prevTime) {
	// 	takeoffQueue.enqueue(newPlane);
	// }

	// adds a plane to the takeoff queue
	public void clearedForDeparture(AirCraft newPlane, int currentTime, int prevTime) {
		takeoffQueue.enqueue(newPlane);
	}

	// removes a plane from the takeoff queue
	public void planeTakesOff(AirCraft newPlane, int currentTime, int prevTime) {
		// TODO add to AirCraftMap?
		takeoffQueue.dequeue();
		planesTakenOff++;
	}

	// returns the set of all the aircraft ids in the takeoff queue
	public Set<String> getTakeOffQueueAircraftIds() {
		return takeoffQueue.getAircraftIds();
	}

	// gets the max number of planes that have been in the takeoff queue
	public int getMaxTakeOffQueueLength() {
		return takeoffQueue.getMaxQueueLength();
	}

	public int getTotalAircraftTakenOff() {
    return planesTakenOff;
	}
}
