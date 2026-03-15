package com.cs261.app.UI;

public class AirCraftDetail 
{
    /*
    the row in the pop up menu for the aircraft table
    */
    private final String callsign;
    private final String operator;
    private final String origin;
    private final String destination;
    private final String flightType;
    private final String emergencyStatus;
    private final String emergencyStart; 

    public AirCraftDetail(String callsign, String operator, String origin, String destination, String flightType, String emergencyStatus, String emergencyStart)
    {
        this.callsign = callsign;
        this.operator = operator;
        this.origin = origin;
        this.destination = destination;
        this.flightType = flightType;
        this.emergencyStatus = emergencyStatus;
        this.emergencyStart = emergencyStart;
    }

    public String getCallsign() 
    {
        return callsign;
    }

    public String getOperator() 
    {
        return operator;
    }

    public String getOrigin() 
    {
        return origin;
    }

    public String getDestination() 
    {
        return destination;
    }

    public String getFlightType()
    {
        return flightType;
    }

    public String getEmergencyStatus() 
    {
        return emergencyStatus;
    }

    public String getEmergencyStart() 
    {
        return emergencyStart;
    }
}
