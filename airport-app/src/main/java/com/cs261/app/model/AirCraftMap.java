package com.cs261.app.model;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Hashmap of all flights in the simulation. 
 */

public class AirCraftMap extends ConcurrentHashMap<String, AirCraft>{

	public AirCraftMap() {
		super(500, 0.75f, 2);
	}
	
	// Override put method
	public AirCraft put(AirCraft plane) {
		return put(plane.getCallSign(), plane);
	}
}
