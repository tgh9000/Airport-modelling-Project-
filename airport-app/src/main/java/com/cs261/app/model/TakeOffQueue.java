package com.cs261.app.model;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * Represents the airport departures take off queue. 
 */
public class TakeOffQueue {

    // the takeoff queue, stores aircraft waiting for takeoff
    private LinkedList<AirCraft> queue;

    // constructs a new empty TakeOffQueue
    public TakeOffQueue() {
        this.queue = new LinkedList<>();
    }

    /**
     * Adds specified aircraft to the back of the takeoff queue.
     *
     * @param aircraft the AirCraft object to be added to the queue
     */
    public void enqueue(AirCraft aircraft) {
        queue.addLast(aircraft);
    }

    /**
     * Removes and returns the aircraft at the front of the queue. If the queue is 
     * empty, it returns null.
     *
     * @return the aircraft at the front of the queue, or null if the queue is empty
     */
    public AirCraft dequeue() {
        if (queue.isEmpty()) {
            return null;
        }
        return queue.removeFirst();
    }

    /**
     * Removes the specified aircraft from the queue if it is in the queue.
     *
     * @param aircraft the aircraft removed from the queue
     * @return true if the aircraft was removed, false if it wasn't in the queue
     */
    public boolean remove(AirCraft aircraft) {
        return queue.remove(aircraft); 
    }

    /**
     * Returns the aircraft at the specified index in the queue.
     *
     * @param index the position of the aircraft to be returned
     * @return the aircraft at the index, otherwise null if the index is out of bounds
     */
    public AirCraft getAircraftAt(int index) throws InvalidParameterException {
        if (index < 0 || index >= queue.size()) {
            throw new InvalidParameterException();
        }
        return queue.get(index);
    }

    /**
     * Checks if the queue is empty.
     *
     * @return true if the queue is empty, false if not
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Returns the size of the takeoff queue.
     *
     * @return the size of the takeoff queue
     */    public int size() {
        return queue.size();
    }

    /**
     * Returns a set of the aircraft ids (callsigns) currently in the takeoff queue.
     *
     * @return set of aircraft ids in the queue
     */
    public Set<String> getAircraftIds() {
        Set<String> ids = new HashSet<>();
        for (AirCraft plane : queue) {
            ids.add(plane.getCallSign());
        }
        return ids;
    }

    /**
     * Returns a list of aircraft in the takeoff queue that are in danger of being cancelled
     * due to their wait time.
     * @param currentTime the current time in simulation ticks
     * @return an arraylist of aircraft in danger of cancellation
     */
    public ArrayList<AirCraft> inCancellationDanger(int currentTime) throws InvalidParameterException {
        if (currentTime < 0){
            throw new InvalidParameterException("Current time parameter negative");
        }

        ArrayList<AirCraft> dangerPlanes = new ArrayList<>();
        for (AirCraft plane : queue) {
            int entry = Utils.convertDateToTicks(plane.getEntryTime());
            if ((currentTime - entry) >= Utils.dangerOfCancel) {
                dangerPlanes.add(plane);
            }
        }
        return dangerPlanes;
    }
}