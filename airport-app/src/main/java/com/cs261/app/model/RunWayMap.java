package com.cs261.app.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import com.cs261.app.model.RunWay.OperatingMode;
import com.cs261.app.model.RunWay.OperationStatus;

/**
 * Stores an array of runways. 
 * Fixed size, can't or remove runways after simulation has been started/configuration of airport has been chosen
 */
public class RunWayMap {
	private RunWay[] runways;
	
	// note that all these runways have operational status AVAILABLE (ie. can be/is being used by a plane, not closed)
	private ArrayList<RunWay> busyDepart = new ArrayList<>();
	private ArrayList<RunWay> busyArrive = new ArrayList<>();

	private ArrayList<RunWay> freeDepart = new ArrayList<>();
	private ArrayList<RunWay> freeArrive = new ArrayList<>();

	// details 
	private int numArrivalRunways;	
	private int numDepartureRunways;
	private int numMixedRunways;

	public RunWayMap(RunWay[] runwayList) {
		this.runways = runwayList;

		// this includes mixed runways as it is checking mixed mode turn
		for (int i = 0; i < runways.length; i++){
			if (runways[i].getMixedModeTurn() == OperatingMode.LANDING){
				if (runways[i].getMode() == OperatingMode.MIXED){
					numMixedRunways++;
				} else {
					numArrivalRunways++;
				}				
				freeArrive.add(runways[i]);
			}
		}

		for (int i = 0; i < runways.length; i++){
			if (runways[i].getMixedModeTurn() == OperatingMode.TAKEOFF){
				numDepartureRunways++;
				freeDepart.add(runways[i]);
			}
		}
	}
		
	/**
	 * Retrieve RunWay object from array
	 * @param number runway number of the requested runway
	 * @return RunWay object requested or none if no such runway exists
	 */
	public RunWay getRunway(String number) {
		for (int i = 0; i < runways.length; i++) {
			if (runways[i].getRunwayNumber().equals(number)) return runways[i];
		}
		return null;
	}
	
	/**
	 * @return Get list of free arrival runways
	 */
	public ArrayList<RunWay> getFreeArrive() {
		return freeArrive;
	}

	/**
	 * @return Get list of free departure runways
	 */
	public ArrayList<RunWay> getFreeDepart() {
		return freeDepart;
	}

	/**
	 * @return Get list of busy arrival runways
	 */
	public ArrayList<RunWay> getBusyArrive() {
		return busyArrive;
	}

	/**
	 * @return Get list of busy departure runways
	 */
	public ArrayList<RunWay> getBusyDepart() {
		return busyDepart;
	}

	/**
	 * Retrieve an available mixed runway regardless of its turn
	 * @return
	 */
	public RunWay getMixedRunWay(){
		for (int i = 0; i < runways.length; i++){
			if (runways[i].getMode() == OperatingMode.MIXED && runways[i].getStatus() == OperationStatus.AVAILABLE){
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
	 * Swap runway from one list to another
	 * @param removeFrom list r is being removed from
	 * @param addTo list r is being added to
	 * @param r runway being swapped 
	 */
	public void swap(ArrayList<RunWay> removeFrom, ArrayList<RunWay> addTo, RunWay r){
		removeFrom.remove(r);
		addTo.add(r);
	}

	/**
	 * Check if status change should be added for all runways
	 * @param time
	 */
	public void addRunwaysStatusChange(int time){
		for (RunWay r : runways){
			r.addStatus(time);

			if (r.getMixedModeTurn() == OperatingMode.LANDING){
				freeArrive.remove(r);
			} else {
				freeDepart.remove(r);
			}
		}
	}

	/**
	 * Check if the status change should be removed for all runways
	 * @param time 
	 */
	public void removeRunWaysStatusChange(int time){
		for (RunWay r : runways){
			r.removeStatus(time);
			
			if (r.getMixedModeTurn() == OperatingMode.LANDING){
				freeArrive.add(r);
			} else {
				freeDepart.add(r);
			}
		}
	}

	/**
	 * @param turn the turn of the mixed runways
	 * @return the number of mixed runways that have operational status available
	 */
	public int numOpenMixed(OperatingMode turn){
		int c = 0;
		for (RunWay r : runways){
			if (r.getMode() == OperatingMode.MIXED && r.getStatus() != OperationStatus.AVAILABLE && r.getMixedModeTurn() == turn){
				c++;
			}
		}
		return c;
	}

	/**
	 * @param turn the turn of the mixed runways
	 * @return the number of mixed runways that are unavailable because of operational status
	 */
	public int numClosedMixed(OperatingMode turn){
		int c = 0;
		for (RunWay r : runways){
			if (r.getMode() == OperatingMode.MIXED && r.getStatus() == OperationStatus.AVAILABLE && r.getMixedModeTurn() == turn){
				c++;
			}
		}
		return c;
	}

	/**
	 * @return the number of available arrival runways
	 */
	public int numOpenArrival(){
		return freeArrive.size() + busyArrive.size() - numOpenMixed(OperatingMode.LANDING);
	}

	/**
	 * @return number of closed arrival runways
	 */
	public int numClosedArrival(){
		return numArrivalRunways - numOpenArrival();
	}

	/**
	 * @return the number of available departure runways
	 */
	public int numOpenDeparture(){
		return freeDepart.size() + busyDepart.size() - numOpenMixed(OperatingMode.TAKEOFF);
	}

	/**
	 * @return number of closed departure runways
	 */
	public int numClosedDeparture(){
		return numDepartureRunways - numOpenDeparture();
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
