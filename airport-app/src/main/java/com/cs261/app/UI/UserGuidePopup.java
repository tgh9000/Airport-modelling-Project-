package com.cs261.app.UI;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Window;

public class UserGuidePopup {
    private Popup popupWindow = new Popup();

    private ScrollPane layoutScroll = new ScrollPane();

    private VBox layoutGuide = new VBox(50);

    private Label inputSectionTitle = new Label("Input Parameters Guide");

    private Label outputSectionTitle = new Label("Output Guide");

    private Label compareSimGuide  = new Label("Saving Simulations");

    private Label restrictionsGuide = new Label("Model Constraints");

    public UserGuidePopup(){
        //popupWindow.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_TOP_LEFT);        

        layoutScroll.setContent(layoutGuide);
        layoutScroll.setPrefSize(500, 500);
        layoutGuide.setMinWidth(500);
        layoutGuide.setMinWidth(500);

        layoutGuide.getChildren().addAll(inputSectionTitle, outputSectionTitle, compareSimGuide, restrictionsGuide);

        popupWindow.getContent().add(layoutScroll);
        popupWindow.setAutoHide(true);
    }

    public void show(Window w){
        this.popupWindow.show(w);
    }

    public void hide(){
        this.popupWindow.hide();
    }
}
