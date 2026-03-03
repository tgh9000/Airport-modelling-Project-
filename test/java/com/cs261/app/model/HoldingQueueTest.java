package com.cs261.app.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class HoldingQueueTest {

    private AirCraft createArrival(String id, AirCraft.EmergencyStatus status, int emergencyTime) {
        return new AirCraft(
                AirCraft.FlightType.ARRIVAL,
                id,
                "Airline",
                "AAA",
                "BBB",
                LocalDateTime.now(),
                LocalDateTime.now(),
                400f,
                8000f,
                30000f,
                status,
                emergencyTime
        );
    }

    @Test
    void emergencyAircraftShouldBePrioritised() {
        HoldingQueue queue = new HoldingQueue(5);

        queue.enqueue(1, createArrival("N1", AirCraft.EmergencyStatus.NONE, -1));
        queue.enqueue(2, createArrival("E1", AirCraft.EmergencyStatus.FUEL, Utils.convertDateToTicks(LocalDateTime.now())));

        assertEquals("E1", queue.top().getCallSign());
    }

    @Test
    void earlierSerialShouldWinIfSameEmergency() {
        HoldingQueue queue = new HoldingQueue(5);

        queue.enqueue(1, createArrival("A1", AirCraft.EmergencyStatus.NONE, -1));
        queue.enqueue(2, createArrival("A2", AirCraft.EmergencyStatus.NONE, -1));

        assertEquals("A1", queue.top().getCallSign());
    }

    @Test
    void isEmptyShouldWorkCorrectly() {
        HoldingQueue queue = new HoldingQueue(5);
        assertTrue(queue.isEmpty());

        queue.enqueue(1, createArrival("A1", AirCraft.EmergencyStatus.NONE, -1));
        assertFalse(queue.isEmpty());
    }

    @Test
    void resizeShouldNotLoseElements() {
        HoldingQueue queue = new HoldingQueue(1);

        queue.enqueue(1, createArrival("A1", AirCraft.EmergencyStatus.NONE, -1));
        queue.enqueue(2, createArrival("A2", AirCraft.EmergencyStatus.NONE, -1));

        assertEquals(2, queue.size());
    }

    @Test
    void topShouldNotRemoveElement() {
        HoldingQueue queue = new HoldingQueue(5);

        queue.enqueue(1, createArrival("A1", AirCraft.EmergencyStatus.NONE, -1));

        assertEquals("A1", queue.top().getCallSign());
        assertEquals(1, queue.size());
    }
}

