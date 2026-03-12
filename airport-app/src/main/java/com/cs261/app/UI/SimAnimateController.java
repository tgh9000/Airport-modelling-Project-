package com.cs261.app.UI;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cs261.app.model.AirCraft;
import com.cs261.app.model.AirCraftMap;
import com.cs261.app.model.ArrivalModel;
import com.cs261.app.model.DataGen;
import com.cs261.app.model.DepartureModel;
import com.cs261.app.model.HoldingQueue;
import com.cs261.app.model.ModelOutput;
import com.cs261.app.model.RunWay;
import com.cs261.app.model.RunWayMap;
import com.cs261.app.model.TakeOffQueue;
import com.cs261.app.model.TrafficController;
import com.cs261.app.model.Utils;

import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class SimAnimateController {
    @FXML 
    ImageView hpPlane;
    @FXML
    Pane holdingPane;
    @FXML
    Label callSignLbl;
    @FXML
    Label emergencyLbl;

    HoldingAnimController holdingPatternAnim;
    
    // model stuff
    private int runtime; // in seconds

    private AirCraftMap aircraftMap = new AirCraftMap();
    private HoldingQueue holdingPattern = new HoldingQueue(500);
	private TakeOffQueue takeOffQueue = new TakeOffQueue();
	private RunWayMap runways;
	private ArrivalModel arrivalModel = new ArrivalModel(aircraftMap, holdingPattern);
	private DepartureModel departureModel = new DepartureModel(aircraftMap, takeOffQueue);
	private TrafficController trafficModel;
	private DataGen dataGen;
	private ModelOutput output;
    private SimTimer clock;


    public void init(RunWay[] runways, int days, int hours, int mins, float failprob, int cancelTime){
        this.runways = new RunWayMap(runways);
        Utils.setCancelTime(cancelTime);
        Utils.setEmergencyProbability((double) failprob);

        // make the date
        LocalDateTime endtime = LocalDateTime.of(2026, 1, 1, 0,0,0).plusDays((long) days).plusHours((long) hours).plusMinutes((long) mins);
        this.runtime = Utils.convertDateToTicks(endtime);

		this.trafficModel = new TrafficController(aircraftMap, this.runways, holdingPattern, takeOffQueue, arrivalModel, departureModel);
		this.dataGen = new DataGen(runtime, Utils.flow, Utils.flow); // init with runtime and default flow rate per hour.
		this.output = new ModelOutput(aircraftMap, departureModel, arrivalModel, this.runways);
        
        this.clock = new SimTimer();
        this.holdingPatternAnim = new HoldingAnimController(hpPlane, holdingPane, callSignLbl, emergencyLbl);
    
        startAnimation();   
    }

    public ModelOutput getOutput(){
        return output;
    }

    public void startAnimation(){
        System.out.println("starting");
        clock.start();
    }

    public void successEvent(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cs261/app/outputPage.fxml"));
            Parent p = loader.load();
            Stage stage = (Stage) holdingPane.getScene().getWindow();
            stage.getScene().setRoot(p);

            OutputPageController outputController = loader.getController();
            outputController.init(output);
        } catch (Exception e){
            e.printStackTrace();
            // code to close program or restart task
        }
    }

    public HoldingAnimController getHoldingAnimation(){
        return holdingPatternAnim;
    }

    private class SimTimer extends AnimationTimer{

        private long FPS = 60L;
        private long INTERVAL =  1000000000L / FPS;
        private long prevTime = 0;

        private int simtime = 0; // 'time passed in simulation' = how many times handle should be called

        public SimTimer(){
            super();
        }

        @Override
        public void handle(long currentTime) {
            if (simtime >= runtime){
                System.out.printf("DONE DONE DONE \n");
                stop();
                successEvent();
            }

            if (currentTime - prevTime > INTERVAL){
                double elapsed = (currentTime - prevTime) / 1000000000.0;
                
                
                List<AirCraft> newArrivals = dataGen.getArrivalsForTick(simtime, simtime + Utils.timeInc);
                List<AirCraft> newDepartures = dataGen.getDeparturesForTick(simtime, simtime + Utils.timeInc);
                arrivalModel.updateArrivals(newArrivals, simtime);
                departureModel.updateDepartures(newDepartures, simtime);
                ArrayList<String> exits = trafficModel.updateTraffic(simtime); 
                System.out.println("returned traffic");
                simtime += Utils.timeInc;
                output.updateOutput(exits, holdingPattern.size(), takeOffQueue.size(), simtime);
                // now do animation stuff????
                AirCraft holdingTop = holdingPattern.top();
                if (holdingTop == null){
                    holdingPatternAnim.invisible();
                } else {
                    if (holdingPatternAnim.getCallSignText().get() != holdingTop.getCallSign()){
                        holdingPatternAnim.updateCallsignProp(holdingTop.getCallSign());
                        holdingPatternAnim.updateEmergencyText(holdingTop.getEmergencyStatus().toString());
                        holdingPatternAnim.step(elapsed);
                    }                
                }

                prevTime = currentTime;
            }
        }
        
    }
}
