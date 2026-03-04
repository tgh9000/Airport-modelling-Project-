package com.cs261.app.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class HoldingQueueTest {

    private AirCraft createArrival(String id, AirCraft.EmergencyStatus status) {
        return new AirCraft(
                AirCraft.FlightType.ARRIVAL,
                id,
                "Airline",
                "AAA",
                "BBB",
                LocalDateTime.now(),
                LocalDateTime.now(),
                400,
                8000,
                30000,
                status
        );
    }

    @Test
    void emergencyAircraftShouldBePrioritised() {
        HoldingQueue queue = new HoldingQueue(5);

        queue.enqueue(1, createArrival("N1", AirCraft.EmergencyStatus.NONE));
        queue.enqueue(2, createArrival("E1", AirCraft.EmergencyStatus.FUEL));

        assertEquals("E1", queue.top().getCallSign());
    }

    @Test
    void earlierSerialShouldWinIfSameEmergency() {
        HoldingQueue queue = new HoldingQueue(5);

        queue.enqueue(1, createArrival("A1", AirCraft.EmergencyStatus.NONE));
        queue.enqueue(2, createArrival("A2", AirCraft.EmergencyStatus.NONE));

        assertEquals("A1", queue.top().getCallSign());
    }

    @Test
    void earlierEmergencyShouldWin() {
        HoldingQueue queue = new HoldingQueue(5);

        queue.enqueue(1, createArrival("E1", AirCraft.EmergencyStatus.HEALTH));
        queue.enqueue(2, createArrival("E2", AirCraft.EmergencyStatus.FUEL));

        assertEquals("E1", queue.top().getCallSign());
    }

    @Test
    void multipleEmergenciesAndNonEmergencies() {
        HoldingQueue queue = new HoldingQueue(5);

        queue.enqueue(1, createArrival("A1", AirCraft.EmergencyStatus.NONE));
        queue.enqueue(2, createArrival("E1", AirCraft.EmergencyStatus.MECH));
        queue.enqueue(3, createArrival("A2", AirCraft.EmergencyStatus.NONE));
        queue.enqueue(4, createArrival("E2", AirCraft.EmergencyStatus.FUEL));

        assertEquals("E1", queue.top().getCallSign());
        queue.dequeue();
        assertEquals("E2", queue.top().getCallSign());
        assertEquals("A1", queue.top().getCallSign());

    }

    @Test
    void isEmptyShouldWorkCorrectly() {
        HoldingQueue queue = new HoldingQueue(5);
        assertTrue(queue.isEmpty());

        queue.enqueue(1, createArrival("A1", AirCraft.EmergencyStatus.NONE));
        assertFalse(queue.isEmpty());
    }

    @Test
    void resizeShouldNotLoseElements() {
        HoldingQueue queue = new HoldingQueue(1);

        queue.enqueue(1, createArrival("A1", AirCraft.EmergencyStatus.NONE));
        queue.enqueue(2, createArrival("A2", AirCraft.EmergencyStatus.NONE));

        assertEquals(2, queue.size());
    }

    @Test
    void topShouldNotRemoveElement() {
        HoldingQueue queue = new HoldingQueue(5);

        queue.enqueue(1, createArrival("A1", AirCraft.EmergencyStatus.NONE));

        assertEquals("A1", queue.top().getCallSign());
        assertEquals(1, queue.size());
    }
}

