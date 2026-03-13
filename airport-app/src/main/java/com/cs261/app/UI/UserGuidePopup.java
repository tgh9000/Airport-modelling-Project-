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
    private Text inputSectionText = new Text("There are multipled important input parameters to enter when creating a new simulation. The first is the length of the simulation in days, hours and minutes. The simulation length should not exceed 20 days. The flow rate of planes, the probability of failure and the time that can pass before cancellation are values that can be changed. You can add multiple runways, and add up to three interruptions for each runway. It is important to note that you cannot give the same bearing to more than one runway.");
    private VBox inputSection = new VBox(10);

    private Label outputSectionTitle = new Label("Output Guide");
    private Text outputSectionText = new Text("This model has multiple output pages that you can select between for your desired use. One important output is the information within the Key Performance Indicators page, which contains important metrics about the plane arrival and departure data, including data like the average wait times and delays in the takeoff and holding queue. Another output of the simulation is graphs which plot these delays. There are also more detailed tables which contain further aicraft information which you can filter based on the timestamps.");
    private VBox outputSection = new VBox(10);

    private Label compareSimGuide  = new Label("Saving Simulations");
    private Text compareSectionText = new Text("This model allows you to save and load previous simulations so that you can easily compare the effect of different airport input parameters and runway configurations on the performance of the airport. You can see this through the effect on the key metrics, which will be displayed when loading previous simulations. You can store multiple previous simulations and click through each of them to compare them.");
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
