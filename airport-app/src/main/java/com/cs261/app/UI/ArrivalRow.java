package com.cs261.app.UI;
public class ArrivalRow {

    private String callsign;
    private String Runway;
    private String actual;


    public ArrivalRow(String callsign, String Runway, String actual ) 
    {
        this.callsign = callsign;
        this.Runway = Runway;
        this.actual = actual;

    }

    public String getCallsign() { return callsign; }
    public String getRunway() { return Runway; }
    public String getActual() { return actual; }

}
