package com.cs261.app.model;
import java.util.*;

// collates data that must be outputted to view
public class ModelOutput implements IModelOutput {
    private DepartureModel departureModel;
    private ArrivalModel arrivalModel;
    private RunWayMap runways;
    // aircraft map

    /**
     * Method called in simloop at the end of the iteration
     * 
     * @param currentTime number of ticks
     */
    public void updateOutput(){
        
    }

    public ModelOutput(DepartureModel departureModel, ArrivalModel arrivalModel, RunWayMap runways) {
        this.departureModel = departureModel;
        this.arrivalModel = arrivalModel;
        this.runways = runways;
    }


    // Runway

    /**
     * Returns each runway and which aircraft it is occupied by (if any)
     * 
     * @return a map of runway numbers to the call sign of the occupying aircraft,
     * or an empty string if the runway is free
     */
    @Override
    public Map<String, String> getRunwayOccupancy() {
        Map<String, String> occupancies = new HashMap<>();
        for (RunWay r : runways.getRunways()) {
            String currentPlane;
            // gets current plane, or an empty string if there is none
            if (r.getCurrentPlane() != null) {
                currentPlane = r.getCurrentPlane();
            } else {
                currentPlane = "";
            }
            occupancies.put(r.getRunwayNumber(), currentPlane);
        }
        return occupancies;
    }

    /**
     * Returns each runway and which mode it is in
     * 
     * @return a map of runway numbers to the mode of the runway
     */
    @Override
    public Map<String, String> getRunwayModes() {
        Map<String, String> modes = new HashMap<>();
        for (RunWay r : runways.getRunways()) {
            modes.put(r.getRunwayNumber(), r.getMode().name()); // enum name here as string
        }
        return modes;
    }

    /**
     * Returns each runway and what its closure status is
     * 
     * @return a map of runway numbers to the closure status of the runway
     */
    @Override
    public Map<String, Boolean> getRunwayClosureStatus() {
        Map<String, Boolean> statuses = new HashMap<>();
        for (RunWay r : runways.getRunways()) {
            // returns true if runway is available, false if closed
            statuses.put(r.getRunwayNumber(), r.getStatus() == RunWay.OperationStatus.AVAILABLE);
        }
        return statuses;
    }


    // Aircraft

    /**
     * Returns the aircraft ids of each aircraft in the holding pattern
     * 
     * @return a set of the aircraft ids of each aircraft in the holding pattern
     */
    @Override
    public Set<String> getHoldingAircraftIds() {
        return arrivalModel.getHoldingPatternAircraftIds();
    }

    /**
     * Returns the aircraft ids of each aircraft in the takeoff queue
     * 
     * @return a set of the aircraft ids of each aircraft in the takeoff queue
     */
    @Override
    public Set<String> getTakeOffAircraftIds() {
        return departureModel.getTakeOffQueueAircraftIds();
    }

    @Override
    public Set<String> getExitedAircraftIds() {
        // TODO need to discuss this, where is its
        // add function in aircraftmap
        return Collections.emptySet();
    }

    
    // Metrics
    @Override
    public int getTotalArrivalsGenerated() {
        // discuss if want this to incremented in data gen, or when they enter holding/takeoff queues
        // similar to getTotalAircraftTakenOff() if when enter queues
        return 0;
    }

    @Override
    public int getTotalDeparturesGenerated() {
        // discuss if want this to incremented in data gen, or when they enter holding/takeoff queues
        return 0;
    }

    @Override
    public int getTotalAircraftLanded() {
        // ask afra about updateArrivals in ArrivalModel before changing (will be similar to mine in DepartureModel)
        return 0;
    }

    @Override
    public int getTotalAircraftTakenOff() {
        //return departureModel.getTotalAircraftTakenOff();
        return 0;
    }

    @Override
    public int getMaxHoldingQueueLength() {
        // TODO ask afra how to get max from holding queue
        return 0;
    }

    /**
     * Returns the maximum length of the takeoff queue
     * 
     * @return the max number of planes that have been in the takeoff queue
     */
    @Override
    public int getMaxTakeOffQueueLength() {
        return departureModel.getMaxTakeOffQueueLength();
    }

    @Override
    public double getAverageHoldingWaitTime() {
        // TODO need to figure out how/where times enter/exit the queue is stored
        return 0.0;
    }

    @Override
    public double getAverageTakeOffWaitTime() {
        // TODO need to figure out how/where times enter/exit the queue is stored
        return 0.0;
    }
}

    