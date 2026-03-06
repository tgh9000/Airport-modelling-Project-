package com.cs261.app.UI;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.cs261.app.model.AirCraft;
import com.cs261.app.model.ModelOutput;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

public class Globals 
{   
     
    public static ModelOutput output;

    public static String duration;

    public static int[] runwayModes;

    // lists used for the grapsh
    public static XYChart.Series<Number, Number> departSeries; 
    public static XYChart.Series<Number, Number> arriveSeries; 

    // trees required for the tables.
    public static TreeMap<Integer,List<ArrivalRow>> arrivalsTree;
    public static TreeMap<Integer, AirCraft> departuresTree;

    public static ObservableList<ArrivalRow>  getArrivalsTableData( int TimeStamp)
    {
        Map<Integer, List<ArrivalRow>> result = arrivalsTree.subMap(TimeStamp, TimeStamp + 15);
        ObservableList<ArrivalRow> data = javafx.collections.FXCollections.observableArrayList();
        for (List<ArrivalRow> rows : result.values()) {
            data.addAll(rows);
        }


        return data;
    }

    public static AirCraft  GetAirplane(String callsign)
    {
        return output.getPlaneByCallsign(callsign);
    }
    

    //list for the arrivals graph

    //list for the departures graph
    
   


    
    
}
