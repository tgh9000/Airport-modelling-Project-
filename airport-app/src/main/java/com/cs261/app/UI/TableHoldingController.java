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

public class TableHoldingController 
{
    int currentTimeStamp = 0;
    int currentEndStamp = 30;
    ObservableList<QueueRow> data;
    //arrivalsTable.setItems(data);
    @FXML
    private TableView<QueueRow> HoldingTable;
    ////column1 = call sign, column2 = scheduled departure/arrival time, column3 = actual departure/arrival time, column4 = delay, column5 = wait time
    @FXML
    private TableColumn<QueueRow, String> callsignColumn;
    @FXML
    private TableColumn<QueueRow, String> scheduledColumn;
    @FXML
    private TableColumn<QueueRow, String> actualColumn;
    @FXML
    private TableColumn<QueueRow, String> delayColumn;
    @FXML
    private TableColumn<QueueRow, String> exitColumn;
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
        boolean emptyInput1 = false;
        boolean validInput2 = true;
        boolean emptyInput2 = false;
        int startTimeStamp = 0;
        int endTimeStamp = 0;

        try
        {
            startTimeStamp = Integer.parseInt(timestampField.getText());
            //endTimeStamp = Integer.parseInt(timestampField1.getText());
            
        }
        catch(Exception e)
        {
            if (timestampField.getText() == null) 
            {
                emptyInput1 = true;
            }
            validInput1 = false;
            //e.printStackTrace();
        }
        try{
            endTimeStamp = Integer.parseInt(timestampField1.getText());
            //ObservableList<ArrivalRow> data = Globals.getArrivalsTableData(startTimeStamp);
            //arrivalsTable.setItems(data);
        }
        catch(Exception e)
        {
            if (timestampField1.getText() == null) 
            {
                emptyInput2 = true;
            }
            validInput2 = false;
            //e.printStackTrace();
        }
        System.out.println("start: " + startTimeStamp);
        System.out.println("end: " + endTimeStamp);
        ObservableList<QueueRow> data = null;
        if(validInput1 && validInput2)
        {
            data = Globals.getHoldingTableDataRange(startTimeStamp,endTimeStamp);
            currentEndStamp = endTimeStamp;
            currentTimeStamp = startTimeStamp;
            HoldingTable.setItems(data);
            
        }
        else if(emptyInput1 && emptyInput2)
        {
            data = Globals.getHoldingTableData();
            HoldingTable.setItems(data);
            currentEndStamp = 30;
            currentTimeStamp = 0;
            
        }
        else
        {
            if(!validInput1)
            {
                Alert alert = new Alert(AlertType.WARNING, "Please enter valid numbers for start and end timestamp.");
                alert.setHeaderText("Please enter a valid number for the start timestamp");
                alert.showAndWait();
                
            }
            else
            {
                Alert alert = new Alert(AlertType.WARNING, "Please enter valid numbers for start and end timestamp.");
                alert.setHeaderText("Please enter a valid number for the Runway");
                alert.showAndWait();
                
            }
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
        ObservableList<QueueRow> data = Globals.getHoldingTableDataRange(currentTimeStamp, currentEndStamp);
        HoldingTable.setItems(data);
        
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
        ObservableList<QueueRow> data = Globals.getHoldingTableDataRange(currentTimeStamp, currentEndStamp );
        HoldingTable.setItems(data);
    }

    

    /**
     * Prints all the data the table can hold
     */
    @FXML
    public void resetTable()
    {
        ObservableList<QueueRow> data = Globals.getHoldingTableData();
        HoldingTable.setItems(data);
    }

    
    /**
     * Function that is run when the page is loaded and correctly sets up the table.
     */
    @FXML
    public void initialize() 
    {
        errorMessage("");
        delayColumn.setCellValueFactory(new PropertyValueFactory<>("delay"));
        callsignColumn.setCellValueFactory(new PropertyValueFactory<>("callsign"));
        scheduledColumn.setCellValueFactory(new PropertyValueFactory<>("entryTime"));
        exitColumn.setCellValueFactory(new PropertyValueFactory<>("waitTime"));
        actualColumn.setCellValueFactory(new PropertyValueFactory<>("actual"));
        data = Globals.getHoldingTableData();
        
        HoldingTable.setItems(data);

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
     * Function to send the user back to the key data page
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
     * Function to send the user to the Arrivals table page
     */
    @FXML
    public void goArrival(ActionEvent event) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/cs261/app/tables(Arrivals).fxml"));
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
