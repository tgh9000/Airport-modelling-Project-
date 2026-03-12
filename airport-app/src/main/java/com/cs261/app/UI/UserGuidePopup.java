package com.cs261.app.UI;

import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Window;

public class UserGuidePopup {
    private Popup popupWindow = new Popup();

    public void UserGuidePopup(){
        popupWindow.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_TOP_LEFT);
        Label test = new Label("hello");
        popupWindow.getContent().add(test);
        
    }

    public void show(Window w){
        this.popupWindow.show(w);
    }

    public void hide(){
        this.popupWindow.hide();
    }
}
