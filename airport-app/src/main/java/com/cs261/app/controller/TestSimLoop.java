package com.cs261.app.controller;

import java.util.ArrayList;
import java.util.List;

import com.cs261.app.model.AirCraft;
import com.cs261.app.model.ModelOutput;
import com.cs261.app.model.RunWay;
import com.cs261.app.model.Utils;

public class TestSimLoop extends SimLoop{
    public TestSimLoop(RunWay[] userRunways, int userRuntime) {
        super(userRunways, userRuntime);
    }
    
    /**
     * Use this when you need to iterate the model for a number of iterations
     * Alternatively you could just run the normal call method multiple times? idk if that would work tho need to check
     * @param iterations
     * @return output
     */ 
    public ModelOutput testIteration(int iterations){
        // need to generate flights? 

        for (int i = 0; i < iterations; i++){
			List<AirCraft> newArrivals = dataGen.getArrivalsForTick(i, i+Utils.timeInc);
			List<AirCraft> newDepartures = dataGen.getDeparturesForTick(i, i+Utils.timeInc);			
			// arrivals update, add flights 
			arrivalModel.updateArrivals(newArrivals, currentTime);
			// departures update, add flights
			// TODO: departureModel.update..
			// traffic controller update, check runways and stuff
			ArrayList<String> exits = trafficModel.updateTraffic(currentTime); 
			// sort by exit time. check this sorts the right thing
			exits.sort((String a, String b) -> aircraftMap.get(a).getExitTime().compareTo(aircraftMap.get(b).getExitTime()));
			currentTime += Utils.timeInc;    
            // update output    
        }
        //return all states
        return null;
    }


}
