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
                400,
                8000,
                30000,
                AirCraft.EmergencyStatus.NONE
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

    @Test
    void maxQueueLengthShouldTrackPeak() {
        TakeOffQueue queue = new TakeOffQueue();

        queue.enqueue(createDeparture("A1"));
        queue.enqueue(createDeparture("A2"));
        queue.enqueue(createDeparture("A3"));

        queue.dequeue();

        assertEquals(3, queue.getMaxQueueLength());
    }

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