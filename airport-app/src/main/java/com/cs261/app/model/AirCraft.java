package com.cs261.app.model;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;

import com.cs261.app.UI.AirCraftDetail;

/**
 * Represents each flight in the simulation. 
 */

public class AirCraft {
	
	/**
	 * Represents what 'zone' in the simulation the aircraft is in
	 */
    public static enum ZoneStatus{
        EXIT, // as in has exited the simulation (will not be considered again until output)
        QUEUE,
        CANCEL,
        DIVERT,
        RUNWAY,
        ;
    }
	/**
	 * Represents what type of flight it is.
	 * The integer values are the same as the corresponding types in OperatingMode in RunWay class
	 */
    public static enum FlightType{
        ARRIVAL(1),
        DEPARTURE(2),
        ;
		private final int code;
		private FlightType(int code){
			this.code = code;
		}
		public int getCode(){
			return this.code;
		}
    }
	/**
	 * Represents the emergency status of the aircraft.
	 */
    public static enum EmergencyStatus 
	{
        NONE,
        HEALTH,
        FUEL,
        MECH,
        ;
    }



    private String callSign; // id
    private String operator;
    private String origin;
    private String destination;
    private LocalDateTime scheduled; // SCHEDULED arrival/departure time
    private LocalDateTime entryTime; // ACTUAL arrival/departure time accounting for variance by normal distribution as per spec
    private LocalDateTime exitTime = null; // Time it leaves the holding queue or take off queue 
    private double altitude; // metres
    private double fuel; // fuel remaining in litres
    private double speed; // ground speed in knots
    private EmergencyStatus emergencyStatus;
    private ZoneStatus zoneStatus = ZoneStatus.QUEUE; 
    private FlightType flightType; 
    private String assignedRunway = null;
	private int emergencyTimeAt; // the number of ticks (runtime) at which it went into emergency


    public AirCraft(FlightType type, String callsign, String operator, String origin, String dest, LocalDateTime scheduled, LocalDateTime entryTime, float speed, float fuel, float alt, EmergencyStatus status, int emergencyTime){
        flightType = type;
        callSign = callsign;
        this.operator = operator;
        this.origin = origin;
        destination = dest;
        this.scheduled = scheduled;
        this.entryTime = entryTime;
        this.speed = speed;
        this.fuel = fuel;
        this.altitude = alt;
        this.emergencyStatus = status;
		this.emergencyTimeAt = emergencyTime;
    }
    /**
	 * @return the entryTime
	 */
	public LocalDateTime getEntryTime() {
		return entryTime;
	}
	/**
	 * @return the exitTime
	 */
	public LocalDateTime getExitTime() {
		return exitTime;
	}

	public AirCraftDetail toDetail()
	{
		FlightType tempType = getFlightType();
		String typeString = "";
		switch(tempType){
			case ARRIVAL :
				typeString = "Arrival";
				break;
			default:
				typeString = "Departure";
				break;
		}
		EmergencyStatus tempStatus = getEmergencyStatus();
		String statusString = "";
		switch(tempStatus)
		{
			case NONE :
				statusString = "N/A";
				break;
			case HEALTH:
				statusString = "Health";
				break;
			case FUEL :
				statusString = "Low Fuel";
				break;
			default:
				statusString = "Mechanical Failure";
				break;
		}
		String startStatus = "N/A";
		if(statusString != "N/A")
		{
			startStatus =String.valueOf( Utils.convertTicksToDate( getEmergencyTime()));

		}
		return new AirCraftDetail(getCallSign(), getOperator(), getOrigin(), getDestination(), typeString, statusString, startStatus);


	}

	/**
	 * @param exitTime the exitTime to set
	 * @param boolean true if it was set, false otherwise
	 */
	public boolean setExitTime(LocalDateTime exitTime) {
		if (exitTime == null){
			return false; 
		}
		this.exitTime = exitTime;

		if (this.exitTime.isBefore(this.entryTime)){
			this.exitTime = this.entryTime;
		}

		return true;
	}

	/**
	 * @param emergencyTime emergency 'time stamp', in tick
	 * @param boolean true if it was set, false otherwise
	 */
	public boolean setEmergencyTime(int emergencyTime){
		if (emergencyTime < 0){
			return false;
		}
		this.emergencyTimeAt = emergencyTime;
		return true;
	}

	/**
	 * @return the zoneStatus
	 */
	public ZoneStatus getZoneStatus() {
		return zoneStatus;
	}

	/**
	 * @param zoneStatus the zoneStatus to set
	 */
	public void setZoneStatus(ZoneStatus zoneStatus) {
		try {
			this.zoneStatus = zoneStatus;
		} catch (InvalidParameterException e){
			e.printStackTrace();
			// incase zoneStatus not set 
		}
	}

	/**
	 * @return the callSign
	 */
	public String getCallSign() {
		return callSign;
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @return the origin
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * @return the scheduled
	 */
	public LocalDateTime getScheduled() {
		return scheduled;
	}

	/**
	 * @return the altitude
	 */
	public double getAltitude() {
		return altitude;
	}

	/**
	 * @return the fuel
	 */
	public double getFuel() {
		return fuel;
	}

	/**
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * @return the emergencyStatus
	 */
	public EmergencyStatus getEmergencyStatus() {
		return emergencyStatus;
	}

	/**
	 * @return the flightType
	 */
	public FlightType getFlightType() {
		return flightType;
	}

	/**
	 * @return the emergency 'time stamp' as tick
	 */
	public int getEmergencyTime(){
		return emergencyTimeAt;
	}

	/**
     * Decrease fuel by constant amount according to time t
     * @param t fuel decrease in t minutes
	 * @param boolean if fuel not updated, false. otherwise true.
     */
    public boolean updateFuel(int t){
        this.fuel = this.fuel - (((this.speed * 1.852) * (t/60) * Utils.fuelBurn) / Utils.fuelDensity);
		return true;
	}
    
    /**
     * @return the number of hours of fuel remaining 
     */
    public double fuelRemainingHrs(){
    	double hrs = ((this.fuel * Utils.fuelDensity) / Utils.fuelBurn) * (1 / this.speed);
		return hrs;
    }
    
    /**
     * TODO: FIX UPDATE METHOD FOR ALTITUDE
     */
    public void updateAltitude(AirCraft parent){
		// get its parents altitude 
		if (parent == null){
			if (this.altitude >= 2000){
				this.altitude -= 1000;
				return;
			} else {
				return; // dont go any lower
			}
		}

		double parentAlt = parent.getAltitude();
		double absDiff = Math.abs((this.altitude - 1000) - parentAlt);
		if (absDiff < 1000){
			this.altitude -= absDiff; 
		} else {
			this.altitude -= 1000;
		}
		return;
    }
    /**
     * Updates emergency status if there is a fuel emergency.
	 * @param t current time in simulation in ticks
     * @return true if there is a fuel emergency, false otherwise.
     */
    public boolean updateFuelEmergency(int t){
		if (fuelRemainingHrs() <= 0.25) {
			this.emergencyStatus = EmergencyStatus.FUEL;
			emergencyTimeAt = t;
    		return true; // there is a fuel emergency
		} else {
			return false; // no fuel emergency
		}
    }
    /**
     * Every iteration of the simulation loop, the arrival plane is updated when it is in
     * the holding queue by updating its fuel, its emergency status and its altitude.
	 * @param t current time in ticks
     * @return true if there is a fuel emergency, false otherwise.
     */
    public boolean updateHoldingFlight(int t, AirCraft parent) {
		if (t < 0 || flightType != FlightType.ARRIVAL){
			return false;
		}

		updateFuel(Utils.timeInc);
		updateAltitude(parent);
		return updateFuelEmergency(t); 
    }
    
	public void addToRunway(String runway){
		this.assignedRunway = runway;
		this.zoneStatus = ZoneStatus.RUNWAY;
	}
    
	/**
	 * @return true if the plane has been assigned to a runway otherwise false
	 */
	public boolean isOnRunway(){
		return this.assignedRunway != "";
	}

	/**
	 * @return get the runway the plane was assigned to
	 */
	public String getRunway(){
		return this.assignedRunway;
	}
}
