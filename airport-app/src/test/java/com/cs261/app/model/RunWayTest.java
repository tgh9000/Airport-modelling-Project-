package com.cs261.app.model;

import org.junit.jupiter.api.Test;

import com.cs261.app.model.RunWay.OperationStatus;

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

    @Test
    void setStatusShouldChangeStatusCorrectly() {
        RunWay runway = new RunWay(3000, 120,
                RunWay.OperatingMode.LANDING,
                null,
                null);

        assertEquals(RunWay.OperationStatus.AVAILABLE,
                runway.getStatus());

        runway.setStatus(RunWay.OperationStatus.SNOW);
        assertEquals(RunWay.OperationStatus.SNOW,
                runway.getStatus());

        runway.setStatus(RunWay.OperationStatus.INSPECTION);
        assertEquals(RunWay.OperationStatus.INSPECTION,
                runway.getStatus());
    }

    @Test
    void hasStatusChangeWhenNoChange() {
        RunWay runway = new RunWay(3000, 120,
                RunWay.OperatingMode.LANDING,
                new RunWay.OperationStatus[]{},
                new int[][]{});

        assertFalse(runway.hasStatusChange());
    }

    @Test
    void hasStatusChangeShouldWorkWhenChange() {
        RunWay runway = new RunWay(3000, 120,
                RunWay.OperatingMode.LANDING,
                new RunWay.OperationStatus[]{RunWay.OperationStatus.INSPECTION},
                new int[][]{{5,10}});

        assertTrue(runway.hasStatusChange());
    }

    @Test
    void addStatusAddsStatusDuringTheRightTime() {
        RunWay runway = new RunWay(3000, 120,
                RunWay.OperatingMode.LANDING,
                new RunWay.OperationStatus[]{RunWay.OperationStatus.INSPECTION},
                new int[][]{{5,10}});

        assertEquals(RunWay.OperationStatus.AVAILABLE, runway.getStatus());
        runway.addStatus(5);
        assertEquals(RunWay.OperationStatus.INSPECTION, runway.getStatus());
    }

    @Test
    void removeStatusRemovesStatusDuringTheRightTime() {
        RunWay runway = new RunWay(3000, 120,
                RunWay.OperatingMode.LANDING,
                new RunWay.OperationStatus[]{RunWay.OperationStatus.INSPECTION},
                new int[][]{{5,10}});

        runway.addStatus(5);
        assertEquals(RunWay.OperationStatus.INSPECTION, runway.getStatus());
        runway.removeStatus(10);
        assertEquals(RunWay.OperationStatus.AVAILABLE, runway.getStatus());
    }

    @Test
    void addStatusAddsStatusDuringTheRightTimeWithMultipleStatuses() {
        RunWay runway = new RunWay(3000, 120,
                RunWay.OperatingMode.LANDING,
                new RunWay.OperationStatus[]{RunWay.OperationStatus.INSPECTION, RunWay.OperationStatus.SNOW},
                new int[][]{{5,10}, {15,20}});

        runway.addStatus(5);
        assertEquals(RunWay.OperationStatus.INSPECTION, runway.getStatus());
        runway.removeStatus(10);
        runway.addStatus(15);
        assertEquals(RunWay.OperationStatus.SNOW, runway.getStatus());
    }

    @Test
    void doesNotAddStatusAtTheWrongTime() {
        RunWay runway = new RunWay(3000, 120,
                RunWay.OperatingMode.LANDING,
                new RunWay.OperationStatus[]{RunWay.OperationStatus.INSPECTION},
                new int[][]{{5,10}});

        assertEquals(RunWay.OperationStatus.AVAILABLE, runway.getStatus());
        runway.addStatus(3);
        assertEquals(RunWay.OperationStatus.AVAILABLE, runway.getStatus());
    }

    @Test
    void doesNotRemoveStatusAtTheWrongTime() {
        RunWay runway = new RunWay(3000, 120,
                RunWay.OperatingMode.LANDING,
                new RunWay.OperationStatus[]{RunWay.OperationStatus.INSPECTION},
                new int[][]{{5,10}});

        runway.addStatus(5);
        assertEquals(RunWay.OperationStatus.INSPECTION, runway.getStatus());
        runway.removeStatus(7);
        assertEquals(RunWay.OperationStatus.INSPECTION, runway.getStatus());
    }

}
