package com.cs261.app.UI;

import javax.swing.GroupLayout.Alignment;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class RunwayAnim extends StackPane{
    public RunwayAnim(String runwaynumber){
        super();
        this.setId(runwaynumber);
        this.setWidth(300);
        this.setHeight(40);
        this.setAlignment(Pos.CENTER_RIGHT);
        Rectangle runwayShape = new Rectangle(300, 40);
        runwayShape.setStyle("-fx-background-color: grey; -fx-border-color: black; -fx-border-width: 1px;");
        this.getChildren().add(runwayShape);
    }
}
