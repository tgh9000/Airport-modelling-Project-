package com.cs261.app.UI;
public class RunwayDetail
{
    private final String id;
    private final String operationalMode;
    private final String runwayStatus;
    private final String hasInterruption;
    private final String statusChangeTo;
    private final String interruptionStart;
    private final String interruptionEnd; 

    public RunwayDetail(String id, String operationalMode, String runwayStatus, String hasInterruption,String statusChangeTo, String interruptionStart , String  interruptionEnd) 
    {
        this.id = id;
        this.operationalMode = operationalMode;
        this.runwayStatus = runwayStatus;
        this.hasInterruption = hasInterruption;
        this.interruptionStart = interruptionStart;
        this.interruptionEnd = interruptionEnd;
        this.statusChangeTo = statusChangeTo;
    }

    String getId(){return id;}
    String getOperationalMode() {return operationalMode;}
    String getRunwayStatus() {return runwayStatus;}
    String getHasInterruption(){return hasInterruption;}
    String getStatusChange(){return statusChangeTo;}
    String getInterruptionStart(){return interruptionStart;}
    String getInterruptionEnd(){return interruptionEnd;}

}