package com.cs261.app.UI;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.Transition;

public class AirplaneNode extends ImageView{
    Transition anim;

    public AirplaneNode(String id, Transition animation) {
        super(new Image("/com/cs261/app/airplane.png"));
        this.setId(id);
        this.setScaleX(0.05);
        this.setScaleY(0.05);
        this.anim = animation;
    }   
}
