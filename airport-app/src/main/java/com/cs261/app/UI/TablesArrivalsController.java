package com.cs261.app.UI;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TablesArrivalsController 
{
    int currentTimeStamp = 0;
    int currentEndStamp = 30;

    ObservableList<ArrivalRow> data;
    //arrivalsTable.setItems(data);

    @FXML
    private TableView<ArrivalRow> arrivalsTable;

    @FXML
    private TableColumn<ArrivalRow, String> callsignColumn;

    @FXML
    private TableColumn<ArrivalRow, String> runwayColumn;

    @FXML
    private TableColumn<ArrivalRow, String> actualColumn;
    @FXML
    private TableColumn<ArrivalRow, String> scheduledColumn;
    @FXML
    private TableColumn<ArrivalRow, String> exitColumn;

    @FXML
    private javafx.scene.control.TextField timestampField;
    @FXML
    private javafx.scene.control.TextField timestampField1;

    @FXML
    private javafx.scene.control.Button btnSearch;

    @FXML
    private VBox detailsPane;
    @FXML
    private Button btnDetailsToggle;
    @FXML
    private PopupMenuController popupMenuController;


    /**
     * function that runs when user presses the Search Button and searches based on the values put in the input
     */
    @FXML
    public void searchByTimestamp()
    {
        boolean validInput1 = true;
        boolean validInput2 = true;
        int startTimeStamp = 0;
        int endTimeStamp = 0;
        try{
            startTimeStamp = Integer.parseInt(timestampField.getText());
            //endTimeStamp = Integer.parseInt(timestampField1.getText());
            
        }
        catch(Exception e)
        {
            validInput1 = false;
            e.printStackTrace();
        }
        try{
            endTimeStamp = Integer.parseInt(timestampField1.getText());
            //ObservableList<ArrivalRow> data = Globals.getArrivalsTableData(startTimeStamp);
            //arrivalsTable.setItems(data);
        }
        catch(Exception e)
        {
            validInput2 = false;
            e.printStackTrace();
        }
        System.out.println("start: " + startTimeStamp);
        System.out.println("end: " + endTimeStamp);
        ObservableList<ArrivalRow> data = null;
        if(validInput1 && validInput2)
        {
            data = Globals.getArrivalsTableDataRange(startTimeStamp,endTimeStamp);
            currentEndStamp = endTimeStamp;
            currentTimeStamp = startTimeStamp;
            arrivalsTable.setItems(data);
            
        }
        else
        {
            data = Globals.getArrivalsTableData();
            arrivalsTable.setItems(data);
            currentEndStamp = 30;
            currentTimeStamp = 0;
            
        }
        

    }


    @FXML
    private void toggleDetailsPane(ActionEvent event) {
        if (detailsPane == null || btnDetailsToggle == null) return;
        boolean show = !detailsPane.isVisible();
        detailsPane.setVisible(show);
        detailsPane.setManaged(show);
        btnDetailsToggle.setText(show ? "▶" : "◀");
        if (show && popupMenuController != null) {
            if (Globals.output == null) {
                Alert alert = new Alert(AlertType.INFORMATION, "No simulation results available yet. Run a simulation first.");
                alert.setHeaderText("No data");
                alert.showAndWait();
                detailsPane.setVisible(false);
                detailsPane.setManaged(false);
                btnDetailsToggle.setText("◀");
                return;
            }
            popupMenuController.refreshFromLastOutput();
        }
    }

    @FXML
    private void closeDetailsPane(ActionEvent event) {
        if (detailsPane == null || btnDetailsToggle == null) return;
        detailsPane.setVisible(false);
        detailsPane.setManaged(false);
        btnDetailsToggle.setText("◀");
    }




    @FXML
    private Button btnPrev;
    @FXML
    private Button btnNext;


    /**
     * Given the current value of the timestamp values will cycle through the next 30 ticks and searches the tree for the corresponding values.
     */
    @FXML
    public void next()
    {
        int max  =  Integer.parseInt(Globals.totalTicks);
        if(currentEndStamp + 30 >= max)
        {
            currentEndStamp = max - 30;
            currentTimeStamp = currentEndStamp - 30;
        }
        currentTimeStamp += 30;
        currentEndStamp += 30;
        ObservableList<ArrivalRow> data = Globals.getArrivalsTableDataRange(currentTimeStamp, currentEndStamp);
        arrivalsTable.setItems(data);
        
    }


    /**
     * Given the current value of the timestamp values will cycle through the previous 30 ticks and searches the tree for the corresponding values.
     */
    @FXML
    public void prev()
    {
        if (currentTimeStamp <= 30)   
        {
            currentTimeStamp = 30;
            currentEndStamp = 60;
        }
        currentTimeStamp -= 30;
        currentEndStamp -= 30;
        ObservableList<ArrivalRow> data = Globals.getArrivalsTableDataRange(currentTimeStamp, currentEndStamp );
        arrivalsTable.setItems(data);
    }

    

    /**
     * Prints all the data the table can hold
     */
    @FXML
    public void resetTable()
    {
    ObservableList<ArrivalRow> data = Globals.getArrivalsTableData();
    arrivalsTable.setItems(data);
    }

    
    /**
     * Function that is run when the page is loaded and correctly sets up the table.
     */
    @FXML
    public void initialize() 
    {
        if (popupMenuController != null) 
        {
            popupMenuController.setOnClose(() -> closeDetailsPane(null));
        }
        errorMessage("");
        //arrivalsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        runwayColumn.setCellValueFactory(new PropertyValueFactory<>("runway"));
        callsignColumn.setCellValueFactory(new PropertyValueFactory<>("callsign"));
        scheduledColumn.setCellValueFactory(new PropertyValueFactory<>("entryTime"));
        exitColumn.setCellValueFactory(new PropertyValueFactory<>("exitTime"));
        actualColumn.setCellValueFactory(new PropertyValueFactory<>("actual"));
        data = Globals.getArrivalsTableData();
        
        arrivalsTable.setItems(data);

    }


    @FXML
    private Label errorMessageLabel;
    /**
     * displays an error message to the user in the errorMessageLabel Label control.
     * @param message the error message to be displayed to the user
     */
    @FXML
    private void errorMessage(String message)
    {
        errorMessageLabel.setText(message);
    }


    //Navigation functions.
    @FXML
    private Button btnHome;
    @FXML
    private Button btnDepartures;
    @FXML
    private Button btnQueue;
    @FXML
    private Button btnHoldings;
    @FXML
    private Button btnRunways;
    /**
     * Function to send the user back to the key data page
     */
    @FXML
    public void goHome(ActionEvent event) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/cs261/app/keyData.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.getScene().setRoot(root);  
    }


    /**
     * Function to send the user to the deprature table page
     */
    @FXML
    public void goDeparture(ActionEvent event) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/cs261/app/tables(Departures).fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.getScene().setRoot(root);  
    }

    /**
     * Function to send the user to the take off queue page
     */
    @FXML
    public void goQueue(ActionEvent event) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/cs261/app/tables(TO).fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.getScene().setRoot(root);  
    }

    /**
     * Function to send the user to the holding pattern queue page
     */
    @FXML
    public void goHolding(ActionEvent event) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/cs261/app/tables(HP).fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.getScene().setRoot(root);  
    }
    /**
     * Function to send the user to the Runway Table page.
     */
    @FXML
    public void goRunway(ActionEvent event) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/cs261/app/tables(Runway).fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.getScene().setRoot(root);  
    }
}
