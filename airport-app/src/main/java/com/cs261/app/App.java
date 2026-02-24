package com.cs261.app;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {
    public void start(Stage stage) throws Exception 
    {
        System.out.println(App.class.getResource("/com/cs261/app/start.fxml"));
        System.out.println(App.class.getResource("/com/cs261/app/companyLogo.png"));
        
        FXMLLoader loader = new FXMLLoader(
                App.class.getResource("/com/cs261/app/start.fxml"));
        //https://www.youtube.com/watch?v=9XJicRt_FaI
        //Scene scene = new Scene(loader.load());

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