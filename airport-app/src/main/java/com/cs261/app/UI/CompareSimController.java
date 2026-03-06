package com.cs261.app.UI;

import java.util.ArrayList;
import java.util.List;

import com.cs261.app.model.HandleOutput;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
        // add a change listener which selects the last added string and adds it to vbox
        runsListView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> event) {
                if (event.wasRemoved()){
                    List<String> removed = new ArrayList<>(event.getRemoved());
                    ObservableList<Node> children = comparableView.getChildren();
                    for (int i = 0; i < children.size(); i++){
                        if (removed.contains(children.get(i).getId())){
                            children.remove(i);
                        }
                    }
                }   
                if (event.wasAdded()){
                    String last = runsListView.getSelectionModel().getSelectedItem();
                    String[] metrics = HandleOutput.loadMetrics(last);
                    ComparePane newPane = new ComparePane(last, metrics);
                    comparableView.getChildren().add(newPane);
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
