package com.cs261.app.UI;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;

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
    private javafx.scene.control.TextField timestampField;
    @FXML
    private javafx.scene.control.TextField timestampField1;

    @FXML
    private javafx.scene.control.Button btnSearch;

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
        ObservableList<ArrivalRow> data = Globals.getArrivalsTableDataRange(currentTimeStamp, currentEndStamp);
        arrivalsTable.setItems(data);
        
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
        ObservableList<ArrivalRow> data = Globals.getArrivalsTableDataRange(currentTimeStamp, currentEndStamp );
        arrivalsTable.setItems(data);
    }

    


    @FXML
    public void resetTable()
    {
    ObservableList<ArrivalRow> data = Globals.getArrivalsTableData();
    arrivalsTable.setItems(data);
    }

    

    @FXML
    public void initialize() 
    {
        //arrivalsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        runwayColumn.setCellValueFactory(new PropertyValueFactory<>("runway"));
        callsignColumn.setCellValueFactory(new PropertyValueFactory<>("callsign"));
        
        

        actualColumn.setCellValueFactory(new PropertyValueFactory<>("actual"));
        data = Globals.getArrivalsTableData();
        
        arrivalsTable.setItems(data);

    }
}
