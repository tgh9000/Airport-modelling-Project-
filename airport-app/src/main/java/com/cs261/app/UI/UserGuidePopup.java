package com.cs261.app.UI;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Window;

public class UserGuidePopup {
    private Popup popupWindow = new Popup();

    private ScrollPane layoutScroll = new ScrollPane();

    private VBox layoutGuide = new VBox(50);

    private Label inputSectionTitle = new Label("Input Parameters Guide");
    private Text inputSectionText = new Text("At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod m");
    private VBox inputSection = new VBox(10);

    private Label outputSectionTitle = new Label("Output Guide");
    private Text outputSectionText = new Text("At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod m");
    private VBox outputSection = new VBox(10);

    private Label compareSimGuide  = new Label("Saving Simulations");
    private Text compareSectionText = new Text("At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod m");
    private VBox compareSection = new VBox(10);

    private Label restrictionsGuide = new Label("Model Constraints");
    private Text restrictionText = new Text("At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod m");
    private VBox restrictionSection = new VBox(10);


    public UserGuidePopup(){
        //popupWindow.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_TOP_LEFT);        

        layoutScroll.setContent(layoutGuide);
        layoutScroll.setPrefSize(500, 500);
        layoutGuide.setMinWidth(500);
        layoutGuide.setMinWidth(500);

        inputSectionText.setWrappingWidth(450);
        inputSection.getChildren().addAll(inputSectionTitle, inputSectionText);

        outputSectionText.setWrappingWidth(450);
        outputSection.getChildren().addAll(outputSectionTitle, outputSectionText);

        compareSectionText.setWrappingWidth(450);
        compareSection.getChildren().addAll(compareSimGuide, compareSectionText);

        restrictionText.setWrappingWidth(450);
        restrictionSection.getChildren().addAll(restrictionsGuide, restrictionText);

        layoutGuide.getChildren().addAll(inputSection, outputSection, compareSection, restrictionSection);

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
