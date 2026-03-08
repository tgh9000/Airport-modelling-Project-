package com.cs261.app.UI;

import java.util.ArrayList;
import java.util.List;

import com.cs261.app.model.ModelOutput;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class OutputPageController {
    public ModelOutput output;
    
    public void init(ModelOutput output){
        this.output = output;
        Globals.output = output; 

        
        //calculate the arrivals and depaartures graph data
        initializeCoords();
        
        //table trees
        loadTables();

        //

        //go load the right page after this.
        try {
            switchScene();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void loadTables()
    {
        //arrivals table.
        String[][] tableAllArrivals = output.getTableAllArrivals();

        for(int i = 0; i < tableAllArrivals.length; i++)
        {   //Runway,callsign,actual
 

            ArrivalRow row = new ArrivalRow(tableAllArrivals[i][1], tableAllArrivals[i][0], tableAllArrivals[i][2], tableAllArrivals[i][3],tableAllArrivals[i][4]);
            String timeStamp = tableAllArrivals[i][5];
            List<ArrivalRow> list = Globals.arrivalsTree.get( Integer.parseInt(timeStamp));
            if(list == null)
            {
                list = new ArrayList<>();
            }
            list.add(row);

            Globals.arrivalsTree.put(Integer.parseInt(timeStamp), list);
            //List<ArrivalRow> list = Globals.arrivalsTree.getOrDefault(row.getScheduled(), new ArrayList<>());
            
            
        }
       // System.out.println("Arrivals tree size: " + Globals.arrivalsTree.size());
        //departures table.
        String[][] tableAllDepartures = output.getTableAllDepartures();
        for(int i = 0; i < tableAllDepartures.length; i++)
        {   //Runway,callsign,actual
 

            ArrivalRow row = new ArrivalRow(tableAllDepartures[i][1], tableAllDepartures[i][0], tableAllDepartures[i][2], tableAllDepartures[i][3],tableAllDepartures[i][4]);
            String timeStamp = tableAllDepartures[i][5];
            List<ArrivalRow> list = Globals.departuresTree.get( Integer.parseInt(timeStamp));
            if(list == null)
            {
                list = new ArrayList<>();
            }
            list.add(row);

            Globals.departuresTree.put(Integer.parseInt(timeStamp), list);
            //List<ArrivalRow> list = Globals.arrivalsTree.getOrDefault(row.getScheduled(), new ArrayList<>());
            
            
        }
        //System.out.println("test2");


        //queue table
        String[][] tableAllqueue = output.getTableTakeOffQueue();
        for(int i = 0; i < tableAllqueue.length; i++)
        {   //Runway,callsign,actual
 

            QueueRow row = new QueueRow(tableAllDepartures[i][0],tableAllDepartures[i][1],  tableAllDepartures[i][2], tableAllDepartures[i][3],tableAllDepartures[i][4]);
            String timeStamp = tableAllDepartures[i][5];
            List<QueueRow> list = Globals.queueTree.get( Integer.parseInt(timeStamp));
            if(list == null)
            {
                list = new ArrayList<>();
            }
            list.add(row);

            Globals.queueTree.put(Integer.parseInt(timeStamp), list);
            //List<ArrivalRow> list = Globals.arrivalsTree.getOrDefault(row.getScheduled(), new ArrayList<>());
            
            
        }
        



        //Holding Pattern table
        String[][] tableAllHolding = output.holdingQueueTable();
        for(int i = 0; i < tableAllHolding.length; i++)
        {   //Runway,callsign,actual
 

            QueueRow row = new QueueRow(tableAllHolding[i][0],tableAllHolding[i][1],  tableAllHolding[i][2], tableAllHolding[i][3],tableAllHolding[i][4]);
            String timeStamp = tableAllHolding[i][5];
            List<QueueRow> list = Globals.holdingTree.get( Integer.parseInt(timeStamp));
            if(list == null)
            {
                list = new ArrayList<>();
            }
            list.add(row);

            Globals.holdingTree.put(Integer.parseInt(timeStamp), list);
            //List<ArrivalRow> list = Globals.arrivalsTree.getOrDefault(row.getScheduled(), new ArrayList<>());
            
            
        }

    }


    @FXML
    private void initializeCoords() 
    {
        ArrayList<Double[]>  depart = output.getGraphDepartDifferences();
        
        ArrayList<Double[]> arrive = output.getGraphArrivalDifferences();
        System.out.println("Depart size: " + depart.size());
        System.out.println("Arrive size: " + arrive.size());
        XYChart.Series<Number, Number> departSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> arriveSeries = new XYChart.Series<>();

        for (int i = 0; i < depart.size(); i++) 
        {
            departSeries.getData().add(new XYChart.Data<>(depart.get(i)[0], depart.get(i)[1]));
            
        }
        for (int i = 0; i < arrive.size(); i++) 
        {
            arriveSeries.getData().add(new XYChart.Data<>(arrive.get(i)[0], arrive.get(i)[1]));
            
        }
        Globals.departSeries = departSeries;
        Globals.arriveSeries = arriveSeries;
        //System.out.println("Coords initialized and the size of the depart series is: " + departSeries.getData().size());
        //System.out.println("Coords initialized and the size of the arrive series is: " + arriveSeries.getData().size());
    }


    @FXML
    private StackPane outputPageStackPane;

    private void switchScene() throws Exception 
    {

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/cs261/app/keyData.fxml")
        );

        Parent root = loader.load();


        
        Stage stage = (Stage) outputPageStackPane.getScene().getWindow();
        
        stage.setScene(new Scene(root));
        stage.setMaximized(true);


    }
}
