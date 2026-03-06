package com.cs261.app.UI;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private TableColumn<ArrivalRow, String> runwayColumn;

    @FXML
    private TableColumn<ArrivalRow, String> actualColumn;

    

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
