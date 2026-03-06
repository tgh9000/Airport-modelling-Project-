package com.cs261.app.UI;

import java.util.List;

import com.cs261.app.model.HandleOutput;

import javafx.beans.property.SimpleListProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class CompareSimController {

    @FXML
    public ListView<String> runsListView; 

    public SimpleListProperty<String> runsListProp = new SimpleListProperty<>();

    public void loadSimRunsList(){
        List<String> runsList = HandleOutput.listStoredRuns();
        runsListProp.addAll(runsList);
        runsListView.setItems(runsListProp);
    }
}
