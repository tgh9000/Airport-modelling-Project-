package com.cs261.app.model;

import java.util.ArrayList;
import com.cs261.app.model.AirCraft.ZoneStatus;
import com.cs261.app.model.RunWay.OperatingMode;


public class TrafficController {
	private AirCraftMap aircraftMap;
	private RunWayMap runways;
	private HoldingQueue holdingPattern;
	private TakeOffQueue takeoffQueue;
	
	public TrafficController(AirCraftMap aircraftMap, RunWayMap runways, HoldingQueue holdingPattern,TakeOffQueue takeoffQueue) {
		this.aircraftMap = aircraftMap;
		this.runways = runways;
		this.holdingPattern = holdingPattern;
		this.takeoffQueue = takeoffQueue;
	}

	/**
	 * return flights that have been exitted from the simulation
	 * we want to separate it from the aircraft map
	 * @param currentTime
	 * @return callsigns of flights that have landed or taken off (NOT including diversions and cancellations)
	 */
	public ArrayList<String> updateTraffic(int currentTime) {
		/**
		 * FIX: need to think carefully about the times noted when there is no holding queue and available runways and airplane enters sim
		 */

		// first check if any runways need a status change
		runways.addRunwaysStatusChange(currentTime);
		runways.removeRunWaysStatusChange(currentTime);

		// second check if any runways need to be converted (assume converting is instant)
		int availableArrivalCount = runways.checkAvailable(OperatingMode.LANDING);
		int availableDepartureCount = runways.checkAvailable(OperatingMode.TAKEOFF);

		if (availableArrivalCount == 0 && availableDepartureCount != 0){
			runways.getFreeRunways(OperatingMode.TAKEOFF).get(0).convertRunway(); // convert take off runway into a mixed runway
		} else if (availableArrivalCount != 0 && availableDepartureCount == 0){ 
			runways.getFreeRunways(OperatingMode.LANDING).get(0).convertRunway(); // convert landing runway into a mixed runway
		}

		ArrayList<String> exitedPlanes = new ArrayList<>(); // list of all planes that exit their queues (take off or holding) in this iteration of the simulation

		handleArrivals(exitedPlanes, currentTime);
		handleDiversions(currentTime); 		// handle diversions stuff here

		/**
		 * Check if the planes currently occupying each unavailable runway has finished its time on the runway 
		 * so the runway can be made free
		 */
		checkBusyRunways(currentTime, OperatingMode.LANDING);
		checkBusyRunways(currentTime, OperatingMode.TAKEOFF);

		// check if any runways need to be converted because other runways have closed within the intreval [currenttime, currenttime + timeinc]

		return exitedPlanes;
	}

	// TODO: converting runways to mixed
	// TODO: changing status of runways at specific times/
	// TODO: passing to output 

	/**
	 * Handles arrivals and holding queue according to the runway availability
	 * Dequeue holding queue per available arrival runway.
	 * If no arrival runways are available and the front of the queue is an emergency aircraft, find a mixed runway which is available.
	 * @param exitedPlanes list of planes that have exited the simulation (because they have been assigned to a runway)
	 * @param currentTime number of ticks elapsed so far
	 */
	private void handleArrivals(ArrayList<String> exitedPlanes, int currentTime){
		ArrayList<RunWay> freeArrivals = runways.getFreeRunways(OperatingMode.LANDING); // all available arrival runways (inc. mixed mode on arrival turn)
		if (freeArrivals.isEmpty()){ 				
			if (holdingPattern.top().getEmergencyStatus() == AirCraft.EmergencyStatus.NONE) return;

			// first plane in the holding pattern has an emergency		
			RunWay mixed = runways.getMixedRunWay(); // check if a mixed runway is available regardless of turn
			if (mixed != null){ 
				AirCraft front = holdingPattern.dequeue(); // airplane exits holding pattern
				mixed.addPlane(front);
				front.setExitTime(Utils.convertTicksToDate(currentTime));
				exitedPlanes.add(front.getCallSign());
			}
		} else {
			/** for each available arrival runway, dequeue the holding queue.
			 * Planes do not have to wait more if there are runways immediately available. 
			 */
			while (!freeArrivals.isEmpty()){
				RunWay landing = freeArrivals.get(0); // always pick from front of the list
				AirCraft front = holdingPattern.dequeue(); 
				landing.addPlane(front); // assign the front plane to the available runway
				front.setExitTime(Utils.convertTicksToDate(currentTime)); // set exit time as the time it exits the queue
				exitedPlanes.add(front.getCallSign());
			}
		}
	}

	/** Retrieve all the planes that currently have an emergency status
	* Check if they have been in emergency for longer than the maximum emergency wait time
	* If so, divert them 
	 * @param currentTime number of ticks elapsed so far
	 */
	private void handleDiversions(int currentTime){
		ArrayList<AirCraft> emergencies = holdingPattern.getEmergencyPlanes();
		for (AirCraft a : emergencies){ 
			if ((currentTime - a.getEmergencyTime()) >= Utils.emergencyTime){
				a.setExitTime(Utils.convertTicksToDate(currentTime));
				a.setZoneStatus(AirCraft.ZoneStatus.DIVERT);
			}
		}
	}

	/**
	 * check if the plane occupying each busy runway has passed its 'runway time' i.e. time taken to depart/arrive on the runway.
	 * remove planes from runway if their runway time has elapsed. 
	 * @param t current time in ticks
	 * @param mode operating mode of the runways being checked 
	 */
	private void checkBusyRunways(int t, OperatingMode mode){
		ArrayList<RunWay> busy = runways.getBusyRunways(mode);
		AirCraft curr = null;
		for (RunWay r : busy) {
			curr = aircraftMap.get(r.getCurrentPlane());
			// either we could do t - exit time or check t - exit time >= runway time || t + timeinc >= runwaytime idk, for now we keep it like this
			if (((t + Utils.timeInc) - Utils.convertDateToTicks(curr.getExitTime())) >= Utils.runwayTime){ // its finished
				r.removePlane();
				curr.setZoneStatus(ZoneStatus.EXIT);
			}
		}
	}
}
