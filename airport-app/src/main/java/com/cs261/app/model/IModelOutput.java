package com.cs261.app.model;

import java.util.Map;
import java.util.Set;

public interface IModelOutput {
    /**
    doneTODO: add getMaxArrivals etc stuff  
    */ 
    //runway
    Map<String, String> getRunwayOccupancy();
    Map<String, String> getRunwayModes();
    Map<String, Boolean> getRunwayClosureStatus();
    //aircraft
    Set<String> getHoldingAircraftIds();
    Set<String> getTakeOffAircraftIds();
    Set<String> getExitedAircraftIds();

    //metrics
    int getTotalArrivalsGenerated();
    int getTotalDeparturesGenerated();
    int getTotalAircraftLanded();
    int getTotalAircraftTakenOff();
    int getMaxHoldingQueueLength();
    int getMaxTakeOffQueueLength();
    double getAverageHoldingWaitTime();
    double getAverageTakeOffWaitTime();
    
}
