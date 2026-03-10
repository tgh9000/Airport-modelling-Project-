package com.cs261.app.model;

import java.util.ArrayList;
import com.cs261.app.model.AirCraft.ZoneStatus;
import com.cs261.app.model.RunWay.OperatingMode;


public class TrafficController {
	private AirCraftMap aircraftMap;
	private RunWayMap runways;
	private HoldingQueue holdingPattern;
	private TakeOffQueue takeoffQueue;
	private ArrivalModel arrivalModel;
	private DepartureModel departureModel;


	public TrafficController(AirCraftMap aircraftMap, RunWayMap runways, HoldingQueue holdingPattern,TakeOffQueue takeoffQueue, ArrivalModel arrivalModel, DepartureModel departureModel) {
		this.aircraftMap = aircraftMap;
		this.runways = runways;
		this.holdingPattern = holdingPattern;
		this.takeoffQueue = takeoffQueue;
		this.arrivalModel = arrivalModel;
		this.departureModel = departureModel;
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

		if (!holdingPattern.isEmpty()){
			handleArrivals(exitedPlanes, currentTime);
			System.out.println("handled arrivals");
			handleDiversions(currentTime); 		// handle diversions stuff here
		}
		System.out.println("arrivals");

		if (!takeoffQueue.isEmpty()){
			handleDepartures(exitedPlanes, currentTime);
			handleCancellations(currentTime); 		// handle cancellations stuff here
		}
		System.out.println("departures");

		/**
		 * Check if the planes currently occupying each unavailable runway has finished its time on the runway 
		 * so the runway can be made free
		 */
		checkBusyRunways(currentTime, OperatingMode.LANDING);
		checkBusyRunways(currentTime, OperatingMode.TAKEOFF);

		// check if any runways need to be converted because other runways have closed within the intreval [currenttime, currenttime + timeinc]
		System.out.println("updateTraffic");
		return exitedPlanes;
	}

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
				arrivalModel.updateLanded();
				exitedPlanes.add(front.getCallSign());
			}
		} else {
			/** for each available arrival runway, dequeue the holding queue.
			 * Planes do not have to wait more if there are runways immediately available. 
			 */
			System.out.println("Help me");
			while (!freeArrivals.isEmpty() && !holdingPattern.isEmpty()){
				System.out.println("Hoho");
				RunWay landing = freeArrivals.remove(0); // always pick from front of the list
				AirCraft front = holdingPattern.dequeue(); 
				if (front == null){//null guard
					break;
				}
				landing.addPlane(front); // assign the front plane to the available runway
				front.setExitTime(Utils.convertTicksToDate(currentTime)); // set exit time as the time it exits the queue
				arrivalModel.updateLanded();
				exitedPlanes.add(front.getCallSign());
			}
		}
	}

	/**
	 * Handles departures and takeoff queue according to the runway availability
	 * Dequeue takeoff queue per available arrival runway.
	 * If no departure runways are available and the front of the queue is an emergency aircraft, find a mixed runway which is available.
	 * @param exitedPlanes list of planes that have exited the simulation (because they have been assigned to a runway)
	 * @param currentTime number of ticks elapsed so far
	 */
	private void handleDepartures(ArrayList<String> exitedPlanes, int currentTime){
		ArrayList<RunWay> freeDepartures = runways.getFreeRunways(OperatingMode.TAKEOFF); // all available arrival runways (inc. mixed mode on arrival turn)
		if (freeDepartures.isEmpty()){ 				
        ArrayList<AirCraft> dangerPlanes = takeoffQueue.inCancellationDanger(currentTime);
		System.out.println("departures got runways");
		// if there are free mixed runways, put any planes in danger of cancellation on those
        for (AirCraft front : dangerPlanes) {
			System.out.println("departure loop");
            RunWay mixed = runways.getMixedRunWay();
            if (mixed != null) {
                takeoffQueue.remove(front); 
                mixed.addPlane(front);     
                front.setExitTime(Utils.convertTicksToDate(currentTime));
				departureModel.updateTakenOff();
                exitedPlanes.add(front.getCallSign());
            }
        }
		} else {
			/** for each available arrival runway, dequeue the takeoff queue.
			 * Planes do not have to wait more if there are runways immediately available. 
			 */
			while (!freeDepartures.isEmpty() && !takeoffQueue.isEmpty()){
				System.out.println("departure loop");
				RunWay takeoff = freeDepartures.remove(0); // always pick from front of the list
				AirCraft front = takeoffQueue.dequeue(); 
				if (front == null){
					break;
				}
				takeoff.addPlane(front); // assign the front plane to the available runway
				front.setExitTime(Utils.convertTicksToDate(currentTime)); // set exit time as the time it exits the queue
				departureModel.updateTakenOff();
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
		System.out.println("diversions");
		ArrayList<AirCraft> emergencies = holdingPattern.getEmergencyPlanes();
		for (AirCraft a : emergencies){ 
			System.out.println("diversion loop");
			if ((currentTime - a.getEmergencyTime()) >= Utils.emergencyTime){
				a.setExitTime(Utils.convertTicksToDate(currentTime));
				a.setZoneStatus(AirCraft.ZoneStatus.DIVERT);
			}
		}
		System.out.println("finish");
	}

	/** Check if the planes in the takeoff queue have been there longer than the maximum
	 *  cancellation time. If any have been, then cancel them.
	 * @param currentTime number of ticks elapsed so far
	 */
	private void handleCancellations(int currentTime){
		for (int i=0; i < takeoffQueue.size(); i++) {
			AirCraft a = takeoffQueue.getAircraftAt(i);
			if ((currentTime - Utils.convertDateToTicks(a.getEntryTime())) >= Utils.cancelTime){
				a.setExitTime(Utils.convertTicksToDate(currentTime));
				a.setZoneStatus(AirCraft.ZoneStatus.CANCEL);
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
package com.cs261.app.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import com.cs261.app.model.AirCraft.ZoneStatus;
import com.cs261.app.model.RunWay.OperatingMode;


public class TrafficController {
	private AirCraftMap aircraftMap;
	private RunWayMap runways;
	private HoldingQueue holdingPattern;
	private TakeOffQueue takeoffQueue;
	private ArrivalModel arrivalModel;
	private DepartureModel departureModel;


	public TrafficController(AirCraftMap aircraftMap, RunWayMap runways, HoldingQueue holdingPattern,TakeOffQueue takeoffQueue, ArrivalModel arrivalModel, DepartureModel departureModel) {
		this.aircraftMap = aircraftMap;
		this.runways = runways;
		this.holdingPattern = holdingPattern;
		this.takeoffQueue = takeoffQueue;
		this.arrivalModel = arrivalModel;
		this.departureModel = departureModel;
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
		} // if both 0, can't do anything.

		ArrayList<String> exitedPlanes = new ArrayList<>(); // list of all planes that exit their queues (take off or holding) in this iteration of the simulation

		if (!holdingPattern.isEmpty()){
			handleArrivals(exitedPlanes, currentTime);
			//System.out.println("handled arrivals");
			handleDiversions(exitedPlanes, currentTime); 	
		}
		//System.out.println("arrivals");

		if (!takeoffQueue.isEmpty()){
			handleDepartures(exitedPlanes, currentTime);
			handleCancellations(exitedPlanes, currentTime); 		// handle cancellations stuff here
		}
		//System.out.println("departures");

		/**
		 * Check if the planes currently occupying each unavailable runway has finished its time on the runway 
		 * so the runway can be made free
		 */
		checkBusyRunways(currentTime, OperatingMode.LANDING);
		checkBusyRunways(currentTime, OperatingMode.TAKEOFF);

		// check if any runways need to be converted because other runways have closed within the intreval [currenttime, currenttime + timeinc]
	//	System.out.println("updateTraffic");
		return exitedPlanes;
	}

	/**
	 * Handles arrivals and holding queue according to the runway availability
	 * Dequeue holding queue per available arrival runway.
	 * If no arrival runways are available and the front of the queue is an emergency aircraft, find a mixed runway which is available.
	 * @param exitedPlanes list of planes that have exited the simulation (because they have been assigned to a runway)
	 * @param currentTime number of ticks elapsed so far
	 */
	private void handleArrivals(ArrayList<String> exitedPlanes, int currentTime){
		try {
			ArrayList<RunWay> freeArrivals = runways.getFreeRunways(OperatingMode.LANDING); // all available arrival runways (inc. mixed mode on arrival turn)
			if (freeArrivals.isEmpty()){ 				
				if (holdingPattern.top().getEmergencyStatus() == AirCraft.EmergencyStatus.NONE) return;

				// first plane in the holding pattern has an emergency		
				RunWay mixed = runways.getMixedRunWay(); // check if a mixed runway is available regardless of turn. only one can be used at a time in this scenario, to control the disorder of mixed runways.
				if (mixed != null){ 
					AirCraft front = holdingPattern.dequeue(); // airplane exits holding pattern
					mixed.addPlane(front);
					front.setExitTime(Utils.convertTicksToDate(currentTime));
					arrivalModel.updateLanded();
					exitedPlanes.add(front.getCallSign());
				}
			} else {
				/** for each available arrival runway, dequeue the holding queue.
				 * Planes do not have to wait more if there are runways immediately available. 
				 */
				while (!freeArrivals.isEmpty() && !holdingPattern.isEmpty()){
					RunWay landing = freeArrivals.remove(0); // always pick from front of the list
					AirCraft front = holdingPattern.dequeue(); 
					landing.addPlane(front); // assign the front plane to the available runway
					front.setExitTime(Utils.convertTicksToDate(currentTime)); // set exit time as the time it exits the queue
					arrivalModel.updateLanded();
					exitedPlanes.add(front.getCallSign());
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Handles departures and takeoff queue according to the runway availability
	 * Dequeue takeoff queue per available arrival runway.
	 * If no departure runways are available and the front of the queue is an emergency aircraft, find a mixed runway which is available.
	 * @param exitedPlanes list of planes that have exited the simulation (because they have been assigned to a runway)
	 * @param currentTime number of ticks elapsed so far
	 */
	private void handleDepartures(ArrayList<String> exitedPlanes, int currentTime){
		ArrayList<RunWay> freeDepartures = runways.getFreeRunways(OperatingMode.TAKEOFF); // all available arrival runways (inc. mixed mode on arrival turn)
		if (freeDepartures.isEmpty()){ 				
			ArrayList<AirCraft> dangerPlanes = takeoffQueue.inCancellationDanger(currentTime);
			
			if (dangerPlanes.isEmpty()){
				return;
			}

			//System.out.println("departures got runways");
			// if there are free mixed runways, put any planes in danger of cancellation on those
			for (AirCraft front : dangerPlanes) {
				//System.out.println("departure loop");
				RunWay mixed = runways.getMixedRunWay();
				if (mixed != null) {
					takeoffQueue.remove(front); 
					mixed.addPlane(front);     
					front.setExitTime(Utils.convertTicksToDate(currentTime));
					departureModel.updateTakenOff();
					exitedPlanes.add(front.getCallSign());
				}
			}
		} else {
			/** for each available arrival runway, dequeue the takeoff queue.
			 * Planes do not have to wait more if there are runways immediately available. 
			 */
			while (!freeDepartures.isEmpty() && !takeoffQueue.isEmpty()){
				//System.out.println("departure loop");
				RunWay takeoff = freeDepartures.remove(0); // always pick from front of the list
				AirCraft front = takeoffQueue.dequeue(); 
				takeoff.addPlane(front); // assign the front plane to the available runway
				front.setExitTime(Utils.convertTicksToDate(currentTime)); // set exit time as the time it exits the queue
				departureModel.updateTakenOff();
				exitedPlanes.add(front.getCallSign());
			}
		}
	}

	/** Retrieve all the planes that currently have an emergency status
	* Check if they have been in emergency for longer than the maximum emergency wait time
	* If so, divert them 
	 * @param currentTime number of ticks elapsed so far
	 */
	private void handleDiversions(ArrayList<String> exitedPlanes, int currentTime){
		holdingPattern.removeDiversions(exitedPlanes, currentTime);
		//System.out.println("diversions");
	}

	/** Check if the planes in the takeoff queue have been there longer than the maximum
	 *  cancellation time. If any have been, then cancel them.
	 * @param currentTime number of ticks elapsed so far
	 */
	private void handleCancellations(ArrayList<String> exitedPlanes, int currentTime){
		for (int i=0; i < takeoffQueue.size(); i++) {
			AirCraft a = takeoffQueue.getAircraftAt(i);
			if ((currentTime - Utils.convertDateToTicks(a.getEntryTime())) >= Utils.cancelTime){
				takeoffQueue.remove(a); // remove from queue
				a.setExitTime(Utils.convertTicksToDate(currentTime));
				a.setZoneStatus(AirCraft.ZoneStatus.CANCEL);
				exitedPlanes.add(a.getCallSign());
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
