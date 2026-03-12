package com.cs261.app.UI;
import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.Label;
public class TableRunwayController 
{
    int currentTimeStamp = 0;
    int currentEndStamp = 30;
    int currentRunway = 0;

    ObservableList<RunwayRow> data;
    //arrivalsTable.setItems(data);

    @FXML
    private TableView<RunwayRow> RunwayTable;

    @FXML
    private TableColumn<RunwayRow, String> callsignColumn;

    @FXML
    private TableColumn<RunwayRow, String> typeColumn;

    @FXML
    private TableColumn<RunwayRow, String> actualColumn;
    @FXML
    private TableColumn<RunwayRow, String> scheduledColumn;
    @FXML
    private TableColumn<RunwayRow, String> exitColumn;

    @FXML
    private TableColumn<RunwayRow, String> runwayColumn;


    @FXML
    private javafx.scene.control.TextField timestampField;
    @FXML
    private javafx.scene.control.TextField timestampField1;
    @FXML
    private javafx.scene.control.TextField RunwayField;

    @FXML
    private javafx.scene.control.Button btnSearch;

    /**
     * function that runs when user presses the Search Button and searches based on the values put in the input
     */
    @FXML
    public void searchByTimestamp()
    {
        errorMessage("");
        boolean validInput1 = true;
        boolean emptyInput1 = false;
        boolean validInput2 = true;
        boolean emptyInput2 = false;
        boolean validInput3 = true;
        boolean emptyInput3 = false;
        int startTimeStamp = 0;
        int endTimeStamp = 0;
        int runway = 0;
        try
        {
            if(timestampField.getText().equals(""))
            {
                emptyInput1 = true;
            }
            startTimeStamp = Integer.parseInt(timestampField.getText());  
        }
        catch(Exception e)
        {
            validInput1 = false;
        }
        try
        {
            if(timestampField1.getText().equals(""))
            {
                emptyInput2 = true;
            }
            endTimeStamp = Integer.parseInt(timestampField1.getText());
        }
        catch(Exception e)
        {
            validInput2 = false;
            //e.printStackTrace();
        }
        try{
            if(RunwayField.getText().equals(""))
            {
                emptyInput3 = true;
            }
            runway = Integer.parseInt(RunwayField.getText());

        }
        catch(Exception e)
        {
            validInput3 = false;
        }

        ObservableList<RunwayRow> data = null;
        if(validInput1 && validInput2 && validInput3)
        {
            data = Globals.getRunwayTableDataRange(startTimeStamp,endTimeStamp, runway);
            currentEndStamp = endTimeStamp;
            currentTimeStamp = startTimeStamp;
            currentRunway = runway;
            RunwayTable.setItems(data);
            
        }
        else if(emptyInput1 && emptyInput2 && validInput3)// the user just wants to see the arrivals and departures for a specific runway
        {
            data = Globals.getSingleRunwayTableData( runway);
            currentEndStamp = 30;
            currentTimeStamp = 0;
            RunwayTable.setItems(data);

        }
        else if((emptyInput1 && emptyInput2 && emptyInput3))// just put every value into a list.
        {
            data = Globals.getRunwayTableData();
            RunwayTable.setItems(data);
            currentEndStamp = 30;
            currentTimeStamp = 0;
            currentRunway = 0;
            
        }
        else // an error has occured and an error message is Displayed.
        {
            if(!validInput1)
            {
                errorMessage("Please enter a valid number for the start timestamp");
                return;
            }
            if(!validInput2)
            {
                errorMessage("Please enter a valid number for the end timestamp");
                return;
            }
            if (!validInput3)
            {
                errorMessage("Please enter a valid number for the Runway");
                return;
            }
        }

    
        

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
        ObservableList<RunwayRow> data = Globals.getRunwayTableDataRange(currentTimeStamp, currentEndStamp, currentRunway);
        RunwayTable.setItems(data);
        
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
        ObservableList<RunwayRow> data = Globals.getRunwayTableDataRange(currentTimeStamp, currentEndStamp, currentRunway);
        RunwayTable.setItems(data);
    }

    

    /**
     * resets the data in table
     */
    @FXML
    public void resetTable()
    {
        ObservableList<RunwayRow> data = Globals.getRunwayTableData();
        RunwayTable.setItems(data);
    }

    

    @FXML
    public void initialize() 
    {
        //arrivalsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        errorMessage("");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        callsignColumn.setCellValueFactory(new PropertyValueFactory<>("callsign"));
        scheduledColumn.setCellValueFactory(new PropertyValueFactory<>("entryTime"));
        exitColumn.setCellValueFactory(new PropertyValueFactory<>("exitTime"));
        actualColumn.setCellValueFactory(new PropertyValueFactory<>("actual"));
        runwayColumn.setCellValueFactory(new PropertyValueFactory<>("runway"));
        data = Globals.getRunwayTableData();
        
        RunwayTable.setItems(data);

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
    private Button btnArrivals;
    
    @FXML
    public void goHome(ActionEvent event) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/cs261/app/keyData.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.getScene().setRoot(root);  
    }



    @FXML
    public void goDeparture(ActionEvent event) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/cs261/app/tables(Departures).fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.getScene().setRoot(root);  
    }

    @FXML
    public void goArrival(ActionEvent event) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/cs261/app/tables(Arrivals).fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.getScene().setRoot(root); 
    }


    @FXML
    public void goQueue(ActionEvent event) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/cs261/app/tables(TO).fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.getScene().setRoot(root);  
    }


    @FXML
    public void goHolding(ActionEvent event) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/cs261/app/tables(HP).fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.getScene().setRoot(root);  
    }

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
