package com.cs261.app.UI;

import com.cs261.app.model.ModelOutput;

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
        {
            ObservableList<ArrivalRow> data = javafx.collections.FXCollections.observableArrayList();
            for(int j = 0; j < tableAllArrivals[i].length; j++)
            {
                //data.add(new ArrivalRow(tableAllArrivals[i][j]));
            }
            
        }
    }


    @FXML
    private void initializeCoords() 
    {
        int[][] depart = output.getGraphDepartDifferences();
        int[][] arrive = output.getGraphArrivalDifferences();

        XYChart.Series<Number, Number> departSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> arriveSeries = new XYChart.Series<>();

        for (int i = 0; i < depart.length; i++) 
        {
            departSeries.getData().add(new XYChart.Data<>(depart[i][0], depart[i][1]));
            
        }
        for (int i = 0; i < arrive.length; i++) 
        {
            arriveSeries.getData().add(new XYChart.Data<>(arrive[i][0], arrive[i][1]));
            
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
