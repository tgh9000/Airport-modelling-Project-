package com.cs261.app.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class TakeOffQueueTest {

    private AirCraft createDeparture(String id) {
        return new AirCraft(
                AirCraft.FlightType.DEPARTURE,
                id,
                "Airline",
                "AAA",
                "BBB",
                LocalDateTime.now(),
                LocalDateTime.now(),
                400f,
                8000f,
                30000f,
                AirCraft.EmergencyStatus.NONE,
                -1
        );
    }

    @Test
    void fifoOrderingShouldBeMaintained() {
        TakeOffQueue queue = new TakeOffQueue();

        queue.enqueue(createDeparture("A1"));
        queue.enqueue(createDeparture("A2"));
        queue.enqueue(createDeparture("A3"));

        assertEquals("A1", queue.dequeue().getCallSign());
        assertEquals("A2", queue.dequeue().getCallSign());
        assertEquals("A3", queue.dequeue().getCallSign());
    }

    @Test
    void sizeShouldIncreaseAndDecreaseCorrectly() {
        TakeOffQueue queue = new TakeOffQueue();

        queue.enqueue(createDeparture("A1"));
        queue.enqueue(createDeparture("A2"));

        assertEquals(2, queue.size());

        queue.dequeue();
        assertEquals(1, queue.size());
    }

    @Test
    void dequeueOnEmptyReturnsNull() {
        TakeOffQueue queue = new TakeOffQueue();
        assertNull(queue.dequeue());
    }

    // @Test
    // void maxQueueLengthShouldTrackPeak() {
    //     TakeOffQueue queue = new TakeOffQueue();

    //     queue.enqueue(createDeparture("A1"));
    //     queue.enqueue(createDeparture("A2"));
    //     queue.enqueue(createDeparture("A3"));

    //     queue.dequeue();

    //     assertEquals(3, queue.getMaxQueueLength());
    // }

    @Test
    void getAircraftIdsReturnsAllIds() {
        TakeOffQueue queue = new TakeOffQueue();

        queue.enqueue(createDeparture("A1"));
        queue.enqueue(createDeparture("A2"));

        assertTrue(queue.getAircraftIds().contains("A1"));
        assertTrue(queue.getAircraftIds().contains("A2"));
        assertEquals(2, queue.getAircraftIds().size());
    }
}

// public static void main(String[] args) {

//     TakeOffQueue myTakeoff = new TakeOffQueue();
//     AirCraft plane = new AirCraft(AirCraft.FlightType.ARRIVAL, "PO111", "Delta", "JFK", "LAX", LocalDateTime.of(2026, 2, 19, 14, 30), LocalDateTime.of(2026, 2, 19, 14, 35), 450, 8000, 30000, AirCraft.EmergencyStatus.NONE, -1);
//     AirCraft plane2 = new AirCraft(AirCraft.FlightType.ARRIVAL, "PT222", "Pelta", "PFK", "PAX", LocalDateTime.of(2026, 1, 9, 17, 30), LocalDateTime.of(2026, 1, 9, 17, 35), 400, 8500, 35000, AirCraft.EmergencyStatus.NONE, -1);
//     myTakeoff.enqueue(plane);

//     System.out.println(myTakeoff.remove(plane2));

// }