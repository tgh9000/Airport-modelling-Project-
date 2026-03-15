package com.cs261.app.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.security.InvalidParameterException;
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

    @Test
    void getAircraftIdsReturnsAllIds() {
        TakeOffQueue queue = new TakeOffQueue();

        queue.enqueue(createDeparture("A1"));
        queue.enqueue(createDeparture("A2"));

        assertTrue(queue.getAircraftIds().contains("A1"));
        assertTrue(queue.getAircraftIds().contains("A2"));
        assertEquals(2, queue.getAircraftIds().size());
    }

    @Test
    void isEmptyShouldBeCorrect() {
        TakeOffQueue queue = new TakeOffQueue();

        assertTrue(queue.isEmpty());
        queue.enqueue(createDeparture("A1"));
        assertFalse(queue.isEmpty());
    }    

    @Test
    void getAircraftAtReturnsCorrectly() {
        TakeOffQueue queue = new TakeOffQueue();

        queue.enqueue(createDeparture("A1"));
        queue.enqueue(createDeparture("A2"));
        queue.enqueue(createDeparture("A3"));

        assertEquals("A2", queue.getAircraftAt(1).getCallSign());
        assertEquals("A3", queue.getAircraftAt(2).getCallSign());
    }

    @Test
    void getAircraftAtThrowsExceptionWhenIndexNegative() {
        TakeOffQueue queue = new TakeOffQueue();

        queue.enqueue(createDeparture("A1"));

        assertThrows(InvalidParameterException.class, () -> {
            queue.getAircraftAt(-1);
        });
    }

    @Test
    void getAircraftAtThrowsExceptionWhenIndexTooLarge() {
        TakeOffQueue queue = new TakeOffQueue();

        queue.enqueue(createDeparture("A1"));
        queue.enqueue(createDeparture("A2"));

        assertThrows(InvalidParameterException.class, () -> {
            queue.getAircraftAt(2); 
        });
    }

    @Test
    void inCancellationDangerThrowsExceptionWhenTimeIsNegative() {
        TakeOffQueue queue = new TakeOffQueue();

        queue.enqueue(createDeparture("A1"));
        queue.enqueue(createDeparture("A2"));

        assertThrows(InvalidParameterException.class, () -> {
            queue.inCancellationDanger(-2); 
        });
    }    
}
