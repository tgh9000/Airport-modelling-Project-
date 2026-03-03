package com.cs261.app.UI;


import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;

public class ArrivalsGraphController 
{
    
    @FXML
    private LineChart<Number, Number> arrivalsGraph;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private void configureYAxis() 
    {

        
        yAxis.setAutoRanging(true);
    }

    private void loadData() {

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("2026 Sales");

        series.getData().add(new XYChart.Data<>(2, 10));
        series.getData().add(new XYChart.Data<>(4, 25));
        series.getData().add(new XYChart.Data<>(5, 18));
        series.getData().add(new XYChart.Data<>(6, 40));

        /*for(int i = 0 ; i < val.size(); i ++ )
        {
            //calculate pair
            // create point pair 
            // add pair to the series
        }*/

        arrivalsGraph.getData().add(series);

        for (XYChart.Data<Number, Number> data : series.getData()) 
        {

            data.nodeProperty().addListener((obs, oldNode, newNode) -> 
            {
                if (newNode != null) {

                    Tooltip tooltip = new Tooltip
                    (
                        "X: " + data.getXValue() + "\nY: " + data.getYValue()
                    );

                    Tooltip.install(newNode, tooltip);
                }
            });

        }
        arrivalsGraph.setCreateSymbols(true);

    }

    @FXML
    public void initialize() {

        configureYAxis();
        loadData();
    }
}
