package com.cs261.app.UI;

import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

public class ComparePane extends TitledPane{
    public ComparePane(String id, String[] metrics){
        this.setId(id);
        this.setContent(makeContent(metrics));
    }

    public GridPane makeContent(String[] metrics){
        GridPane contentGrid = new GridPane();
        String[] labels = {"Total Arrivals Generated", "Total Departures Generated", "Total Arrivals Landed", "Total Departures Taken off", "Max Holding Queue Length",
                            "Max Takeoff Queue Length", "Max Arrival Delay", "Max Departure Delay", "Average Arrival Delay", "Average Departure Delay", "Average Holding Wait Time", 
                            "Average Takeoff Queue Wait Time"};

        for (int i = 0; i < metrics.length; i++){
            contentGrid.add(new Label(labels[i]), 0, i, 1, 1);
            contentGrid.add(new Label(metrics[i]), 1, i, 1, 1);
        }
        return contentGrid;
    }
}
