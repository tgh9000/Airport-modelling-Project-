package com.cs261.app.UI;
public class ArrivalRow {

    private String callsign;
    private String scheduled;
    private String actual;
    private String delay;
    private String wait;

    public ArrivalRow(String callsign, String scheduled, String actual, String delay, String wait) {
        this.callsign = callsign;
        this.scheduled = scheduled;
        this.actual = actual;
        this.delay = delay;
        this.wait = wait;
    }

    public String getCallsign() { return callsign; }
    public String getScheduled() { return scheduled; }
    public String getActual() { return actual; }
    public String getDelay() { return delay; }
    public String getWait() { return wait; }
}
