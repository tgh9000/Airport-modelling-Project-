package com.cs261.app.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class RunWayTest {

    private AirCraft createArrival(String id) {
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
                AirCraft.EmergencyStatus.NONE,
                -1
        );
    }

    @Test
    void runwayShouldStartAvailable() {
        RunWay runway = new RunWay(3000, 120,
                RunWay.OperatingMode.LANDING,
                null,
                null);

        assertEquals(RunWay.OperationStatus.AVAILABLE, runway.getStatus());
    }

    @Test
    void shouldAssignPlaneIfModeMatches() {
        RunWay runway = new RunWay(3000, 120,
                RunWay.OperatingMode.LANDING,
                null,
                null);

        AirCraft plane = createArrival("A1");
        boolean added = runway.addPlane(plane);
        assertTrue(added);
        assertEquals("A1", runway.getCurrentPlane());
    }

    @Test
    void shouldNotAssignPlaneIfModeDoesNotMatch() {
        RunWay runway = new RunWay(3000, 120,
                RunWay.OperatingMode.TAKEOFF,
                null,
                null);

        AirCraft plane = createArrival("A1");
        runway.addPlane(plane);

        assertNull(runway.getCurrentPlane());
    }

    @Test
    void addingNullPlaneReturnsFalse() {
                RunWay runway = new RunWay(3000, 120,
                RunWay.OperatingMode.LANDING,
                null,
                null);
        assertFalse(runway.addPlane(null));
    }

    @Test
    void removePlaneShouldClearRunway() {
        RunWay runway = new RunWay(3000, 120,
                RunWay.OperatingMode.LANDING,
                null,
                null);

        AirCraft plane = createArrival("A1");
        runway.addPlane(plane);

        assertTrue(runway.hasPlane());

        runway.removePlane();

        assertFalse(runway.hasPlane());
    }

    @Test
    void removePlaneShouldReturnNullIfNoPlaneOnRunway() {
        RunWay runway = new RunWay(3000, 120,
                RunWay.OperatingMode.LANDING,
                null,
                null);
        assertNull(runway.removePlane());

    }

    @Test
    void mixedModeShouldAlternate() {
        RunWay runway = new RunWay(3000, 120,
                RunWay.OperatingMode.MIXED,
                null,
                null);

        assertEquals(RunWay.OperatingMode.LANDING,
                runway.getMixedModeTurn());

        AirCraft plane = createArrival("A1");
        runway.addPlane(plane);
        assertEquals(RunWay.OperatingMode.TAKEOFF,
                runway.getMixedModeTurn());
    }

    @Test
    void convertRunwayShouldConvertToMixed() {
        RunWay runway = new RunWay(3000, 120,
                RunWay.OperatingMode.LANDING,
                null,
                null);

        assertEquals(RunWay.OperatingMode.LANDING,
                runway.getMode());

        runway.convertRunway();
        assertEquals(RunWay.OperatingMode.MIXED,
                runway.getMode());
    }

}
