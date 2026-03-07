package com.cs261.app.UI;
public class QueueRow {
    //column1 = call sign, column2 = scheduled departure/arrival time, column3 = actual departure/arrival time, column4 = delay, column5 = wait time
    private String callsign;
    private String scheduled;
    private String actual;
    private String delay;
    private String waitTime;


    public QueueRow(String callsign, String scheduled, String actual, String delay,String waitTime)
    {
        this.callsign = callsign;
        this.delay = delay;
        this.scheduled = scheduled;
        this.actual = actual;
        
        this.waitTime = waitTime;
    }

    public String getCallsign() { return callsign; }
    public String getDelay() { return delay; }
    public String getActual() { return actual; }
    public String getEntryTime() { return scheduled; }
    public String getWaitTime() { return waitTime; }

}