package com.cs261.app.model;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ArrivalModel {
	private AirCraftMap aircraftMap;
	private HoldingQueue holdingPattern;
	private int totalArrivals = 0;
	private int landedPlanes = 0;


	public ArrivalModel(AirCraftMap map, HoldingQueue queue) {
		this.aircraftMap = map;
		this.holdingPattern = queue;
	}
	/**
	 * Update the flights that have arrived during the simulation time.
	 * @param newPlanes the new planes for arrival in the simulation
	 * @param currentTime time elapsed since the start of the simulation.
	 */
	public void updateArrivals(List<AirCraft> newPlanes, int currentTime) {		
		//System.out.printf("num planes %d \n", newPlanes.size());
		try {
			Iterator<AirCraft> i = newPlanes.listIterator();
			while (i.hasNext()){ // add new planes 
				AirCraft a = i.next();
				holdingPattern.enqueue(totalArrivals++, a);	 // increment total arrivals and add plane to holding queue
				aircraftMap.put(a);	
			}
			i = null;
			System.out.printf("num planes added %d \n", totalArrivals);
			holdingPattern.updateHolding(currentTime);
			System.out.println("updateArrivals");
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Updates the number of planes THAT HAVE ACTUALLY LANDED. therefore does not include diversions.
	 */
	public void updateLanded(){
		landedPlanes++;
		System.out.println("updated landed planes #");
	}

	/**
	 * @return the number of planes that actually landed
	 */
	public int getLanded(){
		return landedPlanes;
	}

	/**
	 * @return the number of planes in the simulation that were set for arrival
	 */
	public int getTotalArrivals(){
		return totalArrivals;
	}

	/**
	 * (for testing purposes)
	 * @return set of planes currently in the holding queue
	 */
	public Set<String> getHoldingPatternAircraftIds() {
		return holdingPattern.getAircraftIds();
	}
}
