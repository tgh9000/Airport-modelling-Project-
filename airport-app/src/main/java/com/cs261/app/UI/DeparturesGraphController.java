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

public class DeparturesGraphController 
{
     @FXML
    private LineChart<Number, Number> arrivalsGraph;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private void configureYAxis() 
    {

        
        yAxis.setAutoRanging(true);
    }

    private void loadData() {

        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        series.getData().add(new XYChart.Data<>(2, 10));
        series.getData().add(new XYChart.Data<>(4, 25));
        series.getData().add(new XYChart.Data<>(5, 18));
        series.getData().add(new XYChart.Data<>(6, 40));

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






    @FXML
    public void initialize() {
        // Force tooltips to behave like desktop hover tooltips
        

        configureYAxis();
        //Platform.runLater(this::loadData);
        loadData();
    }



    public void goArrivals(ActionEvent event) throws IOException 
    {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/cs261/app/graphs(arrivals).fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.getScene().setRoot(root);   
    }
    
}
