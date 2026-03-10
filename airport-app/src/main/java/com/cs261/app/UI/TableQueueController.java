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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TableQueueController 
{

    int currentTimeStamp = 0;
    int currentEndStamp = 30;

    ObservableList<QueueRow> data;
    //arrivalsTable.setItems(data);

    @FXML
    private TableView<QueueRow> QueueTable;
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
        ObservableList<QueueRow> data = null;
        if(validInput1 && validInput2)
        {
            data = Globals.getQueueTableDataRange(startTimeStamp,endTimeStamp);
            currentEndStamp = endTimeStamp;
            currentTimeStamp = startTimeStamp;
            QueueTable.setItems(data);
            
        }
        else
        {
            if (!validInput1 || !validInput2) {
                Alert alert = new Alert(AlertType.WARNING, "Please enter valid numbers for start and end timestamp.");
                alert.setHeaderText("Invalid input");
                alert.showAndWait();
            }
            data = Globals.getQueueTableData();
            QueueTable.setItems(data);
            currentEndStamp = 30;
            currentTimeStamp = 0;
            
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
        ObservableList<QueueRow> data = Globals.getQueueTableDataRange(currentTimeStamp, currentEndStamp);
        QueueTable.setItems(data);
        
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
        ObservableList<QueueRow> data = Globals.getQueueTableDataRange(currentTimeStamp, currentEndStamp );
        QueueTable.setItems(data);
    }

    


    @FXML
    public void resetTable()
    {
    ObservableList<QueueRow> data = Globals.getQueueTableData();
    QueueTable.setItems(data);
    }

    

    @FXML
    public void initialize() 
    {
        if (popupMenuController != null) {
            popupMenuController.setOnClose(() -> closeDetailsPane(null));
        }
        delayColumn.setCellValueFactory(new PropertyValueFactory<>("delay"));
        callsignColumn.setCellValueFactory(new PropertyValueFactory<>("callsign"));
        scheduledColumn.setCellValueFactory(new PropertyValueFactory<>("entryTime"));
        exitColumn.setCellValueFactory(new PropertyValueFactory<>("waitTime"));
        actualColumn.setCellValueFactory(new PropertyValueFactory<>("actual"));
        data = Globals.getQueueTableData();
        
        QueueTable.setItems(data);

    }



    //Navigation functions.

    
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
