package com.cs261.app.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.ArrayList;

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
    void earlierEmergencyShouldWin() {
        HoldingQueue queue = new HoldingQueue(5);

        queue.enqueue(1, createArrival("E1", AirCraft.EmergencyStatus.HEALTH, Utils.convertDateToTicks(LocalDateTime.now())));
        queue.enqueue(2, createArrival("E2", AirCraft.EmergencyStatus.FUEL, Utils.convertDateToTicks(LocalDateTime.now())));

        assertEquals("E1", queue.top().getCallSign());
    }

    @Test
    void multipleEmergenciesAndNonEmergencies() {
        HoldingQueue queue = new HoldingQueue(5);

        queue.enqueue(1, createArrival("A1", AirCraft.EmergencyStatus.NONE, Utils.convertDateToTicks(LocalDateTime.now())));
        queue.enqueue(2, createArrival("E1", AirCraft.EmergencyStatus.MECH, Utils.convertDateToTicks(LocalDateTime.now())));
        queue.enqueue(3, createArrival("A2", AirCraft.EmergencyStatus.NONE, Utils.convertDateToTicks(LocalDateTime.now())));
        queue.enqueue(4, createArrival("E2", AirCraft.EmergencyStatus.FUEL, Utils.convertDateToTicks(LocalDateTime.now())));

        assertEquals("E1", queue.top().getCallSign());
        queue.dequeue();
        assertEquals("E2", queue.top().getCallSign());
        queue.dequeue();
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
    void dequeuingWhenEmptyShouldReturnNull() {
        HoldingQueue queue = new HoldingQueue(5);
        
        AirCraft result = queue.dequeue();
        assertNull(result);
    }

    @Test
    void shouldNotEnqueueNonArrivalAircraft() {
        HoldingQueue queue = new HoldingQueue(5);
        
        AirCraft departing = new AirCraft(
                AirCraft.FlightType.DEPARTURE,
                "A1",
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

        queue.enqueue(1, departing);
        assertEquals(0, queue.size());

    }    

    @Test
    void queueShouldReturnCorrectSize() {
        HoldingQueue queue = new HoldingQueue(6);

        queue.enqueue(1, createArrival("A1", AirCraft.EmergencyStatus.NONE, -1));
        queue.enqueue(2, createArrival("A2", AirCraft.EmergencyStatus.NONE, -1));
        queue.enqueue(3, createArrival("A3", AirCraft.EmergencyStatus.NONE, -1));
        queue.enqueue(4, createArrival("A4", AirCraft.EmergencyStatus.NONE, -1));

        assertEquals(4, queue.size());

        queue.dequeue();
        queue.dequeue();
        assertEquals(2, queue.size());
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

   @Test
    void getAircraftIdsShouldReturnCorrectIds() {
        HoldingQueue queue = new HoldingQueue(5);
        
        queue.enqueue(1, createArrival("A1", AirCraft.EmergencyStatus.NONE, -1));
        queue.enqueue(2, createArrival("E1", AirCraft.EmergencyStatus.FUEL, -1));
        queue.enqueue(3, createArrival("A2", AirCraft.EmergencyStatus.NONE, -1));
        
        Set<String> ids = queue.getAircraftIds();
        Set<String> expected = Set.of("E1", "A1", "A2");
        assertEquals(expected, ids);
    }

   @Test
    void getAircraftIdsShouldReturnCorrectIdsAfterRemoval() {
        HoldingQueue queue = new HoldingQueue(5);
        
        queue.enqueue(1, createArrival("A1", AirCraft.EmergencyStatus.NONE, -1));
        queue.enqueue(2, createArrival("E1", AirCraft.EmergencyStatus.FUEL, -1));
        queue.enqueue(3, createArrival("A2", AirCraft.EmergencyStatus.NONE, -1));
        queue.dequeue();
        
        Set<String> ids = queue.getAircraftIds();
        Set<String> expected = Set.of("A1", "A2");
        assertEquals(expected, ids);
    }

//    @Test
//     void getEmergencyPlanesShouldReturnCorrectAircraft() {
//         HoldingQueue queue = new HoldingQueue(6);
        
//         queue.enqueue(1, createArrival("A1", AirCraft.EmergencyStatus.NONE, -1));
//         queue.enqueue(2, createArrival("E1", AirCraft.EmergencyStatus.FUEL, -1));
//         queue.enqueue(3, createArrival("E2", AirCraft.EmergencyStatus.MECH, -1));
//         queue.enqueue(4, createArrival("A2", AirCraft.EmergencyStatus.NONE, -1));
        
//         ArrayList<AirCraft> planes = queue.getEmergencyPlanes();
//         ArrayList<AirCraft> expected = new ArrayList<>();
//         expected.add(createArrival("E1", AirCraft.EmergencyStatus.FUEL, -1));
//         expected.add(createArrival("E2", AirCraft.EmergencyStatus.MECH, -1));

//         assertEquals(planes.get(0).getCallSign(), expected.get(0).getCallSign());
//         assertEquals(planes.get(1).getCallSign(), expected.get(1).getCallSign());
//     }

    @Test
    void multipleEmergencyTypesShouldHaveCorrectOrder() {
        HoldingQueue queue = new HoldingQueue(5);

        queue.enqueue(1, createArrival("E1", AirCraft.EmergencyStatus.MECH, Utils.convertDateToTicks(LocalDateTime.now())));
        queue.enqueue(2, createArrival("E2", AirCraft.EmergencyStatus.FUEL, Utils.convertDateToTicks(LocalDateTime.now())));
        queue.enqueue(3, createArrival("E3", AirCraft.EmergencyStatus.HEALTH, Utils.convertDateToTicks(LocalDateTime.now())));
        queue.enqueue(4, createArrival("E4", AirCraft.EmergencyStatus.MECH, Utils.convertDateToTicks(LocalDateTime.now())));
        queue.enqueue(5, createArrival("E5", AirCraft.EmergencyStatus.FUEL, Utils.convertDateToTicks(LocalDateTime.now())));
        queue.enqueue(6, createArrival("E6", AirCraft.EmergencyStatus.HEALTH, Utils.convertDateToTicks(LocalDateTime.now())));

        assertEquals("E1", queue.top().getCallSign());
        queue.dequeue();
        assertEquals("E2", queue.top().getCallSign());
        queue.dequeue();
        assertEquals("E3", queue.top().getCallSign());
        queue.dequeue();
        assertEquals("E4", queue.top().getCallSign());
        queue.dequeue();
        assertEquals("E5", queue.top().getCallSign());
    }

}

