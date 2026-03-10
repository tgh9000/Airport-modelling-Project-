package com.cs261.app.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TrafficControllerTest {

    private static RunWay landingRunway(int bearing) {
        return new RunWay(
                3000,
                bearing,
                RunWay.OperatingMode.LANDING,
                new RunWay.OperationStatus[0],
                new int[0][0]
        );
    }

    private static RunWay takeoffRunway(int bearing) {
        return new RunWay(
                3000,
                bearing,
                RunWay.OperatingMode.TAKEOFF,
                new RunWay.OperationStatus[0],
                new int[0][0]
        );
    }

    private static RunWay mixedRunway(int bearing) {
        return new RunWay(
                3000,
                bearing,
                RunWay.OperatingMode.MIXED,
                new RunWay.OperationStatus[0],
                new int[0][0]
        );
    }

    private static AirCraft arrivalAircraft(String callsign) {
        return new AirCraft(
                AirCraft.FlightType.ARRIVAL,
                callsign,
                "TestAir",
                "AAA",
                "BBB",
                Utils.convertTicksToDate(0),
                Utils.convertTicksToDate(0),
                200f,
                1000f,
                3000f,
                AirCraft.EmergencyStatus.NONE,
                0
        );
    }

    private static AirCraft departureAircraft(String callsign) {
        return new AirCraft(
                AirCraft.FlightType.DEPARTURE,
                callsign,
                "TestAir",
                "AAA",
                "BBB",
                Utils.convertTicksToDate(0),
                Utils.convertTicksToDate(0),
                200f,
                1000f,
                0f,
                AirCraft.EmergencyStatus.NONE,
                0
        );
    }

    private static AirCraft emergencyArrivalAircraft(String callsign) {
        return new AirCraft(
                AirCraft.FlightType.ARRIVAL,
                callsign,
                "TestAir",
                "AAA",
                "BBB",
                Utils.convertTicksToDate(0),
                Utils.convertTicksToDate(0),
                200f,
                1000f,
                3000f,
                AirCraft.EmergencyStatus.FUEL,
                0
        );
    }

    @Test
    void updateTraffic_emptyQueues_shouldNotThrow() {
        AirCraftMap aircraftMap = new AirCraftMap();
        HoldingQueue holdingQueue = new HoldingQueue(10);
        TakeOffQueue takeOffQueue = new TakeOffQueue();
        RunWayMap runways = new RunWayMap(new RunWay[]{
                mixedRunway(90),
                landingRunway(180),
                takeoffRunway(270)
        });

        ArrivalModel arrivalModel = new ArrivalModel(aircraftMap, holdingQueue);
        DepartureModel departureModel = new DepartureModel(aircraftMap, takeOffQueue);

        TrafficController controller = new TrafficController(
                aircraftMap, runways, holdingQueue, takeOffQueue, arrivalModel, departureModel
        );

        assertDoesNotThrow(() -> controller.updateTraffic(0));
    }

    @Test
    void updateTraffic_moreLandingRunwaysThanHoldingAircraft_shouldNotThrow() {
        AirCraftMap aircraftMap = new AirCraftMap();
        HoldingQueue holdingQueue = new HoldingQueue(10);
        TakeOffQueue takeOffQueue = new TakeOffQueue();
        RunWayMap runways = new RunWayMap(new RunWay[]{
                landingRunway(90),
                landingRunway(100)
        });

        ArrivalModel arrivalModel = new ArrivalModel(aircraftMap, holdingQueue);
        DepartureModel departureModel = new DepartureModel(aircraftMap, takeOffQueue);

        AirCraft arrival = arrivalAircraft("ARR1");
        aircraftMap.put(arrival);
        holdingQueue.enqueue(0, arrival);

        TrafficController controller = new TrafficController(
                aircraftMap, runways, holdingQueue, takeOffQueue, arrivalModel, departureModel
        );

        assertDoesNotThrow(() -> controller.updateTraffic(0));
    }

    @Test
    void updateTraffic_moreTakeoffRunwaysThanQueuedAircraft_shouldNotThrow() {
        AirCraftMap aircraftMap = new AirCraftMap();
        HoldingQueue holdingQueue = new HoldingQueue(10);
        TakeOffQueue takeOffQueue = new TakeOffQueue();
        RunWayMap runways = new RunWayMap(new RunWay[]{
                takeoffRunway(270),
                takeoffRunway(280)
        });

        ArrivalModel arrivalModel = new ArrivalModel(aircraftMap, holdingQueue);
        DepartureModel departureModel = new DepartureModel(aircraftMap, takeOffQueue);

        AirCraft departure = departureAircraft("DEP1");
        aircraftMap.put(departure);
        takeOffQueue.enqueue(departure);

        TrafficController controller = new TrafficController(
                aircraftMap, runways, holdingQueue, takeOffQueue, arrivalModel, departureModel
        );

        assertDoesNotThrow(() -> controller.updateTraffic(0));
    }

    @Test
    void updateTraffic_arrivalWithFreeLandingRunway_shouldAssignRunway() {
        AirCraftMap aircraftMap = new AirCraftMap();
        HoldingQueue holdingQueue = new HoldingQueue(10);
        TakeOffQueue takeOffQueue = new TakeOffQueue();

        RunWay landing = landingRunway(180);
        RunWayMap runways = new RunWayMap(new RunWay[]{ landing });

        ArrivalModel arrivalModel = new ArrivalModel(aircraftMap, holdingQueue);
        DepartureModel departureModel = new DepartureModel(aircraftMap, takeOffQueue);

        AirCraft arrival = arrivalAircraft("ARR1");
        aircraftMap.put(arrival);
        holdingQueue.enqueue(0, arrival);

        TrafficController controller = new TrafficController(
                aircraftMap, runways, holdingQueue, takeOffQueue, arrivalModel, departureModel
        );

        ArrayList<String> exited = controller.updateTraffic(0);

        assertTrue(holdingQueue.isEmpty());
        assertTrue(landing.hasPlane());
        assertEquals(arrival.getCallSign(), landing.getCurrentPlane());
        assertNotNull(arrival.getExitTime());
        assertEquals(1, arrivalModel.getLanded());
        assertTrue(exited.contains("ARR1"));
    }

    @Test
    void updateTraffic_departureWithFreeTakeoffRunway_shouldAssignRunway() {
        AirCraftMap aircraftMap = new AirCraftMap();
        HoldingQueue holdingQueue = new HoldingQueue(10);
        TakeOffQueue takeOffQueue = new TakeOffQueue();

        RunWay takeoff = takeoffRunway(270);
        RunWayMap runways = new RunWayMap(new RunWay[]{ takeoff });

        ArrivalModel arrivalModel = new ArrivalModel(aircraftMap, holdingQueue);
        DepartureModel departureModel = new DepartureModel(aircraftMap, takeOffQueue);

        AirCraft departure = departureAircraft("DEP1");
        aircraftMap.put(departure);
        takeOffQueue.enqueue(departure);

        TrafficController controller = new TrafficController(
                aircraftMap, runways, holdingQueue, takeOffQueue, arrivalModel, departureModel
        );

        ArrayList<String> exited = controller.updateTraffic(0);

        assertTrue(takeOffQueue.isEmpty());
        assertTrue(takeoff.hasPlane());
        assertEquals(departure.getCallSign(), takeoff.getCurrentPlane());
        assertNotNull(departure.getExitTime());
        assertEquals(1, departureModel.getTakenOff());
        assertTrue(exited.contains("DEP1"));
    }

    @Test
    void updateTraffic_emergencyArrivalWithMixedRunway_shouldAssignMixedRunway() {
        AirCraftMap aircraftMap = new AirCraftMap();
        HoldingQueue holdingQueue = new HoldingQueue(10);
        TakeOffQueue takeOffQueue = new TakeOffQueue();

        RunWay mixed = mixedRunway(90);
        RunWayMap runways = new RunWayMap(new RunWay[]{ mixed });

        ArrivalModel arrivalModel = new ArrivalModel(aircraftMap, holdingQueue);
        DepartureModel departureModel = new DepartureModel(aircraftMap, takeOffQueue);

        AirCraft emergencyArrival = emergencyArrivalAircraft("EM1");
        aircraftMap.put(emergencyArrival);
        holdingQueue.enqueue(0, emergencyArrival);

        TrafficController controller = new TrafficController(
                aircraftMap, runways, holdingQueue, takeOffQueue, arrivalModel, departureModel
        );

        ArrayList<String> exited = controller.updateTraffic(0);

        assertTrue(holdingQueue.isEmpty());
        assertTrue(mixed.hasPlane());
        assertEquals(emergencyArrival.getCallSign(), mixed.getCurrentPlane());
        assertNotNull(emergencyArrival.getExitTime());
        assertEquals(1, arrivalModel.getLanded());
        assertTrue(exited.contains("EM1"));
    }
}