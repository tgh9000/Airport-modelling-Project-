package com.cs261.app.UI;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Window;

public class UserGuidePopup {
    private Popup popupWindow = new Popup();

    private ScrollPane layoutScroll = new ScrollPane();

    private VBox layoutGuide = new VBox(50);

    private Label stepsSectionTitle = new Label("Getting started");

    private String stepsT = "To the the simulation, press 'Load New Simulation' on the front page. On the input page, please enter the required fields as indicated by the * symbols. Once the fields have been entered, press 'Start Simulation' for the simulation to start processing. \n"
                            + "Once the simulation has finished processing, the output page will be loaded. The first page are the key statistics. The dark blue menu on the left can be used to navigate to further output pages."; 
    private String inputT = "Runtime: The runtime input is how long the function of the airport will be simulated for. This determines the airport flight schedule. The runtime of the simulation can be entered in days, hours and minutes. \n"
                                + "Arrival and departure flow rate: The arrival flow rate is the number of incoming planes per hour in the simulation. The departure flow rate is the number of outgoing planes per hour in the simulation. The default flow rate for arrivals and departures is 15.\n"
                                + "Probability of failure: the chance of a mechanical failure emergency or a passenger health emergency occuring in an arrival plane.\n"
                                + "Time that can pass before cancellation: The number of minutes a departure flight can wait before being cancelled. The default value is 30 minutes.\n"
                                + "Interuptions: A change in operational status which causes runway closure. For each interuption added, the start time and duration of the interuption can be inputed. Each runway can have up to three interuptions during runtime.\n"
                                + "Start time of interuption: Time elapsed in simulation before the interuption occurs. \n"
                                + "Duration of interuption: How long the interruption will last for. \n"
                                + "Runways: 1 to 10 (inclusive) runways may be added for the simulated airport.";
    private String outputT =  "The key statistics are an overview of the airport traffic during simulation. \n"
                                + "Graphs: the graphs of each queue wait time against simulation time have been provided to identify bottlenecks in traffic. \n"
                                + "The graphs of queue lengths over time have been provided to show the changing structure of airport queues during runtime. \n" 
                                + "Departure table: The departure table provides information on every departure flight generated during take off. \n"
                                + "Arrival table: The arrival table provides information on every arrival flight generated during landing. \n"
                                + "Take off queue table: Provides information on every flight which has been in the take off queue during runtime. \n"
                                + "Holding pattern table: Provides information on every flight which has been in the holding pattern during runtime. \n"
                                + "Runway flight data: Provides information on which flights landed on which runways. The table can be sorted by runway number.\n"
                                + "Time stamp search: For each table where the search function is available, the time elapsed in minutes may be inputted to search for the flights simulated during the inputted times. \n"
                                + "Side menu: By pressing the blue '<' arrow button on the right side, a searchable (by callsign) table of the airplane schedule can be accessed for further details. A searchable table of runways (by runway number) can be accessed as well. \n";
    private String compareT = "The simulation may be exitted by clicking the 'Exit Simulation' button on the menu in the 'Key Statistics' page. This will show a 'Save Data' prompt, which will save the data for comparisons before exitting the current simulation run.\n"
                                + "The 'Compare Previous Simulations button on the front page menu will provide a list of previous simulations which may be selected to view past statistics. \n"
                                + "Multiple simulations can be selected from the list at a time. Use CTRL + LEFT-CLICK to select a simulation to view. The key statistics for the selected simulation will appear under the respective date in the right hand panel. \n"
                                + "Simulations can be de-selected by using CTRL + LEFT-CLICK on a highlighted simulation name in the list.\n"
                                + "Please note the simulation will be named under the real-world date it was created on. This is not changable. \n";
    private String restrictT = "Runway conversions: An exclusive use (take off only/ landing only) runway will only be converted into a mixed use runway if all runways of an exclusive type have been closed due to user-inputted interuptions. This will contribute to the delay and wait time statistics. This is a special case modelling scenario and the runways will NOT be converted back for the rest of the simulation. \n"
                                + "Mixed runway use: mixed runways will alternate between handling arrivals and departures, starting on arrivals. \n"
                                + "Runway time: Once a plane has been allocated a runway for arrival/departure, it will spend '2 minutes' on the runway. Please note that this simulates extended queue wait times. \n"
                                + "Diversion reduction: The simulation will attempt to reduce diversions by placing any planes in emergency on an available mixed runway immediately if no exclusive runways are available, regardless of mixed runway turn. \n"
                                + "Cancellation reduction: The simulation will attempt to reduce cancellations by place any planes which are 10 minutes away from being cancelled onto an available mixed runway immediately, regardless of mixed runway turn.\n"
                                + "Emergency wait time: Even if it does not land immediately, an emergency plane will remain in the simulation for 4 minutes in an attempt to reduce diversions. \n"
                                + "Fuel emergencies are decided during runtime. Fuel emergencies will be triggered if an aircraft in the holding queue only has 15 minutes of fuel left. Health and mechanical emergencies are pre-determined based on probability. \n"
                                + "All emergencies are treated with equal priority. Emergency planes that arrived earlier have higher priority than those that arrived later. \n";

    private Text stepsSectionText = new Text(stepsT);
    private VBox stepsSection = new VBox(10);    

    private Label inputSectionTitle = new Label("Input Parameters Guide");
    private Text inputSectionText = new Text(inputT);
    private VBox inputSection = new VBox(10);

    private Label outputSectionTitle = new Label("Output Guide");
    private Text outputSectionText = new Text(outputT);
    private VBox outputSection = new VBox(10);

    private Label compareSimGuide  = new Label("Saving Simulations");
    private Text compareSectionText = new Text(compareT);
    private VBox compareSection = new VBox(10);

    private Label restrictionsGuide = new Label("Model Constraints and Logic Features");
    private Text restrictionText = new Text(restrictT);
    private VBox restrictionSection = new VBox(10);

    public UserGuidePopup(){
        //popupWindow.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_TOP_LEFT);        

        layoutScroll.setContent(layoutGuide);
        layoutScroll.setMaxHeight(400);
        layoutGuide.setMinHeight(700);

        layoutScroll.getScene().getStylesheets().add("");

        stepsSectionText.setWrappingWidth(450);
        stepsSection.getChildren().addAll(stepsSectionTitle, stepsSectionText);

        inputSectionText.setWrappingWidth(450);
        inputSection.getChildren().addAll(inputSectionTitle, inputSectionText);

        outputSectionText.setWrappingWidth(450);
        outputSection.getChildren().addAll(outputSectionTitle, outputSectionText);

        compareSectionText.setWrappingWidth(450);
        compareSection.getChildren().addAll(compareSimGuide, compareSectionText);

        restrictionText.setWrappingWidth(450);
        restrictionSection.getChildren().addAll(restrictionsGuide, restrictionText);

        layoutGuide.getChildren().addAll(stepsSection, inputSection, outputSection, compareSection, restrictionSection);

        popupWindow.getContent().add(layoutScroll);
        popupWindow.setAutoHide(true);

        layoutScroll.setStyle("-fx-background-color: #E3EDF9; -fx-padding: 10;");
        layoutGuide.setStyle("-fx-background-color: #E3EDF9; -fx-padding: 10;");
        stepsSection.setStyle("-fx-background-color: #F0F4F8; -fx-background-radius: 16px; -fx-padding: 10;");
        inputSection.setStyle("-fx-background-color: #F0F4F8; -fx-background-radius: 16px; -fx-padding: 10;");
        outputSection.setStyle("-fx-background-color: #F0F4F8; -fx-background-radius: 16px; -fx-padding: 10;");
        compareSection.setStyle("-fx-background-color: #F0F4F8; -fx-background-radius: 16px; -fx-padding: 10;");
        restrictionSection.setStyle("-fx-background-color: #F0F4F8; -fx-background-radius: 16px; -fx-padding: 10;");

        stepsSectionTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: BOLD;");
        stepsSectionText.setStyle("-fx-font-size: 14px;");
        inputSectionTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: BOLD;");
        inputSectionText.setStyle("-fx-font-size: 14px;");
        outputSectionTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: BOLD;");
        outputSectionText.setStyle("-fx-font-size: 14px;");
        compareSimGuide.setStyle("-fx-font-size: 16px; -fx-font-weight: BOLD;");
        compareSectionText.setStyle("-fx-font-size: 14px;");
        restrictionsGuide.setStyle("-fx-font-size: 16px; -fx-font-weight: BOLD;");
        restrictionText.setStyle("-fx-font-size: 14px;");
    }

    public void show(Window w){
        this.popupWindow.show(w);
    }

    public void hide(){
        this.popupWindow.hide();
    }
}
