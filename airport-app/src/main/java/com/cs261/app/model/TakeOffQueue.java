package com.cs261.app.model;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class TakeOffQueue {
    /** 
     * is there anything else we want the queue to do? TODO max takeoff queue length
     * test, calculate metrics here?
     * TODO return list of plane ids in order?
     * TODO test getAircraftIds, maxQueueLength
     */

    private LinkedList<AirCraft> queue;
    private int maxQueueLength = 0;

    public TakeOffQueue() {
        this.queue = new LinkedList<>();
    }

    // adds aircraft to the back of the queue
    public void enqueue(AirCraft aircraft) {
        queue.addLast(aircraft);

    // update maximum length if current size exceeds it
    if (queue.size() > maxQueueLength) {
        maxQueueLength = queue.size();
    }

    }

    // removes aircraft from front of queue
    public AirCraft dequeue() {
        if (queue.isEmpty()) {
            return null;  // TODO might want to change to throw exception
        }
        return queue.removeFirst();
    }

    // returns the aircraft at a specific position in the queue
    public AirCraft getAircraftAt(int index) {
        if (index < 0 || index >= queue.size()) {
            return null; // TODO might want to change to throw exception
        }
        return queue.get(index);
    }

    // checks if queue is empty
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    // returns the size of the queue
    public int size() {
        return queue.size();
    }

    // returns a set of the aircraft ids in the takeoff queue
    public Set<String> getAircraftIds() {
        Set<String> ids = new HashSet<>();
        for (AirCraft plane : queue) {
            ids.add(plane.getCallSign());
        }
        return ids;
    }

    // returns the maximum takeoff queue length
    public int getMaxQueueLength() {
        return maxQueueLength;
    }
//}

public static void main(String[] args) {

    TakeOffQueue myTakeoff = new TakeOffQueue();
    AirCraft plane = new AirCraft(AirCraft.FlightType.ARRIVAL, "PO111", "Delta", "JFK", "LAX", LocalDateTime.of(2026, 2, 19, 14, 30), LocalDateTime.of(2026, 2, 19, 14, 35), 450, 8000, 30000, AirCraft.EmergencyStatus.NONE);
    AirCraft plane2 = new AirCraft(AirCraft.FlightType.ARRIVAL, "PT222", "Pelta", "PFK", "PAX", LocalDateTime.of(2026, 1, 9, 17, 30), LocalDateTime.of(2026, 1, 9, 17, 35), 400, 8500, 35000, AirCraft.EmergencyStatus.NONE);
    AirCraft plane3 = new AirCraft(AirCraft.FlightType.ARRIVAL, "PT333", "Pelta", "PFK", "PAX", LocalDateTime.of(2026, 1, 9, 17, 30), LocalDateTime.of(2026, 1, 9, 17, 35), 400, 8500, 35000, AirCraft.EmergencyStatus.NONE);
    AirCraft plane4 = new AirCraft(AirCraft.FlightType.ARRIVAL, "PF444", "Pelta", "PFK", "PAX", LocalDateTime.of(2026, 1, 9, 17, 30), LocalDateTime.of(2026, 1, 9, 17, 35), 400, 8500, 35000, AirCraft.EmergencyStatus.NONE);

    myTakeoff.enqueue(plane2);
    myTakeoff.enqueue(plane);
    myTakeoff.enqueue(plane3);
    myTakeoff.enqueue(plane4);
    System.out.println(myTakeoff.size());
    System.out.println(myTakeoff.getAircraftAt(0).getCallSign());
    myTakeoff.dequeue();
    System.out.println(myTakeoff.getAircraftAt(0).getCallSign());
    System.out.println(myTakeoff.isEmpty());
}

}