package com.cs261.app.UI;
public class ArrivalRow {

    private String callsign;
    private String Runway;
    private String actual;
    private String scheduled;
    private String exitTime;


    public ArrivalRow(String callsign, String Runway, String actual, String scheduled,String exitTime)
    {
        this.callsign = callsign;
        this.Runway = Runway;
        this.scheduled = scheduled;
        this.actual = actual;
        
        this.exitTime = exitTime;
    }

    public String getCallsign() { return callsign; }
    public String getRunway() { return Runway; }
    public String getActual() { return actual; }
    public String getEntryTime() { return scheduled; }
    public String getExitTime() { return exitTime; }

}
