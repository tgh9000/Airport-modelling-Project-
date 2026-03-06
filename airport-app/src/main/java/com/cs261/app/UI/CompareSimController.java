package com.cs261.app.UI;

import java.util.List;

import com.cs261.app.model.HandleOutput;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;

public class CompareSimController {

    @FXML
    public ListView<String> runsListView; 
    public SimpleListProperty<String> runsListProp = new SimpleListProperty<>();
    @FXML
    public VBox comparableView;

    public void init(){
        runsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        // add a change listener which selects the last added string and adds it to accordion
        runsListView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> event) {
                if (event.wasRemoved()){
                    // need to remove tilted pane with same id??
                }   
                if (event.wasAdded()){
                    // need to make new titled pane
                }
            }
            
        });;
    }

    public void loadSimRunsList(){
        List<String> runsList = HandleOutput.listStoredRuns();
        runsListProp.addAll(runsList);
        runsListView.setItems(runsListProp);
    }
}
