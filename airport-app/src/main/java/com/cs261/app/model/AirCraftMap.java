package com.cs261.app.model;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Hashmap of all flights in the simulation. 
 */

public class AirCraftMap extends ConcurrentHashMap<String, AirCraft>{

	/**
	 * whats this for??
	 */
	private static final long serialVersionUID = -3959847986262784252L;
    /*
    TODO: extends hashmap. for now, just a constructor that does super() and nothing else.
    TODO: think about what other things need to be added 
    */
	
	public AirCraftMap() {
		super(500, 0.75f, 2);
	}
	
	public AirCraft put(AirCraft plane) {
		return put(plane.getCallSign(), plane);
	}
}
