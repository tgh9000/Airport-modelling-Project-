package com.cs261.app.UI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cs261.app.model.HandleOutput;

import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CompareSimController {

    @FXML
    public ListView<String> runsListView;
    public ObservableList<String> list = FXCollections.observableArrayList(); 
    public SimpleListProperty<String> runsListProp = new SimpleListProperty<>(list);
    @FXML
    public VBox comparableView;
    @FXML
    public HBox compareHbox;
    @FXML
    public SplitPane splitPaneCompare;
    @FXML
    public ScrollPane compareScroll;

    public void init(){
        runsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        comparableView.setFillWidth(true);
        HBox.setHgrow(splitPaneCompare, Priority.ALWAYS);
        SplitPane.setResizableWithParent(compareScroll, true);
        // add a change listener which selects the last added string and adds it to vbox
        runsListView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> event) {
                while (event.next()){
                    if (event.wasRemoved()){
                        List<String> removed = new ArrayList<>(event.getRemoved());
                        Iterator<Node> children = comparableView.getChildren().iterator();
                        while (children.hasNext()){
                            Node child = children.next();
                            if (removed.contains(child.getId())){
                                children.remove();
                            }
                        }
                    }   
                    if (event.wasAdded()){
                        // focus on it
                        List<String> added = new ArrayList<>(event.getAddedSubList());
                        List<String> ids = comparableView.getChildren().stream().map(Node::getId).toList();
                        Iterator<String> items = added.iterator();

                        while (items.hasNext()){
                            String curr = items.next();
                            if (!ids.contains(curr)){
                                String[] metrics = HandleOutput.loadMetrics(curr);
                                ComparePane newPane = new ComparePane(curr, metrics);
                                comparableView.getChildren().add(newPane);
                            }
                        }
                    }
                }
            }
        });;
        loadSimRunsList();
    }

    public void loadSimRunsList(){
        List<String> runsList = HandleOutput.listStoredRuns();
        //System.out.println(runsList.toString());
        runsListProp.get().addAll(runsList);
        runsListView.setItems(runsListProp);
    }

    @FXML
    public void goToInput(ActionEvent event) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/cs261/app/inputForm.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.getScene().setRoot(root);  
    }

    @FXML
    public void goToLanding(ActionEvent event) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/cs261/app/start.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.getScene().setRoot(root);  
    }
}
