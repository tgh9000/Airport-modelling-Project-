package com.cs261.app.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MapsTest {

    private RunWayMap map;
    private RunWay arrivalRunway;
    private RunWay departureRunway;
    private RunWay mixedRunway;

    @BeforeEach
    void setup() {
        arrivalRunway = new RunWay(
                3000,
                90, // runway number "09"
                RunWay.OperatingMode.LANDING,
                null,
                null
        );

        departureRunway = new RunWay(
                3000,
                180, // runway number "18"
                RunWay.OperatingMode.TAKEOFF,
                null,
                null
        );

        mixedRunway = new RunWay(
                3000,
                270, // runway number "27"
                RunWay.OperatingMode.MIXED,
                null,
                null
        );

        RunWay[] runways = {arrivalRunway, departureRunway, mixedRunway};
        map = new RunWayMap(runways);
    }

    @Test
    void getRunwayShouldReturnCorrectRunwayByCalculatedId() {
        RunWay result = map.getRunway("09");

        assertNotNull(result);
        assertEquals("09", result.getRunwayNumber());
    }

    @Test
    void sizeShouldReturnCorrectNumberOfRunways() {
        assertEquals(3, map.size());
    }

    @Test
    void checkAvailableShouldIncludeMixedRunways() {
        int availableLanding = map.checkAvailable(RunWay.OperatingMode.LANDING);

        // Landing runway + Mixed runway
        assertEquals(2, availableLanding);
    }

    @Test
    void getMixedRunwayShouldReturnAvailableMixedRunway() {
        RunWay result = map.getMixedRunWay();

        assertNotNull(result);
        assertEquals(RunWay.OperatingMode.MIXED, result.getMode());
    }

    @Test
    void getFreeRunwaysShouldReturnListWhenAvailable() {
        ArrayList<RunWay> free =
                map.getFreeRunways(RunWay.OperatingMode.LANDING);

        assertNotNull(free);
    }

    @Test
    void getBusyRunwaysShouldInitiallyBeEmpty() {
        ArrayList<RunWay> busy =
                map.getBusyRunways(RunWay.OperatingMode.LANDING);

        assertTrue(busy.isEmpty());
    }

    @Test
    void getRunwayShouldReturnNullIfIdDoesNotExist() {
        assertNull(map.getRunway("99"));
    }
}