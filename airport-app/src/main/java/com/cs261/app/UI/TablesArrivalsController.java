package com.cs261.app.UI;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;

public class TablesArrivalsController 
{
    int currentTimeStamp = 0;

    ObservableList<ArrivalRow> data;
    //arrivalsTable.setItems(data);

    @FXML
    private TableView<ArrivalRow> arrivalsTable;

    @FXML
    private TableColumn<ArrivalRow, String> callsignColumn;

    @FXML
    private TableColumn<ArrivalRow, String> scheduledColumn;

    @FXML
    private TableColumn<ArrivalRow, String> actualColumn;

    @FXML
    private TableColumn<ArrivalRow, String> delayColumn;

    @FXML
    private TableColumn<ArrivalRow, String> waitColumn;
    

    @FXML
    public void initialize() 
    {

        callsignColumn.setCellValueFactory(new PropertyValueFactory<>("callsign"));
        scheduledColumn.setCellValueFactory(new PropertyValueFactory<>("scheduled"));
        actualColumn.setCellValueFactory(new PropertyValueFactory<>("actual"));
        delayColumn.setCellValueFactory(new PropertyValueFactory<>("delay"));
        waitColumn.setCellValueFactory(new PropertyValueFactory<>("wait"));
        data = Globals.getArrivalsTableData(currentTimeStamp);
        
        arrivalsTable.setItems(data);

    }
}
