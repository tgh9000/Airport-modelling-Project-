package com.cs261.app.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void convertTicksToDateShouldIncreaseByCorrectMinutes() {
        LocalDateTime result = Utils.convertTicksToDate(5);
        LocalDateTime expected = LocalDateTime.of(2026, 1, 1, 0, 0)
                .plusMinutes(5 * Utils.timeInc);

        assertEquals(expected, result);
    }

    @Test
    void convertDateToTicksShouldReturnCorrectTickValue() {
        LocalDateTime date = LocalDateTime.of(2026, 1, 1, 0, 20);
        int ticks = Utils.convertDateToTicks(date);

        assertEquals(10, ticks); // 20 minutes / 2 minute increments
    }

    @Test
    void conversionShouldBeReversible() {
        int originalTick = 15;

        LocalDateTime date = Utils.convertTicksToDate(originalTick);
        int convertedBack = Utils.convertDateToTicks(date);

        assertEquals(originalTick, convertedBack);
    }

    @Test
    void constantsShouldHaveExpectedValues() {
        assertEquals(2, Utils.timeInc);
        assertEquals(6, Utils.runwayTime);
        assertEquals(4, Utils.emergencyTime);
        assertEquals(2.70f, Utils.fuelBurn);
    }
}