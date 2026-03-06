package com.cs261.app.UI;

import com.cs261.app.model.ModelOutput;
import com.cs261.app.model.AirCraft;
import com.cs261.app.model.Utils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.collections.ObservableList;

import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

 


public class OutputPageController {
    public ModelOutput output;
    
    public void init(ModelOutput output){
        this.output = output;
        Globals.output = output; 


        //calculate the arrivals and depaartures graph data
        initializeCoords();
        
        //table trees
        

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
        String[][] tableAllArrivals = output.getTableAllArrivals();

        for(int i = 0; i < tableAllArrivals.length; i++)
        {   //Runway,callsign,scheduled,actual,delay
 

            ArrivalRow row = new ArrivalRow(tableAllArrivals[i][0], tableAllArrivals[i][1], tableAllArrivals[i][2], tableAllArrivals[i][3], tableAllArrivals[i][4] );
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
