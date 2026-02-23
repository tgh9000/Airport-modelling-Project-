package com.cs261.app.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Utils {
	public static final int timeInc = 2;
    public static final float fuelBurn = 2.70f;
	public static final int runwayTime = 6;
	public static final int emergencyTime = 4; //maximum emergency wait time

	/**
	 * Convert from ticks to LocalDateTime
	 * Adding number of minutes to january 1st 2026 00:00
	 */

	public static LocalDateTime convertTicksToDate(int t){
		LocalDateTime date = LocalDateTime.of(2026, 1, 1, 0, 0).plusMinutes(t * Utils.timeInc);
		return date;
	}


	/**
	 * Convert from LocalDateTime to ticks
	 * finding number of minutes/2 from january 1st 2026 00:00
	 */
	public static int convertDateToTicks(LocalDateTime d){
		LocalDateTime s = LocalDateTime.of(2026, 1, 1, 0, 0);
		long mins = Duration.between(s, d).toMinutes();
		int ticks = (int) mins / Utils.timeInc;
		return ticks;
	}

}