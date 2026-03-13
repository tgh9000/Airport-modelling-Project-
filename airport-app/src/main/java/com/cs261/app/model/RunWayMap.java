package com.cs261.app.model;

import java.util.ArrayList;

import com.cs261.app.model.RunWay.OperatingMode;
import com.cs261.app.model.RunWay.OperationStatus;

/**
 * Stores an array of runways. 
 * Fixed size, can't or remove runways after simulation has been started/configuration of airport has been chosen
 */
public class RunWayMap {
	private RunWay[] runways;
	private int numArrivalRunways;	
	private int numDepartureRunways;
	private int numMixedRunways;

	public RunWayMap(RunWay[] runwayList) throws IllegalArgumentException {
		ArrayList<String> newBearings = new ArrayList<>();
		runways = new RunWay[runwayList.length];

		for (int i = 0; i < runwayList.length; i++){
			if (newBearings.contains(runwayList[i].getRunwayNumber())){
				throw new IllegalArgumentException("Two runways may not have the same bearing or runway number.");
			} else {
				newBearings.add(runwayList[i].getRunwayNumber());
				runways[i]  = runwayList[i];
				switch (runwayList[i].getMode()){
				case OperatingMode.LANDING:
					numArrivalRunways++;
					break;
				case OperatingMode.TAKEOFF:
					numDepartureRunways++;
					break;
				case OperatingMode.MIXED:
					numMixedRunways++;
					break;
			}
			}
		}
	}
		
	/**
	 * Retrieve RunWay object from array
	 * @param number runway number of the requested runway
	 * @return RunWay object requested or null if no such runway exists
	 */
	public RunWay getRunway(String number) {
		for (int i = 0; i < runways.length; i++) {
			if (runways[i].getRunwayNumber().equals(number)) return runways[i];
		}
		return null;
	}

	/**
	 * Retrieve an available mixed runway regardless of its turn
	 * @return
	 */
	public RunWay getMixedRunWay(){
		for (int i = 0; i < runways.length; i++){
			if (runways[i].getMode() == OperatingMode.MIXED && runways[i].getStatus() == OperationStatus.AVAILABLE && !runways[i].hasPlane()){
				return runways[i];
			}
		}
		return null;
	}

	/**
	 * @return number of runways 
	 */
	public int size(){
		return runways.length;
	}

	/**
	 * Check if status change should be added for all runways
	 * @param time
	 */
	public void addRunwaysStatusChange(int time){
		for (int r = 0; r < runways.length; r++){
			runways[r].addStatus(time);
		}
	}

	/**
	 * Check if the status change should be removed for all runways
	 * @param time 
	 */
	public void removeRunWaysStatusChange(int time){
		for (int r = 0; r < runways.length; r++){
			runways[r].removeStatus(time);
		}
	}

	/**
	 * Returns the number of runways an arrival/departure flight can land on (Inc. mixed)
	 * @param mode type of flight that can land (corresponding to type of runway)
	 * @return number of runways available
	 */
	public int checkAvailable(OperatingMode mode){
		int count = 0;
		for (int r = 0; r < runways.length; r++){
			if ((runways[r].getMode() == mode || runways[r].getMode() == OperatingMode.MIXED) && runways[r].getStatus() == OperationStatus.AVAILABLE){
				count++;
			} 
		}
		return count;
	}

	/**
	 * Get all runways of a specific type that are available for landing/takeoff
	 * @param mode type of runways to return
	 * @return list of free runways
	 */
	public ArrayList<RunWay> getFreeRunways(OperatingMode mode){
		ArrayList<RunWay> result = new ArrayList<>();
		for (int r = 0; r < runways.length; r++){
			if (runways[r].getMixedModeTurn() == mode && runways[r].getStatus() == OperationStatus.AVAILABLE && !runways[r].hasPlane()){
				result.add(runways[r]);
			}
		}
		return result;
	}

	/**
	 * Get all runways of a specific type that are not available for landing/takeoff
	 * @param mode type of runways to return
	 * @return list of busy runways
	 */
	public ArrayList<RunWay> getBusyRunways(OperatingMode mode){
		ArrayList<RunWay> result = new ArrayList<>();
		for (int r = 0; r < runways.length; r++){
			if (runways[r].getMixedModeTurn() == mode && runways[r].getStatus() == OperationStatus.AVAILABLE && runways[r].hasPlane()){
				result.add(runways[r]);
			}
		}
		return result;
	}

	/**
	 * @return number of arrival runways
	 */
	public int getNumArrivals(){
		return numArrivalRunways;
	}

	/**
	 * @return number of mixed runways
	 */
	public int getNumMixed(){
		return numMixedRunways;
	}

	/**
	 * @return number of depature runways
	 */
	public int getNumDepart(){
		return numDepartureRunways;
	}

	/**
	 * @return list of all runways
	 */
	public RunWay[] getRunways() {
		return runways; 
	}
}
