package com.cs261.app;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application 
{
    /**
     * Initialises the app with the correct scene
     */
    public void start(Stage stage) throws Exception 
    {
        
        FXMLLoader loader = new FXMLLoader(
                App.class.getResource("/com/cs261/app/start.fxml"));

        Image icon = new Image(
        getClass().getResource("/com/cs261/app/companyLogo.png").toExternalForm()
        );
        Scene scene = new Scene(loader.load());
        stage.setMaximized(true);
        stage.getIcons().add(icon);
        stage.setTitle("Airport App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) 
    {
                    
        // launch ui
        launch(args);

    }
}