package com.cs261.app.model;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.cs261.app.model.AirCraft.FlightType;

// collates data that must be outputted to view
public class ModelOutput implements IModelOutput {
    private DepartureModel departureModel;
    private ArrivalModel arrivalModel;
    private RunWayMap runways;

    // list of planes that have exitted in order of exit time
    ArrayList<AirCraft> exitedArrivals = new ArrayList<>();
    ArrayList<AirCraft> exitedDepartures = new ArrayList<>();
    // hash set of callsigns all planes that exitted the simulation
    HashSet<String> allExits = new HashSet<>();
    ArrayList<AirCraft> planesInTQueue = new ArrayList<>();
    ArrayList<AirCraft> planesInHQueue = new ArrayList<>();

    // aircraft map
    private AirCraftMap aircrafts;
    // KEY STATISTICS 
    private int maxHoldingPlanes = 0;
    private int maxTQueuePlanes = 0;
    private int maxArriveDelay = 0;
    private int maxDepartDelay = 0;
    private int sumArriveDelay = 0;
    private int sumDepartDelay = 0;
    private int sumArriveWait = 0;
    private int sumDepartWait = 0;

    // STATISTICS TO OUTPUT 
    private double avgArriveDelay;
    private double avgDepartDelay;
    private double avgArriveWait;
    private double avgDepartWait;

    // TODO: total delays out of all flights or just the ones that are delayed? assuming the former for now

    public ModelOutput(DepartureModel departureModel, ArrivalModel arrivalModel, RunWayMap runways, HoldingQueue holdingPattern, TakeOffQueue takeOffQueue) {
        this.departureModel = departureModel;
        this.arrivalModel = arrivalModel;
        this.runways = runways;
    }

    /**
     * Method called in simloop at the end of the iteration to update output statistics 
     */
    public void updateOutput(ArrayList<String> exitedPlanes, int holdingSz, int takeOffSz){
		if (exitedPlanes.isEmpty()){
            return;
        }
        // sort the planes. this list will have max 10 planes on it in one iteration of the simulations as it has planes that are just off the runways. 
        exitedPlanes.sort((String a, String b) -> aircrafts.get(a).getExitTime().compareTo(aircrafts.get(b).getExitTime()));
        updateMaxHolding(holdingSz);
        updateMaxTQueue(takeOffSz);
        updateSumsAndMaxs(exitedPlanes);
    }

    /**
     * Method to update the stat for max number of planes in the holding pattern at a time during the simulation
     */
    private void updateMaxHolding(int sz){
        if (sz > maxHoldingPlanes) maxHoldingPlanes = sz;
    }

    /**
     * Method to update the stat for max number of planes in the take off queue at a time during the simulation
     */
    private void updateMaxTQueue(int sz){
        if (sz > maxTQueuePlanes) maxTQueuePlanes = sz;
    }

    /**
     * Method to update: maximum arrival delay, maximum departured delay, sum of arrival delays, sum of departure delays, sum of wait time for arrival and sum of wait time
     * for departure.
     * Avoid processing same array multiple times 
     * @param exits planes that have exitted the simulation in the current iteration
     */
    private void updateSumsAndMaxs(ArrayList<String> exits){
        for (String e : exits){
            AirCraft plane = aircrafts.get(e);
            // its only delayed if it leaves the sim AFTER its scheduled time
            // now calculate wait times
            // we're gonna assume that the model will set queue entry = queue exit if it does not wait in any queues,,, but need to test this 


            /**
             * DELAY is the difference between the time it was scheduled to leave/land and the time it does. You want to check delays only for planes that leave after they were scheduled to.
             * WAIT TIME is the time it waits in the queue, so exit time and entry time difference. 
             * this may include being delayed or being early, as long as it was in the queue
             * you want to check the wait time for all planes.
             */
            int delay = 0;
            if (plane.getExitTime().isAfter(plane.getScheduled())){ // only check this if it was delayed
                delay = (int) Duration.between(plane.getScheduled(), plane.getExitTime()).toMinutes();
            } 
            int waitTime = (int) Duration.between(plane.getEntryTime(), plane.getExitTime()).toMinutes();
            if (plane.getFlightType() == FlightType.ARRIVAL){
                maxArriveDelay = ((delay > maxArriveDelay) ? delay : maxArriveDelay);
                sumArriveDelay += delay;
                sumArriveWait += waitTime;

                if (waitTime > 0){// this means it did go into a queue
                    planesInHQueue.add(plane);
                }

                exitedArrivals.add(plane);
                allExits.add(e);
            } else {
                maxDepartDelay = ((delay > maxDepartDelay) ? delay : maxDepartDelay);
                sumDepartDelay += delay;
                sumDepartWait += waitTime;

                if (waitTime > 0){// this means it did go into a queue
                    planesInTQueue.add(plane);
                }

                exitedDepartures.add(plane);
                allExits.add(e);
            }
        }
    }


    // Metrics

    /**
     * @return the total number of airplanes that were scheduled for arrival
     */
    public int getTotalArrivalsGenerated() {
        return arrivalModel.getTotalArrivals();
    }

    /**
     * @return total number of planes that were scheduled for departure
     */
    public int getTotalDeparturesGenerated() {
        return departureModel.getTotalDepartures();
    }

    /**
     * @return total number of airplanes that actually landed
     */
    public int getTotalAircraftLanded() {
        return arrivalModel.getLanded();
    }

    /**
     * @return total number of airplanes that actually took off
     */
    public int getTotalAircraftTakenOff() {
        return departureModel.getTakenOff();
    }

    /**
     * @return the max number of planes that have been in the holding queue
     */
    public int getMaxHoldingQueueLength() {
        return maxHoldingPlanes;
    }

    /**
     * @return the max number of planes that have been in the takeoff queue
     */
    public int getMaxTakeOffQueueLength() {
        return maxTQueuePlanes;
    }


    public int getMaxArriveDelay(){
        return maxArriveDelay;
    }

    public int getMaxDepartDelay(){
        return maxDepartDelay;
    }

    /**
     * @return the average delay of planes that landed at the airport
     */
    public double getAvgArriveDelay(){
        avgArriveDelay = sumArriveDelay / arrivalModel.getLanded();
        return avgArriveDelay;
    }

    /**
     * @return the average delay of planes that took off at the airport
     */
    public double getAvgDepartDelay(){
        avgDepartDelay = sumDepartDelay / departureModel.getTakenOff();
        return avgDepartDelay;
    }

    /**
     * @return the average wait time of planes that landed at the airport 
     */
    public double getAverageHoldingWaitTime() {
        avgArriveWait = sumArriveWait / arrivalModel.getLanded();
        return avgArriveWait;
    }

    /**
     * @return the average wait time of planes that took off at the airport
     */
    public double getAverageTakeOffWaitTime(){
        avgDepartWait = sumDepartWait / departureModel.getTakenOff();
        return avgDepartWait;
    }

    // lists and maps 
    /**
     * Returns each runway and which aircraft it is occupied by currently (if any)
     * @return a map of runway numbers to the call sign of the occupying aircraft,
     * or an empty string if the runway is free
     */
    @Override
    public Map<String, String> getRunwayOccupancy() {
        Map<String, String> occupancies = new HashMap<>();
        for (RunWay r : runways.getRunways()) {
            String currentPlane = "";
            // gets current plane, or an empty string if there is none
            if (r.hasPlane()) {
                currentPlane = r.getCurrentPlane();
            } 
            occupancies.put(r.getRunwayNumber(), currentPlane);
        }
        return occupancies;
    }

    /**
     * Returns each runway and which mode it is in
     * @return a map of runway numbers to the mode of the runway
     */
    @Override
    public Map<String, String> getRunwayModes() {
        Map<String, String> modes = new HashMap<>();
        for (RunWay r : runways.getRunways()) {
            modes.put(r.getRunwayNumber(), r.getMode().toString()); // enum name here as string
        }
        return modes;
    }

    /**
     * Returns each runway and what its closure status is
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
        return allExits;   
    }

    // METHODS TO GET DATA FOR EACH TABLE...

    /**
     * Returns table of runway configurations. 
     * @return table of strings
     * column1 = runway number, column2 = runway opertional mode (landing/takeoff), column3 = runway operational status, column4 = true if
     * runway becomes unavailable during runtime, false otherwise, column5 = status it changes to, column6 = start time of status change, column6 = end time of status change
     * note that the last three columns will be "N/A" if the status of the runway does not change during runtime
     */
    public String[][] runwayTable(){
        String[][] table = new String[runways.size()][7];
        RunWay[] runwayList = runways.getRunways();
        for (int i = 0; i < runwayList.length; i++){
            table[i][0] = runwayList[i].getRunwayNumber();
            table[i][1] = runwayList[i].getMode().toString();
            table[i][2] = runwayList[i].getStatus().toString();
            table[i][3] = Boolean.toString(runwayList[i].hasStatusChange());
            if (runwayList[i].hasStatusChange()){
                table[i][4] = runwayList[i].getChangedStatus().toString();
                table[i][5] = runwayList[i].getStatusChangeStartTime(i).toString();
                table[i][6] = runwayList[i].getStatusChangeEndTime(i).toString();
            } else {
                table[i][4] = "N/A";
                table[i][5] = "N/A";
                table[i][6] = "N/A";
            }            
        }
        return table;
    }

    /**
     * @return all airplanes that have been in the holding queue
     */
    public String[][] holdingQueueTable(){
        return getQueueTable(planesInHQueue);
    }

    /**
     * @return all airplanes that have been in the take off queue
     */
    public String[][] takeOffQueueTable(){
        return getQueueTable(planesInTQueue);
    }

    /**
     * Returns the columns and rows necessary for a queue table in the simulation output page.
     * The list should be in the natural order of exit time of the aircrafts (from the simulation)
     * @param list list to get aircraft details from
     * @return table of strings
     * column1 = call sign, column2 = scheduled departure/arrival time, column3 = actual departure/arrival time, column4 = delay, column5 = wait time
     */
    public String[][] getQueueTable(ArrayList<AirCraft> list){
        String[][] table = new String[list.size()][5];
        for (int i = 0; i < list.size(); i++){
            AirCraft a = list.get(i);
            int delay = 0;
            if (a.getExitTime().isAfter(a.getScheduled())){ // only check this if it was delayed
                delay = (int) Duration.between(a.getScheduled(), a.getExitTime()).toMinutes();
            } 

            table[i][0] = a.getCallSign();
            table[i][1] = a.getScheduled().toString();
            table[i][2] = a.getEntryTime().toString(); 
            table[i][3] = Integer.toString(delay);
            table[i][4] = Integer.toString((int) Duration.between(a.getEntryTime(), a.getExitTime()).toMinutes());
        }
        return table;
    }

    /**
     * @return all aircrafts that landed in the simulation in order of exit time
     */
    public String[][] arrivalsTable(){
        return getSimExitTable(exitedArrivals);
    }

    /**
     * @return all aircrafts that took off in the simulation in order of exit time
     */
    public String[][] departuresTable(){
        return getSimExitTable(exitedDepartures);
    }

    public String[][] getSimExitTable(ArrayList<AirCraft> list){
        String[][] table = new String[list.size()][3];
        for (int i = 0; i < list.size(); i++){
            AirCraft a = list.get(i);
            table[i][0] = a.getRunway();
            table[i][1] = a.getCallSign();
            table[i][2] = a.getExitTime().toString();
        }
        return table;
    }

    /**
     * @return difference in scheduled time and actual time of arrivals
     */
    public LocalDateTime[][] arrivalDifferenceGraph(){
        LocalDateTime[][] graph = new LocalDateTime[exitedArrivals.size()][2];
        for (int i = 0; i < exitedArrivals.size(); i++){
            graph[i][0] = exitedArrivals.get(i).getScheduled();
            graph[i][1] = exitedArrivals.get(i).getExitTime();
        }
        return graph;
    }

    /**
     * @return difference in scheduled time and actual time of departures
     */
    public LocalDateTime[][] departDifferenceGraph(){
        LocalDateTime[][] graph = new LocalDateTime[exitedDepartures.size()][2];
        for (int i = 0; i < exitedDepartures.size(); i++){
            graph[i][0] = exitedDepartures.get(i).getScheduled();
            graph[i][1] = exitedDepartures.get(i).getExitTime();
        }
        return graph;
    }

}
