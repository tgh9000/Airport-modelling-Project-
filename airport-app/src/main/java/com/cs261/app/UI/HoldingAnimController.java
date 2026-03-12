package com.cs261.app.UI;

import javafx.animation.PathTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;

public class HoldingAnimController {
    private Pane holdingPane;
    private ImageView plane;
    private StringProperty callSignText = new SimpleStringProperty(null);
    private StringProperty emergencyText = new SimpleStringProperty(null);
    private Label callsignLbl;
    private Label emergencyLbl;

    private double holdingCenterX;
    private double holdingCenterY;
    private double radiusX = 100;
    private double radiusY = 100;
    private double speed = 2.0;
    private double angle = 0;

    public HoldingAnimController(ImageView plane, Pane bgPane, Label callsign, Label emergency){    
        this.holdingPane = bgPane;
        this.plane = plane;
        this.callsignLbl = callsign;
        this.emergencyLbl = emergency;

        this.callsignLbl.textProperty().bind(callSignText);
        this.emergencyLbl.textProperty().bind(emergencyText);

        holdingCenterX = holdingPane.getLayoutX() / 2;
        holdingCenterY = holdingPane.getLayoutY() / 2;

        // plane.setLayoutX(holdingCenterX + radiusX);
        // plane.setLayoutY(holdingCenterY);

    }

    public void step(double t){
            angle += speed * t;

            plane.setLayoutX(holdingCenterX + radiusX * Math.cos(angle));
            plane.setLayoutY(holdingCenterY + radiusY * Math.sin(angle));

            plane.setOpacity(1);
    }

    public void invisible(){
            plane.setOpacity(1);
    }

    public StringProperty getCallSignText() {
        return callSignText;
    }

    public StringProperty getEmergencyText() {
        return emergencyText;
    }

    public void updateCallsignProp(String c){
        this.callSignText.set(c);
    }

    public void updateEmergencyText(String e){
        this.emergencyText.set(e);
    }
}
