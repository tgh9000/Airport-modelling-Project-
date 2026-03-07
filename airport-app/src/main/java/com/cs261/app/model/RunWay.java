package com.cs261.app.model;

import java.time.LocalDateTime;

/**
 * Represents each runway
 */
public class RunWay {
	/**
	 * Represents the operating mode of the runway
	 * The code each value represents to the same flight type in the AirCraft class.
	 */
	public static enum OperatingMode {
		LANDING(1),
		TAKEOFF(2),
		MIXED(3),
		;
		private final int code;
		private OperatingMode(int code){
			this.code = code;
		}
		public int getCode(){
			return this.code;
		}
	}
	
	/**
	 * Represents the operational status of the runway
	 */
	public static enum OperationStatus {
		AVAILABLE,
		INSPECTION,
		SNOW,
		EQUIPFAIL,
		;
	}
	
	private String runwayNumber; // runway number
	private int length; // does not affect simulation
	private int bearing;
	private String currentPlane = null; // occupying aircraft callsign
	private OperationStatus status;
	private OperatingMode mode;

	private OperatingMode mixedModeTurn;
	// can have up to 3 status changes
	private final OperationStatus[] changeStatusTo;
	private final int[][] changeStatusTimes; // changeStatusTimes[0][0] = start time of interruption 1, changeStatusTimes[0][1] = end time of interruption 1,etc, in simulation ticks
	
	public RunWay(int length, int  bearing, OperatingMode mode, OperationStatus[] change, int[][] timeAt) {
		this.length = length;
		this.bearing = bearing;
		this.mode = mode;
		this.status = OperationStatus.AVAILABLE; // always starts as available
		this.runwayNumber = calcID(this.bearing);
		this.changeStatusTo = change;
		this.changeStatusTimes = timeAt;
		if (mode == OperatingMode.MIXED) {
			this.mixedModeTurn = OperatingMode.LANDING; // mixed runways start with arrivals
		} else {
			this.mixedModeTurn = mode; // if the runway is not a mixed runway, the mixed mode turn is the mode of the runway and it never changes
		}
	}

	/**
	 * Calculate runway number, e.g. bearing = 124, runwayNumber =  "12"
	 * @param bearing
	 * @return runway number (which is the runway id) based on its bearing
	 */
	private static String calcID(int bearing){
		int number = Math.round(bearing / 10.0f); // divide by 10 and round to round to nearest 10 degrees, and as math.round returns integer truncates last digit
		String id;
		if (number < 10 && number >= 0) { // convert to string, but add 0 if the rounded and truncated bearing is a single digit
			id = "0" + Integer.toString(number);
		} else {
			id = Integer.toString(number);
		}
		return id;
	}

	/**
	 * Assign an occupying plane to the runway
	 * @param currentPlane the plane to set
	 * @return true if plane is set, false otherwise
	 */
	public boolean addPlane(AirCraft plane) {
		if (plane == null){
			return false;
		}

		if (plane.getFlightType().getCode() == mixedModeTurn.getCode()){ // check the plane is of the correct type for the runway
			this.currentPlane = plane.getCallSign();
			plane.addToRunway(runwayNumber); // update plane with the runway it is occupying

			if (this.mode == OperatingMode.MIXED) {
				switch (this.mixedModeTurn){
					case OperatingMode.LANDING:
						this.mixedModeTurn = OperatingMode.TAKEOFF;
						break;
					case OperatingMode.TAKEOFF:
						this.mixedModeTurn = OperatingMode.LANDING;
						break;
					default:
						break; // dont change because its an error
				}
			}
		}
		System.out.println("added plane");
		return true;
	}

	/**
	 * Remove plane that currently occupies the runway
	 * @return plane removed
	 */
	public String removePlane(){
		String temp = this.currentPlane;
		this.currentPlane = null;
		this.status = OperationStatus.AVAILABLE;
		return temp;
	}

	/**
	 * Checks if the current runway is occupied by a plane
	 * @return true if it is, false otherwise
	 */
	public boolean hasPlane(){
		return currentPlane != null;
	}

	/**
	 * Checks if a runway will have a status change or not during the simulation
	 * @return true if it will have a status change, false otherwise
	 */
	public boolean hasStatusChange(){
		return changeStatusTo.length != 0;
	}

	/**
	 * Change status to whatever the user input is at the right time
	 * @param time number of ticks passed in simulation currently
	 */
	public void addStatus(int time){
		if (hasStatusChange()){
			// if it should be changed within the intreval 
			for (int i = 0; i < changeStatusTimes.length; i++) {
				if (time <= changeStatusTimes[i][0] && changeStatusTimes[i][0] <= time + Utils.timeInc){
					this.setStatus(changeStatusTo[i]);
					break;
				}
			}
		} 
	}

	/**
	 * Change status back to available at the user inputted time
	 * @param time current time in simulation
	 */
	public void removeStatus(int time){
		if (hasStatusChange()){
			for (int i = 0; i < changeStatusTimes.length; i++) {
				if (time <= changeStatusTimes[i][1] && changeStatusTimes[i][1] <= time + Utils.timeInc){
					this.setStatus(OperationStatus.AVAILABLE);
					break;
				}
			}
		}
	}

	/**
	 * Convert a runway into a mixed runway
	 */
	public void convertRunway(){
		this.mode = OperatingMode.MIXED; // mixed mode turn stays the same as the original mode for the next turn
	}

	/**
	 * @return the mixedModeTurn
	 */
	public OperatingMode getMixedModeTurn() {
		return mixedModeTurn;
	}

	/**
	 * @return the currentPlane
	 */
	public String getCurrentPlane() {
		return currentPlane;
	}

	/**
	 * @return the operating status
	 */
	public OperationStatus getStatus() {
		return status;
	}

	/**
	 * @return status the runway would change to
	 */
	public OperationStatus[] getChangedStatus(){
		return changeStatusTo;
	}

	/**
	 * @return start time at which status would change
	 */
	public LocalDateTime getStatusChangeStartTime(int i){
		return Utils.convertTicksToDate(changeStatusTimes[i][0]); // is this correct? was showing error.
	}

	/**
	 * @return end time at which status would change
	 */
	public LocalDateTime getStatusChangeEndTime(int i){
		return Utils.convertTicksToDate(changeStatusTimes[i][1]);
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(OperationStatus status) {
		this.status = status;
	}

	/**
	 * @return the runwayNumber
	 */
	public String getRunwayNumber() {
		return runwayNumber;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @return the bearing
	 */
	public int getBearing() {
		return bearing;
	}

	/**
	 * @return the operating mode
	 */
	public OperatingMode getMode() {
		return mode;
	}

}
