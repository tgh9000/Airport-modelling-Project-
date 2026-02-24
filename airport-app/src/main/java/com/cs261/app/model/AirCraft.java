package com.cs261.app.model;

import java.time.LocalDateTime;

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
    public static enum EmergencyStatus {
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
    private LocalDateTime exitTime; // Time it leaves the holding queue or take off queue 
    private double altitude; // metres
    private double fuel; // fuel remaining in litres
    private double speed; // ground speed in knots
    private EmergencyStatus emergencyStatus;
    private ZoneStatus zoneStatus; 
    private FlightType flightType; 
    private String assignedRunway;
	private int emergencyTimeAt; // the number of ticks (runtime) at which it went into emergency


    public AirCraft(FlightType type, String callsign, String operator, String origin, String dest, LocalDateTime scheduled, LocalDateTime entryTime, float speed, float fuel, float alt, EmergencyStatus status){
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
		this.assignedRunway = "";

        this.exitTime = null;
        this.zoneStatus =  ZoneStatus.QUEUE;
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

	/**
	 * @param exitTime the exitTime to set
	 */
	public void setExitTime(LocalDateTime exitTime) {
		this.exitTime = exitTime;
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
		this.zoneStatus = zoneStatus;
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
	 * @return the emergency 'time stamp'
	 */
	public int getEmergencyTime(){
		return emergencyTimeAt;
	}

	/**
     * Decrease fuel by constant amount according to time t
     * @param t fuel decrease in t minutes
     */
    public void updateFuel(int t){
        this.fuel = this.fuel - (((this.speed * 1.852) * (t/60) * Utils.fuelBurn) / 0.8029);
    }
    
    /**
     * @return the number of hours of fuel remaining 
     */
    public double fuelRemainingHrs(){
    	double hrs = ((this.fuel * 0.8029) / Utils.fuelBurn) * (1 / this.speed);
		return hrs;
    }
    
    /**
     * TODO: FIX UPDATE METHOD FOR ALTITUDE
     */
    public void updateAltitude(){
		this.altitude -= 1000;
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
    public boolean updateHoldingFlight(int t) {
		updateFuel(Utils.timeInc);
		updateAltitude();
		return updateFuelEmergency(t); 
    }
    
    // TODO: updateTakeOffFlight()

	public void addToRunway(String runway){
		this.assignedRunway = runway;
		this.zoneStatus = ZoneStatus.RUNWAY;
	}
    
	/**
	 * @return true if the plane has been assigned to a runway otherwise false
	 */
	public boolean isOnRunway(){
		if (this.assignedRunway != ""){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return get the runway the plane was assigned to
	 */
	public String getRunway(){
		return this.assignedRunway;
	}
}
