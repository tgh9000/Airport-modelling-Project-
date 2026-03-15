package com.cs261.app.UI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.cs261.app.model.AirCraft;
import com.cs261.app.model.ModelOutput;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

public class Globals 
{   
    public static ModelOutput output;

    public static String duration;

    public static String totalTicks;

    public static int[] runwayModes;
    
    // lists used for the grapsh
    public static XYChart.Series<Number, Number> departSeries; 
    public static XYChart.Series<Number, Number> arriveSeries; 
    public static XYChart.Series<Number, Number> tQSeries;
    public static XYChart.Series<Number, Number> hPSeries; 

    // trees required for the tables.
    public static TreeMap<Integer,List<ArrivalRow>> arrivalsTree = new TreeMap<>();
    public static TreeMap<Integer, List<ArrivalRow>> departuresTree = new TreeMap<>();
    public static TreeMap<Integer, List<QueueRow>> queueTree = new TreeMap<>();
    public static TreeMap<Integer, List<QueueRow>> holdingTree = new TreeMap<>();

    public static HashMap<Integer,TreeMap<Integer, List<RunwayRow>>> RunwayTree = new HashMap<>();

    public static ObservableList<RunwayDetail> runwayDetails = FXCollections.observableArrayList();
    public static ObservableList<AirCraftDetail> airCraftDetails = FXCollections.observableArrayList();

    /**
     * returns a subset of the arrivals tree whose arrival time is within the given range.
     * @param TimeStamp the start of the time range.
     * @param endTStamp the end of the time range.
     * @return an observable list of ArrivalRow objects that fall within the specified time range.
     */
    public static ObservableList<ArrivalRow>  getArrivalsTableDataRange( int TimeStamp,int endTStamp)
    {
        Map<Integer, List<ArrivalRow>> result = arrivalsTree.subMap(TimeStamp, endTStamp);
        ObservableList<ArrivalRow> data = javafx.collections.FXCollections.observableArrayList();
        for (List<ArrivalRow> rows : result.values()) {
            data.addAll(rows);
        }
        return data;
    }

    /**
     * returns the entire arrivals tree as an observable list of ArrivalRow objects. 
      * @return an observable list of all ArrivalRow objects in the arrivalsTree.
     */
    public static ObservableList<ArrivalRow>  getArrivalsTableData( )
    {
        ObservableList<ArrivalRow> data = javafx.collections.FXCollections.observableArrayList();
        for (Map.Entry<Integer, List<ArrivalRow>> entry : arrivalsTree.entrySet()) {
            data.addAll(entry.getValue());
        }
        

        
        return data;
    }

    /**
     * returns a subset of the departures tree whose departure time is within the given range.
     * @param TimeStamp the start of the time range.
     * @param endTStamp the end of the time range.
     * @return an observable list of ArrivalRow objects that fall within the specified time range.
     */
    public static ObservableList<ArrivalRow>  getDeparturesTableDataRange( int TimeStamp,int endTStamp)
    {
        Map<Integer, List<ArrivalRow>> result = departuresTree.subMap(TimeStamp, endTStamp);
        ObservableList<ArrivalRow> data = javafx.collections.FXCollections.observableArrayList();
        for (List<ArrivalRow> rows : result.values()) {
            data.addAll(rows);
        }

        return data;
    }

    /**
     * returns the entire departures tree as an observable list of ArrivalRow objects. 
      * @return an observable list of all ArrivalRow objects in the departuresTree.
     */
    public static ObservableList<ArrivalRow>  getDeparturesTableData( )
    {
        ObservableList<ArrivalRow> data = javafx.collections.FXCollections.observableArrayList();
        for (Map.Entry<Integer, List<ArrivalRow>> entry : departuresTree.entrySet()) {
            data.addAll(entry.getValue());
        }
        

        
        return data;
    }

    /**
     * returns a subset of the queue tree whose time is within the given range.
     * @param TimeStamp the start of the time range.
     * @param endTStamp the end of the time range.
     * @return an observable list of QueueRow objects that fall within the specified time range.
     */
    public static ObservableList<QueueRow>  getQueueTableDataRange( int TimeStamp,int endTStamp)
    {
        Map<Integer, List<QueueRow>> result = queueTree.subMap(TimeStamp, endTStamp);
        ObservableList<QueueRow> data = javafx.collections.FXCollections.observableArrayList();
        for (List<QueueRow> rows : result.values()) {
            data.addAll(rows);
        }


        return data;
    }

    /**
     * returns the entire queue tree as an observable list of QueueRow objects.
     * @return an observable list of all QueueRow objects in the queueTree.
     */
    public static ObservableList<QueueRow>  getQueueTableData( )
    {
        ObservableList<QueueRow> data = javafx.collections.FXCollections.observableArrayList();
        for (Map.Entry<Integer, List<QueueRow>> entry : queueTree.entrySet()) {
            data.addAll(entry.getValue());
        }
        
        return data;
    }


    /**
     * returns a subset of the holding tree whose time is within the given range.
     * @param TimeStamp the start of the time range.
     * @param endTStamp the end of the time range.
     * @return an observable list of QueueRow objects that fall within the specified time range.
     */
    public static ObservableList<QueueRow>  getHoldingTableDataRange( int TimeStamp,int endTStamp)
    {
        Map<Integer, List<QueueRow>> result = holdingTree.subMap(TimeStamp, endTStamp);
        ObservableList<QueueRow> data = javafx.collections.FXCollections.observableArrayList();
        for (List<QueueRow> rows : result.values()) {
            data.addAll(rows);
        }


        return data;
    }

    /**
     * returns the entire holding tree as an observable list of QueueRow objects.
     * @return an observable list of all QueueRow objects in the holdingTree.
     */
    public static ObservableList<QueueRow>  getHoldingTableData( )
    {
        ObservableList<QueueRow> data = javafx.collections.FXCollections.observableArrayList();
        for (Map.Entry<Integer, List<QueueRow>> entry : holdingTree.entrySet()) {
            data.addAll(entry.getValue());
        }
        

        
        return data;
    }

    /**
     * returns a subset of the runway tree whose time is within the given range.
     * @param TimeStamp the start of the time range.
     * @param endTStamp the end of the time range.
     * @param runway the runway number.
     * @return an observable list of RunwayRow objects that fall within the specified time range.
     */
    public static ObservableList<RunwayRow>  getRunwayTableDataRange( int TimeStamp,int endTStamp,int runway)
    {
        TreeMap<Integer, List<RunwayRow>> runwayData = RunwayTree.get(runway);
        Map<Integer, List<RunwayRow>> result = runwayData.subMap(TimeStamp, endTStamp);
        ObservableList<RunwayRow> data = javafx.collections.FXCollections.observableArrayList();
        for (List<RunwayRow> rows : result.values()) {
            data.addAll(rows);
        }

        return data;
    }

    /**
     * returns a subset of the runway tree for a specific runway.
     * @param runway the runway number.
     * @return an observable list of RunwayRow objects for the specified runway.
     * 
     */
    public static ObservableList<RunwayRow>  getSingleRunwayTableData( int runway)
    {
        TreeMap<Integer, List<RunwayRow>> runwayData = RunwayTree.get(runway);
        ObservableList<RunwayRow> data = javafx.collections.FXCollections.observableArrayList();
        for (Map.Entry<Integer, List<RunwayRow>> entry : runwayData.entrySet()) {
            data.addAll(entry.getValue());
        }
        

        
        return data;
    }

    /**
     * returns the entire runway tree as an observable list of RunwayRow objects.
     * @return an observable list of all RunwayRow objects in the runwayTree.
     */
    public static ObservableList<RunwayRow>  getRunwayTableData( )
    {
        ObservableList<RunwayRow> data = javafx.collections.FXCollections.observableArrayList();
        for (Map.Entry<Integer, TreeMap<Integer, List<RunwayRow>>> runwayEntry : RunwayTree.entrySet()) {
            TreeMap<Integer, List<RunwayRow>> runwayData = runwayEntry.getValue();
            for (Map.Entry<Integer, List<RunwayRow>> entry : runwayData.entrySet()) {
                data.addAll(entry.getValue());
            }
        }
        

        
        return data;
    }

    /**
     * returns the AirCraft object associated with the given callsign. 
     * @param callsign the callsign of the airplane for which the AirCraft object is to be retrieved.
     * @return the AirCraft object associated with the given callsign, or null if no such object exists.
     */
    public static AirCraft  GetAirplane(String callsign)
    {
        return output.getPlaneByCallsign(callsign);
    }
}
