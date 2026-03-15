
package com.cs261.app.UI;
import com.cs261.app.model.AirCraft;
import com.cs261.app.model.ModelOutput;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
public class PopupMenuController {
    
    
    private final ObservableList<AirCraftDetail> planesList = FXCollections.observableArrayList();
    private final ObservableList<RunwayDetail> runwaysList = FXCollections.observableArrayList();
    private Runnable onClose;
    @FXML private TabPane tabPane;
    @FXML private TextField planesSearchField;
    @FXML private TableView<AirCraftDetail> planesTable;
    @FXML private TableColumn<AirCraftDetail, String> planesCallsignColumn;
    @FXML private TableColumn<AirCraftDetail, String> planesOperatorColumn;
    @FXML private TableColumn<AirCraftDetail, String> planesOriginColumn;
    @FXML private TableColumn<AirCraftDetail, String> planesDestinationColumn;
    @FXML private TableColumn<AirCraftDetail, String> planesFlightTypeColumn;
    @FXML private TableColumn<AirCraftDetail, String> planesEmergencyStatusColumn;
    @FXML private TableColumn<AirCraftDetail, String> planesEmergencyStartColumn;
    @FXML private TextField runwaysSearchField;
    @FXML private TableView<RunwayDetail> runwaysTable;
    @FXML private TableColumn<RunwayDetail, String> runwaysIdColumn;
    @FXML private TableColumn<RunwayDetail, String> runwaysOperationModeColumn;
    @FXML private TableColumn<RunwayDetail, String> runwaysStatusColumn;
    @FXML private TableColumn<RunwayDetail, String> runwaysHasInterruptionColumn;
    @FXML private TableColumn<RunwayDetail, String> runwaysStatusChangeTo;
    @FXML private TableColumn<RunwayDetail, String> runwaysInterruptionStartColumn;
    @FXML private TableColumn<RunwayDetail, String> runwaysInterruptionEndColumn;
    @FXML private javafx.scene.control.Button btnClose;
    public void setOnClose(Runnable onClose) {
        this.onClose = onClose;
    }
    /**
     * initialises the rows for all the tables and sets up the filtering for the pop up table.
     */
    @FXML
    public void initialize() {
        if (planesCallsignColumn != null) {
            planesCallsignColumn.setCellValueFactory(cdf -> new SimpleStringProperty(cdf.getValue().getCallsign()));
        }
        if (planesOriginColumn != null) {
            planesOriginColumn.setCellValueFactory(cdf -> new SimpleStringProperty(cdf.getValue().getOrigin()));
        }
        if (planesOperatorColumn != null) {
            planesOperatorColumn.setCellValueFactory(cdf -> new SimpleStringProperty(cdf.getValue().getOperator()));
        }
        if (planesDestinationColumn != null) {
            planesDestinationColumn.setCellValueFactory(cdf -> new SimpleStringProperty(cdf.getValue().getDestination()));
        }
        if (planesFlightTypeColumn != null) {
            planesFlightTypeColumn.setCellValueFactory(cdf -> new SimpleStringProperty(cdf.getValue().getFlightType()));
        }
        if (planesEmergencyStatusColumn != null) {
            planesEmergencyStatusColumn.setCellValueFactory(cdf -> new SimpleStringProperty(cdf.getValue().getEmergencyStatus()));
        }
        if (planesEmergencyStartColumn != null) {
            planesEmergencyStartColumn.setCellValueFactory(cdf -> new SimpleStringProperty(cdf.getValue().getEmergencyStart()));
        }




        if (runwaysIdColumn != null) {
            runwaysIdColumn.setCellValueFactory(cdf -> new SimpleStringProperty(cdf.getValue().getId()));
        }
        if (runwaysOperationModeColumn != null) {
            runwaysOperationModeColumn.setCellValueFactory(cdf -> new SimpleStringProperty(cdf.getValue().getOperationalMode()));
        }
        if (runwaysStatusColumn != null) {
            runwaysStatusColumn.setCellValueFactory(cdf -> new SimpleStringProperty(cdf.getValue().getRunwayStatus()));
        }
        if (runwaysHasInterruptionColumn != null) {
            runwaysHasInterruptionColumn.setCellValueFactory(cdf -> new SimpleStringProperty(cdf.getValue().getHasInterruption()));
        }
        if (runwaysStatusChangeTo != null) {
            runwaysStatusChangeTo.setCellValueFactory(cdf -> new SimpleStringProperty(cdf.getValue().getStatusChange()));
        }

        if (runwaysInterruptionStartColumn != null) {
            runwaysInterruptionStartColumn.setCellValueFactory(cdf -> new SimpleStringProperty(cdf.getValue().getInterruptionStart()));
        }
        if (runwaysInterruptionEndColumn != null) {
            runwaysInterruptionEndColumn.setCellValueFactory(cdf -> new SimpleStringProperty(cdf.getValue().getInterruptionEnd()));
        }
        FilteredList<AirCraftDetail> filteredPlanes = new FilteredList<>(planesList, p -> true);
        FilteredList<RunwayDetail> filteredRunways = new FilteredList<>(runwaysList, p -> true);
        if (planesTable != null) {
            planesTable.setItems(filteredPlanes);
            planesTable.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && planesTable.getSelectionModel().getSelectedItem() != null) {
                    openAircraftDetails(planesTable.getSelectionModel().getSelectedItem().getCallsign());
                }
            });
        }
        if (runwaysTable != null) {
            runwaysTable.setItems(filteredRunways);
            runwaysTable.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && runwaysTable.getSelectionModel().getSelectedItem() != null) {
                    openRunwayDetails(runwaysTable.getSelectionModel().getSelectedItem().getId());
                }
            });
        }
        if (planesSearchField != null) {
            planesSearchField.textProperty().addListener((o, oldVal, newVal) -> {
                String q = (newVal == null ? "" : newVal).trim().toLowerCase();
                filteredPlanes.setPredicate(row -> q.isEmpty()
                    || (row.getCallsign() != null && row.getCallsign().toLowerCase().contains(q)));
            });
            planesSearchField.setOnAction(e -> openAircraftDetails(planesSearchField.getText()));
        }
        if (runwaysSearchField != null) {
            runwaysSearchField.textProperty().addListener((o, oldVal, newVal) -> {
                String q = (newVal == null ? "" : newVal).trim().toLowerCase();
                filteredRunways.setPredicate(row -> q.isEmpty()
                    || (row.getId() != null && row.getId().toLowerCase().contains(q)));
            });
            runwaysSearchField.setOnAction(e -> openRunwayDetails(runwaysSearchField.getText()));
        }
    }
    /**
     * adds all values to runway table.
     */
    public void refreshFromLastOutput() 
    {
        planesList.addAll(Globals.airCraftDetails);
        /*
        ModelOutput out = Globals.output;
        planesList.clear();
        runwaysList.clear();
        if (out == null) return;
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
        }*/
        runwaysList.addAll(Globals.runwayDetails) ;
        /**String[][] rwy = out.getTableRunways();
        if (rwy != null) {
            for (String[] row : rwy) {
                if (row != null && row.length >= 3) {
                    String id = row[0];
                    String details = row[1] + ", " + row[2];
                    runwaysList.add(new IdDetail(id, details));
                }
            }
        }*/
    }
    /**
     * closes the pop up menu.
     */
    @FXML
    private void closePopup() {
        if (onClose != null) onClose.run();
    }
    /**
     * opens the details for the aircraft table
     * @param rawCallsign
     */
    private void openAircraftDetails(String rawCallsign) {
        String callsign = rawCallsign == null ? "" : rawCallsign.trim();
        if (callsign.isEmpty()) {
            showInfo("Aircraft details", "Please enter an aircraft callsign.");
            return;
        }
        ModelOutput out = Globals.output;
        if (out == null) {
            showInfo("Aircraft details", "No simulation results available yet.");
            return;
        }
        AirCraft plane = Globals.GetAirplane(callsign);
        if (plane == null) {
            showInfo("Aircraft details", "Aircraft \"" + callsign + "\" was not found in the latest run.");
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
    }
    /**
     * opens the details for the runway table.
     */
    private void openRunwayDetails(String rawId) {
        String id = rawId == null ? "" : rawId.trim();
        if (id.isEmpty()) {
            showInfo("Runway details", "Please enter a runway ID.");
            return;
        }
        ModelOutput out = Globals.output;
        if (out == null) {
            showInfo("Runway details", "No simulation results available yet.");
            return;
        }
        String[][] runways = out.getTableRunways();
        if (runways == null || runways.length == 0) {
            showInfo("Runway details", "No runway data available.");
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
            showInfo("Runway details", "Runway \"" + id + "\" was not found in the latest run.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Runway ").append(match[0]).append('\n');
        if (match.length > 1) sb.append("Mode: ").append(match[1]).append('\n');
        if (match.length > 2) sb.append("Status: ").append(match[2]).append('\n');
        if (match.length > 3) sb.append("Has status change: ").append(match[3]).append('\n');
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
    }
    private void showInfo(String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION, content);
        alert.setHeaderText(header);
        alert.showAndWait();
    }
}

