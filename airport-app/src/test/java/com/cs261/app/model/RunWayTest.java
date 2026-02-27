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
                400,
                8000,
                30000,
                AirCraft.EmergencyStatus.NONE
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
        runway.addPlane(plane);

        assertEquals("A1", runway.getCurrentPlane());
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
    void mixedModeShouldAlternate() {
        RunWay runway = new RunWay(3000, 120,
                RunWay.OperatingMode.MIXED,
                null,
                null);

        assertEquals(RunWay.OperatingMode.LANDING,
                runway.getMixedModeTurn());
    }
}
