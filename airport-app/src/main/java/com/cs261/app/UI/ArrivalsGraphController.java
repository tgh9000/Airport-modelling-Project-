package com.cs261.app.UI;


import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.util.Duration;


public class ArrivalsGraphController 
{
    
    @FXML
    private LineChart<Number, Number> arrivalsGraph;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    /**
     * Configures the Y-axis to automatically adjust its range based on the data. This ensures that all data points are visible and the graph is appropriately scaled, regardless of the values being plotted.
     */
    private void configureYAxis() 
    {
        yAxis.setAutoRanging(true);
    }

    /**
     * Loads sample data into the line chart. It creates a series of (x, y) data points and adds them to the chart. The method also sets up tooltips for each data point, allowing users to see the exact coordinates when hovering over them. Additionally, it provides visual feedback by scaling the data points on mouse hover.
     */
    private void loadData() {

        XYChart.Series<Number, Number> series =Globals.arriveSeries;


        arrivalsGraph.setCreateSymbols(true);
        arrivalsGraph.getData().add(series);
        arrivalsGraph.setAnimated(false);
        arrivalsGraph.setLegendVisible(false);
        arrivalsGraph.setPrefSize(600, 400);

        // adds the hover box to see the exact coordinates of each point
        Platform.runLater(() -> {
            for (XYChart.Data<Number, Number> data : series.getData()) {

                Node node = data.getNode();

                if (node != null) {

                    Tooltip tooltip = new Tooltip(
                            "X: " + data.getXValue() +
                            "\nY: " + data.getYValue()
                    );
                    tooltip.setStyle(
                            "-fx-background-color: white;" +
                            "-fx-text-fill: black;" +
                            "-fx-font-size: 13px;" +
                            "-fx-border-color: black;"
                    );
                    tooltip.setShowDelay(Duration.millis(100));
                    tooltip.setShowDuration(Duration.seconds(10));
                    tooltip.setHideDelay(Duration.millis(200));

                    Tooltip.install(node, tooltip);

                    // makes a small visual change to the node when hovered over to make it more obvious
                    node.setOnMouseEntered(e -> node.setScaleX(1.5));
                    node.setOnMouseExited(e -> node.setScaleX(1.0));
                }
            }
        });

        
        

    }





    /**
     * Funcation that is called when the page is loaded.
     */
    @FXML
    public void initialize() {
       
        

        configureYAxis();
        loadData();
    }



    // navigation methods

    /**
     * Takes user to the Departures graph page when the button is clicked.
     * @param event the event that is triggered when the user clicks the button.
     * @throws IOException exception is thrown if the fxml file cannot be loaded. This could be due to a missing file, incorrect path, or issues with the file's content.
     */
    public void goDepartures(ActionEvent event) throws IOException 
    {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/cs261/app/graphs(departures).fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.getScene().setRoot(root);   
    }

    /**
     * 
     * @param event the event that is triggered when the user clicks the button.
     * @throws IOException exception is thrown if the fxml file cannot be loaded. This could be due to a missing file, incorrect path, or issues with the file's content.
     */
    public void goHome(ActionEvent event) throws IOException 
    {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/cs261/app/keyData.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.getScene().setRoot(root);   
    }
}
