package com.cs261.app.model;

import java.util.ArrayList;
import com.cs261.app.model.AirCraft.ZoneStatus;
import com.cs261.app.model.RunWay.OperatingMode;


public class TrafficController {
	private AirCraftMap aircraftMap;
	private RunWayMap runways;
	private HoldingQueue holdingPattern;
	private TakeOffQueue takeoffQueue;
	private IModelOutput modelOutput;
	
	public TrafficController(AirCraftMap aircraftMap, RunWayMap runways, HoldingQueue holdingPattern,TakeOffQueue takeoffQueue) {
		this.aircraftMap = aircraftMap;
		this.runways = runways;
		this.holdingPattern = holdingPattern;
		this.takeoffQueue = takeoffQueue;
	}
	/**
	 * @return the modelOutput
	 */
	public IModelOutput getModelOutput() {
		return modelOutput;
	}

	public void updateTraffic(int currentTime) {
		/**
		 * FIX: need to think carefully about the times noted when there is no holding queue and available runways and airplane enters sim
		 */

		runways.addRunwaysStatusChange(currentTime);
		runways.removeRunWaysStatusChange(currentTime);

		ArrayList<RunWay> freeArrivals = runways.getFreeArrive(); // all available arrival runways (inc. mixed mode on arrival turn)
		ArrayList<RunWay> freeDepart = runways.getFreeDepart(); // all available departure runways (inc. mixed mode runways on departure turn)

		ArrayList<String> exitedPlanes = new ArrayList<>(); // list of all planes that exit their queues (take off or holding) in this iteration of the simulation

		handleArrivals(freeArrivals, exitedPlanes, currentTime);
		handleDiversions(currentTime);

		/**
		 * Check if the planes currently occupying each unavailable runway has finished its time on the runway 
		 * so the runway can be made free
		 */
		ArrayList<RunWay> busyArrivals = runways.getBusyArrive(); // list of all arrival runways that are occupied currently
		ArrayList<RunWay> busyDepart = runways.getBusyDepart(); // list of all departure runways that are occupied currently
		checkBusyRunways(busyArrivals, currentTime, OperatingMode.LANDING);
		checkBusyRunways(busyDepart, currentTime, OperatingMode.TAKEOFF);
	}

	/**
	 * Handles arrivals and holding queue according to the runway availability
	 * Dequeue holding queue per available arrival runway.
	 * If no arrival runways are available and the front of the queue is an emergency aircraft, find a mixed runway which is available.
	 * @param freeArrivals list of available arrival runways
	 * @param exitedPlanes list of planes that have exited the simulation (because they have been assigned to a runway)
	 * @param currentTime number of ticks elapsed so far
	 */
	private void handleArrivals(ArrayList<RunWay> freeArrivals, ArrayList<String> exitedPlanes, int currentTime){
		if (freeArrivals.isEmpty()){ 
			if (holdingPattern.top().getEmergencyStatus() != AirCraft.EmergencyStatus.NONE){ // first plane in the holding pattern has an emergency
				RunWay mixed = runways.getMixedRunWay(); // check if a mixed runway is available regardless of turn
				if (mixed != null){ 
					AirCraft front = holdingPattern.dequeue(); // airplane exits holding pattern
					mixed.addPlane(front);
					runways.swapFreeRunway(mixed, mixed.getMixedModeTurn());
					front.setExitTime(Utils.convertTicksToDate(currentTime));
					exitedPlanes.add(front.getCallSign());
				} 
			}
		} else {
			/** for each available arrival runway, dequeue the holding queue.
			 * Planes do not have to wait more if there are runways immediately available. 
			 */
			while (!freeArrivals.isEmpty()){
				RunWay landing = freeArrivals.get(0); // always pick from front of the list
				AirCraft front = holdingPattern.dequeue(); 
				landing.addPlane(front); // assiggn the front plane to the available runway
				runways.swapFreeRunway(landing, OperatingMode.LANDING); // runway is no longer available, so swap from list of free runways to list of busy runways
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
	 * @param runwayList list of busy runways to be checked
	 * @param t current time in ticks
	 * @param mode operating mode of the runways being checked 
	 */
	private void checkBusyRunways(ArrayList<RunWay> runwayList, int t, OperatingMode mode){
		AirCraft curr = null;
		for (RunWay r : runwayList) {
			curr = aircraftMap.get(r.getCurrentPlane());
			if ((t - Utils.convertDateToTicks(curr.getExitTime())) >= Utils.runwayTime){ // its finished
				r.removePlane();
				curr.setZoneStatus(ZoneStatus.EXIT);
				runways.swapBusyRunway(r, mode);
			}
		}
	}
}
