package com.cs261.app.UI;

import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Navigation 
{
    @FXML
    public void goBack(ActionEvent event)
    {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/cs261/app/start.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Could not load start screen: " + e.getMessage());
        }
    }   

    @FXML
    public void goToSecondPage(ActionEvent event)
    {
        try {
            try {
                Font.loadFont(getClass().getResourceAsStream("/com/cs261/fonts/Inter/Inter-VariableFont_opsz,wght.ttf"), 12);
            } catch (Exception e) {
                // Font optional
            }
            URL url = getClass().getResource("/com/cs261/app/inputForm.fxml");
            if (url == null) {
                showError("inputForm.fxml not found on classpath.");
                return;
            }
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            String msg = cause.getClass().getSimpleName() + ": " + cause.getMessage();
            if (cause.getCause() != null) msg += " | " + cause.getCause().getMessage();
            showError("Load new simulation failed: " + msg);
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void goToResults(ActionEvent event)
    {
        try {
            java.net.URL url = getClass().getResource("/com/cs261/app/kpiHome.fxml");
            if (url == null) {
                showError("Results page not found.");
                return;
            }
            Parent root = FXMLLoader.load(url);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            showError(e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
        }
    }

    @FXML
    public void closeApp(ActionEvent event)
    {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
