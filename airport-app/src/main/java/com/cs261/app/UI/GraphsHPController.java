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

public class GraphsHPController 
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

        XYChart.Series<Number, Number> series = Globals.hPSeries;
        /**
        series.getData().add(new XYChart.Data<>(2, 10));
        series.getData().add(new XYChart.Data<>(4, 25));
        series.getData().add(new XYChart.Data<>(5, 18));
        series.getData().add(new XYChart.Data<>(6, 40));*/

        arrivalsGraph.setCreateSymbols(true);
        arrivalsGraph.getData().add(series);
        arrivalsGraph.setAnimated(false);
        arrivalsGraph.setLegendVisible(false);
        arrivalsGraph.setPrefSize(600, 400);

        // VERY IMPORTANT
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

                    // Optional visual feedback
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
        // Force tooltips to behave like desktop hover tooltips
        

        configureYAxis();
        //Platform.runLater(this::loadData);
        loadData();
    }


    /**
     * Takes user to the Arrivals graph page when the button is clicked.
     * @param event the event that is triggered when the user clicks the button.
     * @throws IOException exception is thrown if the fxml file cannot be loaded. This could be due to a missing file, incorrect path, or issues with the file's content.
     */
    public void goArrivals(ActionEvent event) throws IOException 
    {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/cs261/app/graphs(arrivals).fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.getScene().setRoot(root);   
    }

    /**
     * Takes the user to the key data page when the button is clicked.
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
     * Takes user to the Holding Pattern graph page when the button is clicked.
     * @param event the event that is triggered when the user clicks the button.
     * @throws IOException exception is thrown if the fxml file cannot be loaded. This could be due to a missing file, incorrect path, or issues with the file's content.
     */
    public void goHP(ActionEvent event) throws IOException 
    {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/cs261/app/graphs(HP).fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.getScene().setRoot(root);   
    }

    /**
     * Takes user to the Takeoff Queue graph page when the button is clicked.
     * @param event the event that is triggered when the user clicks the button.
     * @throws IOException exception is thrown if the fxml file cannot be loaded. This could be due to a missing file, incorrect path, or issues with the file's content.
     */
    public void goTQ(ActionEvent event) throws IOException 
    {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/cs261/app/graphs(TQ).fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.getScene().setRoot(root);   
    }    
    
}
