package com.cs261.app.UI;

import java.util.Optional;

//import com.cs261.app.controller.ResultsContext;
import com.cs261.app.model.AirCraft;
import com.cs261.app.model.HandleOutput;
import com.cs261.app.model.ModelOutput;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;



public class TableNavController 
{




    /** Simple row for popup tables: id (callsign/runway id) and details line. */
    private static class IdDetail {
        private final String id;
        private final String details;
        IdDetail(String id, String details) {
            this.id = id;
            this.details = details;
        }
        String getId() { return id; }
        String getDetails() { return details; }
    }

    private final ObservableList<IdDetail> planesList = FXCollections.observableArrayList();
    private final ObservableList<IdDetail> runwaysList = FXCollections.observableArrayList();

    @FXML private Label kpiMaxDelayTakeoff;
    @FXML private Label kpiAvgDelayTakeoff;
    @FXML private Label kpiMaxDelayArrival;
    @FXML private Label kpiAvgDelayArrival;
    @FXML private Label kpiIncomingCancelled;
    @FXML private Label kpiOutgoingDiverted;
    @FXML private Label kpiRunways;
    @FXML private Label kpiMaxWaitCancellation;
    @FXML private Label kpiMaxWaitDiversion;
    @FXML private Label kpiDuration;
    @FXML private Label kpiAvgWaitTakeOff;
    @FXML private Label kpiAvgWaitArrival;
    @FXML private Label kpiHPLength;
    @FXML private Label kpiTQLength;
    @FXML private ComboBox<String> timestampComboBox;
    @FXML private Button btnSearch;
    @FXML private VBox detailsPane;
    //@FXML private Button btnDetailsToggle;
    @FXML private TextField planesSearchField;
    @FXML private TableView<IdDetail> planesTable;
    @FXML private TableColumn<IdDetail, String> planesIdColumn;
    @FXML private TableColumn<IdDetail, String> planesDetailColumn;
    @FXML private TextField runwaysSearchField;
    @FXML private TableView<IdDetail> runwaysTable;
    @FXML private TableColumn<IdDetail, String> runwaysIdColumn;
    @FXML private TableColumn<IdDetail, String> runwaysDetailColumn;
    //@FXML private Button btnCloseDetails;


    @FXML
    public void initialize() {
        if (kpiMaxDelayTakeoff != null) {
            updateKpiFromLastOutput();
        }
        //setupDetailsPanel();
    }


    /**
     * 
     */
    private void setupDetailsPanel() { 
        Tooltip detailsTip = new Tooltip("Show airplane scheudle and runway configurations");
        detailsTip.setShowDelay(Duration.millis(300));
        //btnDetailsToggle.setTooltip(detailsTip);
        
        if (planesIdColumn != null) {
            planesIdColumn.setCellValueFactory(cdf -> new SimpleStringProperty(cdf.getValue().getId()));
        }
        if (planesDetailColumn != null) {
            planesDetailColumn.setCellValueFactory(cdf -> new SimpleStringProperty(cdf.getValue().getDetails()));
        }
        if (runwaysIdColumn != null) {
            runwaysIdColumn.setCellValueFactory(cdf -> new SimpleStringProperty(cdf.getValue().getId()));
        }
        if (runwaysDetailColumn != null) {
            runwaysDetailColumn.setCellValueFactory(cdf -> new SimpleStringProperty(cdf.getValue().getDetails()));
        }
        FilteredList<IdDetail> filteredPlanes = new FilteredList<>(planesList, p -> true);
        FilteredList<IdDetail> filteredRunways = new FilteredList<>(runwaysList, p -> true);
        if (planesTable != null) {
            planesTable.setItems(filteredPlanes);
        }
        if (runwaysTable != null) {
            runwaysTable.setItems(filteredRunways);
        }
        if (planesSearchField != null) {
            planesSearchField.textProperty().addListener((o, oldVal, newVal) -> {
                String q = (newVal == null ? "" : newVal).trim().toLowerCase();
                filteredPlanes.setPredicate(row -> q.isEmpty()
                    || (row.getId() != null && row.getId().toLowerCase().contains(q))
                    || (row.getDetails() != null && row.getDetails().toLowerCase().contains(q)));
            });
        }
        if (runwaysSearchField != null) {
            runwaysSearchField.textProperty().addListener((o, oldVal, newVal) -> {
                String q = (newVal == null ? "" : newVal).trim().toLowerCase();
                filteredRunways.setPredicate(row -> q.isEmpty()
                    || (row.getId() != null && row.getId().toLowerCase().contains(q))
                    || (row.getDetails() != null && row.getDetails().toLowerCase().contains(q)));
            });
        }
    }

    private void updateKpiFromLastOutput() {
        //ModelOutput out = ResultsContext.getLastOutput();
        ModelOutput out = Globals.output;

        if (Globals.output == null) {
            return;
        }
        kpiMaxDelayTakeoff.setText("Maximum delay for take-off: " + out.getMaxDepartDelay() + " Minutes");
        kpiAvgDelayTakeoff.setText("Average delay for take off: " + String.format("%.1f", out.getAvgDepartDelay()) + " Minutes");
        kpiMaxDelayArrival.setText("Maximum delay for arrival: " + out.getMaxArriveDelay() + " Minutes");
        kpiAvgDelayArrival.setText("Average delay for arrival: " + String.format("%.1f", out.getAvgArriveDelay()) + " Minutes");
        kpiIncomingCancelled.setText("Number of outgoing planes cancelled: " + out.getNumCancels());
        kpiOutgoingDiverted.setText("Number of incoming planes diverted: " + out.getNumDiversions());
        int[] modeCounts = Globals.runwayModes; // ResultsContext.getRunwayModeCounts();
        kpiRunways.setText("Number of Runways: " + (modeCounts[0] + modeCounts[1] + modeCounts[2]) + " (" + modeCounts[0] + " incoming, " + modeCounts[1] + " outgoing, " + modeCounts[2] + " mixed)");
        kpiMaxWaitCancellation.setText("Maximum wait time for a cancelled plane: " + out.getMaxCancelWait() + " Minutes");
        kpiMaxWaitDiversion.setText("Maximum wait time for a diverted plane: " + out.getMaxDiversionWait() + " Minutes");
        String duration = Globals.duration; // ResultsContext.getDurationText();
        kpiDuration.setText("Duration of simulation: " + (duration.isEmpty() ? "—" : duration));
        kpiAvgWaitTakeOff.setText("Average take off wait time: " + out.getAverageTakeOffWaitTime() + "Minutes");
        kpiAvgWaitArrival.setText("Average holding queue wait time: " + out.getAverageTakeOffWaitTime() + " Minutes");
        kpiHPLength.setText("Maximum holding queue length: " + out.getMaxHoldingQueueLength());
        kpiTQLength.setText("Maximum take-off queue length: " + out.getMaxTakeOffQueueLength());
    }

    @FXML
    private void onSearch(ActionEvent event) {
        if (timestampComboBox != null) {
            timestampComboBox.show();
        }
    }


    private void loadPage(ActionEvent event, String fxml) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // make a page that asks the user if they want to close the page and save their run.


    @FXML
    private void goHome(ActionEvent event) {
        loadPage(event, "/com/cs261/app/kpiHome.fxml");
    }

    @FXML
    private void goToDepartures(ActionEvent event) {
        loadPage(event, "/com/cs261/app/tables(Departures).fxml");
    }


    @FXML
    private void goToArrivals(ActionEvent event) {
        loadPage(event, "/com/cs261/app/tables(Arrivals).fxml");
    }


    @FXML
    private void goToQueue(ActionEvent event) {
        loadPage(event, "/com/cs261/app/tables(TO).fxml");
    }

    @FXML
    private void goToHolding(ActionEvent event) {
        loadPage(event, "/com/cs261/app/tables(HP).fxml");
    }

    @FXML
    private void goToRunways(ActionEvent event) {
        loadPage(event, "/com/cs261/app/tables(Runway).fxml");
    }


    @FXML
    private void goToGraphs(ActionEvent event) {
        loadPage(event, "/com/cs261/app/graphs(arrivals).fxml");
    }

    @FXML 
    private void gotoLanding(ActionEvent event){
        Alert saveAlert = new Alert(AlertType.NONE, "Do you want to save the simulation statistics before exitting?");
        saveAlert.getButtonTypes().add(ButtonType.YES);
        saveAlert.getButtonTypes().add(ButtonType.NO);

        saveAlert.showAndWait().ifPresent( response -> {
            if (response == ButtonType.YES){
                HandleOutput.storeMetrics(Globals.output);
            } 
            loadPage(event, "/com/cs261/app/start.fxml");
        }
        );
    }


    /*
    @FXML
    private void toggleDetailsPane(ActionEvent event) {
        if (detailsPane == null || btnDetailsToggle == null) {
            return;
        }
        boolean show = !detailsPane.isVisible();
        detailsPane.setVisible(show);
        detailsPane.setManaged(show);
        btnDetailsToggle.setText(show ? "▶" : "◀");
        /*if (show) {
            refreshDetailsTables();
        }
    } */

    /** Close the slide-out details panel (same as toggling it closed). */
    
    /* 
    @FXML
    private void closeDetailsPane(ActionEvent event) {
        if (detailsPane == null || btnDetailsToggle == null) {
            return;
        }
        detailsPane.setVisible(false);
        detailsPane.setManaged(false);
        btnDetailsToggle.setText("◀");
    }
    */

    /** Populate Planes and Runways tables from last simulation output. */
    /* 
    private void refreshDetailsTables() {
        ModelOutput out = Globals.output;
        planesList.clear();
        runwaysList.clear();
        if (out == null) {
            return;
        }
        String[][] deps = out.getTableAllDepartures();
        if (deps != null) {
            for (String[] row : deps) {
                if (row != null && row.length >= 3) {
                    String callsign = row[1];
                    String details = row[0] + " • " + row[2];
                    planesList.add(new IdDetail(callsign, details));
                }
            }
        }
        String[][] arrs = out.getTableAllArrivals();
        if (arrs != null) {
            for (String[] row : arrs) {
                if (row != null && row.length >= 3) {
                    String callsign = row[1];
                    String details = row[0] + " • " + row[2];
                    planesList.add(new IdDetail(callsign, details));
                }
            }
        }
        String[][] rwy = out.getTableRunways();
        if (rwy != null) {
            for (String[] row : rwy) {
                if (row != null && row.length >= 3) {
                    String id = row[0];
                    String details = row[1] + ", " + row[2];
                    runwaysList.add(new IdDetail(id, details));
                }
            }
        }
    }*/

    
}
