package com.cs261.app.UI;

//import com.cs261.app.controller.ResultsContext;
import com.cs261.app.model.AirCraft;
import com.cs261.app.model.ModelOutput;
import com.cs261.app.UI.Globals;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class TableNavController {

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
    @FXML private ComboBox<String> timestampComboBox;
    @FXML private Button btnSearch;
    @FXML private VBox detailsPane;
    @FXML private Button btnDetailsToggle;
    @FXML private TextField planesSearchField;
    @FXML private TableView<IdDetail> planesTable;
    @FXML private TableColumn<IdDetail, String> planesIdColumn;
    @FXML private TableColumn<IdDetail, String> planesDetailColumn;
    @FXML private TextField runwaysSearchField;
    @FXML private TableView<IdDetail> runwaysTable;
    @FXML private TableColumn<IdDetail, String> runwaysIdColumn;
    @FXML private TableColumn<IdDetail, String> runwaysDetailColumn;
    @FXML private Button btnCloseDetails;


    @FXML
    public void initialize() {
        if (kpiMaxDelayTakeoff != null) {
            updateKpiFromLastOutput();
        }
        setupDetailsPanel();
    }

    private void setupDetailsPanel() {
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
        kpiIncomingCancelled.setText("Number of incoming planes cancelled: " + "—");
        kpiOutgoingDiverted.setText("Number of outgoing planes diverted: " + "—");
        int[] modeCounts = Globals.runwayModes; // ResultsContext.getRunwayModeCounts();
        kpiRunways.setText("Number of Runways: " + (modeCounts[0] + modeCounts[1] + modeCounts[2]) + " (" + modeCounts[0] + " incoming, " + modeCounts[1] + " outgoing, " + modeCounts[2] + " mixed)");
        kpiMaxWaitCancellation.setText("Maximum wait time (for cancellation): " + "— Minutes");
        kpiMaxWaitDiversion.setText("Maximum wait time (for diversion): " + "— Minutes");
        String duration = Globals.duration; // ResultsContext.getDurationText();
        kpiDuration.setText("Duration of simulation: " + (duration.isEmpty() ? "—" : duration));
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
    private void toggleDetailsPane(ActionEvent event) {
        if (detailsPane == null || btnDetailsToggle == null) {
            return;
        }
        boolean show = !detailsPane.isVisible();
        detailsPane.setVisible(show);
        detailsPane.setManaged(show);
        btnDetailsToggle.setText(show ? "▶" : "◀");
        if (show) {
            refreshDetailsTables();
        }
    }

    /** Close the slide-out details panel (same as toggling it closed). */
    @FXML
    private void closeDetailsPane(ActionEvent event) {
        if (detailsPane == null || btnDetailsToggle == null) {
            return;
        }
        detailsPane.setVisible(false);
        detailsPane.setManaged(false);
        btnDetailsToggle.setText("◀");
    }

    /** Populate Planes and Runways tables from last simulation output. */
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
    }

    /**
     * Opens a pop-up dialog allowing the user to look up a runway by ID and see its configuration.
     * Intended to be used from the Runways table page.
     */
    @FXML
    private void openRunwayDetails(ActionEvent event) {
        ModelOutput out = Globals.output;
        if (out == null) {
            Alert alert = new Alert(AlertType.INFORMATION, "No simulation results available yet.");
            alert.setHeaderText("Runway details");
            alert.showAndWait();
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Runway details");
        dialog.setHeaderText("View details for a specific runway");
        dialog.setContentText("Enter runway number (e.g. 09, 27):");
        dialog.getDialogPane().getScene().getWindow().sizeToScene();

        dialog.showAndWait().ifPresent(id -> {
            String[][] runways = out.getTableRunways();
            if (runways == null || runways.length == 0) {
                Alert alert = new Alert(AlertType.INFORMATION, "No runway data available.");
                alert.setHeaderText("Runway details");
                alert.showAndWait();
                return;
            }
            String[] match = null;
            for (String[] row : runways) {
                if (row != null && row.length > 0 && row[0] != null && row[0].equals(id)) {
                    match = row;
                    break;
                }
            }
            if (match == null) {
                Alert alert = new Alert(AlertType.INFORMATION, "Runway \"" + id + "\" was not found in the latest run.");
                alert.setHeaderText("Runway details");
                alert.showAndWait();
                return;
            }

            // Columns described in ModelOutput.runwayTable() Javadoc
            StringBuilder sb = new StringBuilder();
            sb.append("Runway ").append(match[0]).append('\n');
            sb.append("Mode: ").append(match[1]).append('\n');
            sb.append("Status: ").append(match[2]).append('\n');
            sb.append("Has status change: ").append(match[3]).append('\n');

            // Up to three status change blocks (status, start, end) starting at index 4
            for (int j = 4; j + 2 < match.length; j += 3) {
                if (match[j] == null || "N/A".equals(match[j])) continue;
                sb.append("\nChange ").append((j - 4) / 3 + 1).append(":\n");
                sb.append("  New status: ").append(match[j]).append('\n');
                sb.append("  Start: ").append(match[j + 1]).append('\n');
                sb.append("  End: ").append(match[j + 2]).append('\n');
            }

            Alert alert = new Alert(AlertType.INFORMATION, sb.toString());
            alert.setHeaderText("Runway " + match[0] + " details");
            alert.showAndWait();
        });
    }

    /**
     * Opens a pop-up dialog allowing the user to look up an aircraft by callsign and see its details.
     * Intended to be used from the aircraft table pages (Departures, Arrivals, TO, HP).
     */
    @FXML
    private void openAircraftDetails(ActionEvent event) {
        ModelOutput out = Globals.output;
        if (out == null) {
            Alert alert = new Alert(AlertType.INFORMATION, "No simulation results available yet.");
            alert.setHeaderText("Aircraft details");
            alert.showAndWait();
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Aircraft details");
        dialog.setHeaderText("View details for a specific aircraft");
        dialog.setContentText("Enter aircraft callsign:");
        dialog.getDialogPane().getScene().getWindow().sizeToScene();

        dialog.showAndWait().ifPresent(callsign -> {
            AirCraft plane = Globals.GetAirplane(callsign);
            if (plane == null) {
                Alert alert = new Alert(AlertType.INFORMATION, "Aircraft \"" + callsign + "\" was not found in the latest run.");
                alert.setHeaderText("Aircraft details");
                alert.showAndWait();
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Callsign: ").append(plane.getCallSign()).append('\n');
            sb.append("Operator: ").append(plane.getOperator()).append('\n');
            sb.append("Origin: ").append(plane.getOrigin()).append('\n');
            sb.append("Destination: ").append(plane.getDestination()).append('\n');
            sb.append("Scheduled time: ").append(plane.getScheduled()).append('\n');
            sb.append("Actual queue entry time: ").append(plane.getEntryTime()).append('\n');
            sb.append("Exit time: ").append(plane.getExitTime()).append('\n');
            sb.append("Altitude (m): ").append(plane.getAltitude()).append('\n');
            sb.append("Ground speed (kts): ").append(plane.getSpeed()).append('\n');
            sb.append("Fuel remaining (L): ").append(plane.getFuel()).append('\n');
            sb.append("Emergency status: ").append(plane.getEmergencyStatus()).append('\n');
            sb.append("Zone status: ").append(plane.getZoneStatus()).append('\n');
            sb.append("Flight type: ").append(plane.getFlightType()).append('\n');

            Alert alert = new Alert(AlertType.INFORMATION, sb.toString());
            alert.setHeaderText("Aircraft " + plane.getCallSign() + " details");
            alert.showAndWait();
        });
    }
}
