package com.cs261.app.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Utils {
	public static final int timeInc = 1;
    public static final float fuelBurn = 2.71f; // Per design doc.
	public static final float fuelDensity = 0.8029f; // ""
	public static final float groundSpeedKnots = 140.0f; // ""
	public static final float baseArrivalAltitudeMetres = 304.8f; // ""
	public static final int runwayTime = 2;
	public static final int emergencyTime = 4; //maximum emergency wait time
	public static final LocalDateTime simulationStart = LocalDateTime.of(2026, 1, 1, 0, 0);
	public static final int flow = 15; //might make this configurable later 

	public static int cancelTime = 30; // maximum takeoff queue wait time
	public static int dangerOfCancel = 20; // time at which planes are considered close to being cancelled
	public static double emergencyProbability = 0.01;

	// Setters
	// for readability
	public static void setCancelTime(int c){
		cancelTime = c;
		dangerOfCancel = c - 10;
	}

	public static void setEmergencyProbability(double e){
		emergencyProbability = e;
	}

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