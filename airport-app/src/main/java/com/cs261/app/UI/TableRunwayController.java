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

    @FXML
    public void searchByTimestamp()
    {
        boolean validInput1 = true;
        boolean validInput2 = true;
        boolean validInput3 = true;
        int startTimeStamp = 0;
        int endTimeStamp = 0;
        int runway = 0;
        try{
            startTimeStamp = Integer.parseInt(timestampField.getText());
            //endTimeStamp = Integer.parseInt(timestampField1.getText());
            
        }
        catch(Exception e)
        {
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
            validInput2 = false;
            //e.printStackTrace();
        }
        try{
            runway = Integer.parseInt(RunwayField.getText());
            //ObservableList<ArrivalRow> data = Globals.getArrivalsTableData(startTimeStamp);
            //arrivalsTable.setItems(data);
        }
        catch(Exception e)
        {
            validInput3 = false;
            //e.printStackTrace();
        }
        System.out.println("start: " + startTimeStamp);
        System.out.println("end: " + endTimeStamp);
        System.out.println("runway: " + runway);
        ObservableList<RunwayRow> data = null;
        if(validInput1 && validInput2 && validInput3)
        {
            data = Globals.getRunwayTableDataRange(startTimeStamp,endTimeStamp, runway);
            currentEndStamp = endTimeStamp;
            currentTimeStamp = startTimeStamp;
            currentRunway = runway;
            RunwayTable.setItems(data);
            
        }
        else
        {
            data = Globals.getRunwayTableDataRange(currentTimeStamp,currentEndStamp, currentRunway);
            RunwayTable.setItems(data);
            currentEndStamp = 30;
            currentTimeStamp = 0;
            currentRunway = 0;
            
        }
    
        

    }

    @FXML
    private Button btnPrev;
    @FXML
    private Button btnNext;

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
