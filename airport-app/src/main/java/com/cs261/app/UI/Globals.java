package com.cs261.app.UI;
import com.cs261.app.model.AirCraft;
import com.cs261.app.model.ModelOutput;

public class Globals 
{   
     
    public static ModelOutput output;

    public static String duration;

    public static int[] runwayModes;

    public static AirCraft  GetAirplane(String callsign)
    {
        return output.getPlaneByCallsign(callsign);
    }
    

    //list for the arrivals graph

    //list for the departures graph
    
   


    
    
}
