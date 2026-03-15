package com.cs261.app.UI;

import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

/**
 * Create titled panes for previous simulations iteratively
 */
public class ComparePane extends TitledPane{
    public ComparePane(String id, String[] metrics){
        super(id, null);
        this.setId(id);
        this.setMaxWidth(Double.MAX_VALUE);
        this.setContent(makeContent(metrics));
    }

    /**
     * Factory method to create titled pane content
     * @param metrics metrics of previous simulation
     * @return pane to add to vbox
     */
    public GridPane makeContent(String[] metrics){
        GridPane contentGrid = new GridPane();
        contentGrid.setMaxWidth(Double.MAX_VALUE);
        String[] labels = {"Simulation", "Total Arrivals Generated", "Total Departures Generated", "Total Arrivals Landed", "Total Departures Taken off", "Max Holding Queue Length",
                            "Max Takeoff Queue Length", "Max Arrival Delay", "Max Departure Delay", "Average Arrival Delay", "Average Departure Delay", "Average Holding Wait Time", 
                            "Average Takeoff Queue Wait Time"};
        contentGrid.setStyle("-fx-background-color: white;");
        for (int i = 0; i < metrics.length; i++){
            Label lbl = new Label(labels[i]);
            lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            contentGrid.add(new Label(labels[i]), 0, i, 1, 1);
            contentGrid.add(new Label(metrics[i]), 1, i, 1, 1);
        }
        return contentGrid;
    }
}
