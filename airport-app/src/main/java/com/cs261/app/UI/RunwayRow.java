package com.cs261.app.UI;

public class RunwayRow 
{
    private String callsign;
    private String type;
    private String actual;
    private String runway;
    private String scheduled;
    private String exitTime;

    /**
     * Constructor for the Runway class
     */
    public RunwayRow(String callsign, String type, String actual, String scheduled,String exitTime, String runway)
    {
        this.callsign = callsign;
        this.type = type;
        this.scheduled = scheduled;
        this.actual = actual;
        this.runway = runway;
        this.exitTime = exitTime;
    }

    public String getCallsign() { return callsign; }
    public String getType() { return type; }
    public String getActual() { return actual; }
    public String getEntryTime() { return scheduled; }
    public String getExitTime() { return exitTime; }
     public String getRunway() { return runway; }

}
