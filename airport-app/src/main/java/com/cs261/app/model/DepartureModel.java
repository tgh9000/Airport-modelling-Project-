package com.cs261.app.model;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Handle adding airplanes into departures
 */
public class DepartureModel {
	private AirCraftMap aircraftMap;
	private TakeOffQueue takeoffQueue;
	private int totalDepartutes = 0;
	private int takenOffPlanes = 0;
	
	public DepartureModel(AirCraftMap map, TakeOffQueue queue) {
		this.aircraftMap = map;
		this.takeoffQueue = queue;
	}

	/**
	 * Adds a new aircraft to the takeoff queue.
	 *
	 * @param newPlanes the aircrafts to be added to the takeoff queue
	 * @param currentTime the current time elapsed in the simulation 
	 */
	public void updateDepartures(List<AirCraft> newPlanes, int currentTime) {
		try {
			Iterator<AirCraft> i = newPlanes.iterator();
			while (i.hasNext()){
				AirCraft a = i.next();
				takeoffQueue.enqueue(a);
				aircraftMap.put(a);
				totalDepartutes++;
			}
			
			i = null;
			System.out.println("updateDepartures");
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * @return total departures processed in the system overall (inc cancellations and stuff)
	 */
	public int getTotalDepartures(){
		return totalDepartutes;
	}

	/**
	 * Updates the number of planes THAT HAVE ACTUALLY TAKEN OFF. therefore does not include cancellations.
	 */
	public void updateTakenOff(){
		takenOffPlanes++;
	}

	/**
	 * @return planes that actually took off
	 */
	public int getTakenOff(){
		return takenOffPlanes;
	}

	/**
	 * Returns the set of all aircraft ids currently in the takeoff queue.
	 *
	 * @return a set containing the ids of all aircraft in the takeoff queue
	 */
	public Set<String> getTakeOffQueueAircraftIds() {
		return takeoffQueue.getAircraftIds();
	}
	
	// /**
	//  * Returns the maximum length the takeoff queue has reached.
	//  *
	//  * @return maximum recorded takeoff queue length
	//  */
	// public int getMaxTakeOffQueueLength() {
	// 	return takeoffQueue.getMaxQueueLength();
	// }

}
