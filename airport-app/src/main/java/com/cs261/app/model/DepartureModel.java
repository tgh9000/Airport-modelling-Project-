package com.cs261.app.model;

import java.util.Set;

// TODO need to add where time enter queue stored
// TODO test getTakeoffQueueAircraftIds and other things

// TODO the point of this is to find out which planes have been cancelled

public class DepartureModel {
	private AirCraftMap aircraftMap;
	private TakeOffQueue takeoffQueue;
	private IModelOutput output = null;
	
	public DepartureModel(AirCraftMap map, TakeOffQueue queue) {
		this.aircraftMap = map;
		this.takeoffQueue = queue;
	}

	/**
	 * Adds a new aircraft to the takeoff queue.
	 *
	 * @param newPlane the aircraft to be added to the takeoff queue
	 * @param currentTime the current simulation time
	 */
	public void updateDepartures(AirCraft newPlane, int currentTime) {
		takeoffQueue.enqueue(newPlane);
		aircraftMap.put(newPlane);
	}

	/**
	 * Returns the set of all aircraft ids currently in the takeoff queue.
	 *
	 * @return a set containing the ids of all aircraft in the takeoff queue
	 */
	public Set<String> getTakeOffQueueAircraftIds() {
		return takeoffQueue.getAircraftIds();
	}

}
