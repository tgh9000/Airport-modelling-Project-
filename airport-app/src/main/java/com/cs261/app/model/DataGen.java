package com.cs261.app.model;

import java.time.LocalDateTime;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import com.cs261.app.model.AirCraft.FlightType;
import com.cs261.app.model.AirCraft.EmergencyStatus;

public class DataGen {
    /* Potentially used elsewhere? Move to constants file after merging .... */
    private static final LocalDateTime SIM_START = LocalDateTime.of(2026, 1, 1, 0, 0); // 12 am as per design doc?

    /* */
    private Queue<AirCraft> scheduledArrivals;
    private Queue<AirCraft> scheduledDepartures;
    private Random random;

    /* */
    private List<OperatorData> operators;
    private List<AirportData> airports;
    private String ourAirport = "LHR"; // default for now.

    private double emergencyProbability = 0.01; // default: 1/100


    public DataGen(int runtime, int arrivalFlowRate, int departureFlowRate) {
        /* Sort Arrivals & Departures by actual entry time. */
        this.scheduledArrivals = new PriorityQueue<>((a, b) -> a.getEntryTime().compareTo(b.getEntryTime()));
        this.scheduledDepartures = new PriorityQueue<>((a, b) -> a.getEntryTime().compareTo(b.getEntryTime()));

        /* Initialise attribute information. */
        this.random = new Random();
        initData();

        generateFlights(runtime, arrivalFlowRate, departureFlowRate);
    }

    /**
     *  ...
     */
    private void initData() {
        this.operators = new ArrayList<>();
        this.airports = new ArrayList<>();

        /* Focus on UK & EU Airlines due to our home airport of Heathrow. */
        operators.add(new OperatorData("British Airways", "BAW", 0));
        operators.add(new OperatorData("EasyJet", "EZY", 0));
        operators.add(new OperatorData("TUI Airways", "TOM", 0));
        operators.add(new OperatorData("Ryanair", "RYR", 0));
        operators.add(new OperatorData("Air France", "AFR", 0));
        operators.add(new OperatorData("Lufthansa", "DLH", 0));
        operators.add(new OperatorData("KLM", "KLM", 0));
        operators.add(new OperatorData("Jet2", "EXS", 0));
        operators.add(new OperatorData("Aer Lingus", "EIN", 0));
        operators.add(new OperatorData("Virgin Atlantic", "VIR", 0));

        /* Same goes for airports. UK & EU \ Heathrow. */
        airports.add(new AirportData("Bristol", "Bristol Airport", "BRS"));
        airports.add(new AirportData("Birmingham", "Birmingham Airport", "BHX"));
        airports.add(new AirportData("Paris", "Charles de Gaulle", "CDG"));
        airports.add(new AirportData("Frankfurt", "Frankfurt Airport", "FRA"));
        airports.add(new AirportData("Barcelona", "El Prat", "BCN"));
        airports.add(new AirportData("Lisbon", "Lisbon Airport", "LIS"));
        airports.add(new AirportData("Dublin", "Dublin Airport", "DUB"));
        airports.add(new AirportData("Rome", "Rome Fiumicino Airport", "FCO"));
        airports.add(new AirportData("Zurich", "Zurich Airport", "ZRH"));
    }

    /**
     *  Generate all flights for the simulation.
     *  Preschedule flights then apply Box-Muller for variance.
     */
    private void generateFlights(int runtime, int arrivalRate, int departureRate) {
        /* Calculate ideal interval between flights (in minutes). */
        int arrivalInterval = 60 / arrivalRate;
        int departureInterval = 60 / departureRate;

        /* Generate arrivals. */
        for (int scheduledMinutes = 0; scheduledMinutes < runtime; scheduledMinutes += arrivalInterval) {
            LocalDateTime scheduledTime = SIM_START.plusMinutes(scheduledMinutes);
            LocalDateTime actualEntryTime = applyBoxMuller(scheduledMinutes);

            AirCraft arrival = generateArrival(scheduledTime, actualEntryTime);
            scheduledArrivals.add(arrival);
        }

        /* Generate departures. */
        for (int scheduledMinutes = 0; scheduledMinutes < runtime; scheduledMinutes += departureInterval) {
            LocalDateTime scheduledTime = SIM_START.plusMinutes(scheduledMinutes);
            LocalDateTime actualEntryTime = applyBoxMuller(scheduledMinutes);

            AirCraft departure = generateDeparture(scheduledTime, actualEntryTime);
            scheduledDepartures.add(departure);
        }
    }

    /** 
     *  x
     */
    private LocalDateTime applyBoxMuller(int scheduledMinutes) {
        return SIM_START;
    }

    /** 
     *  y
     */
    private AirCraft generateArrival(LocalDateTime scheduledTime, LocalDateTime actualEntryTime) {
        LocalDateTime scheduledTimeTemp = LocalDateTime.of(2026, 1, 1, 0, 4);
        LocalDateTime actualEntryTimeTemp = LocalDateTime.of(2026, 1, 1, 0, 3);

        return new AirCraft(
        FlightType.ARRIVAL,
        "BAW0001",
        "British Airways",
        "BRS",
        "LHR",
        scheduledTimeTemp,
        actualEntryTimeTemp,
        140.0f,
        300.0f,
        304.8f,
        EmergencyStatus.NONE
    );
    }

    /**
     *  z
     */
    private AirCraft generateDeparture(LocalDateTime scheduledTime, LocalDateTime actualEntryTime) {
        LocalDateTime scheduledTimeTemp = LocalDateTime.of(2026, 1, 1, 0, 4);
        LocalDateTime actualEntryTimeTemp = LocalDateTime.of(2026, 1, 1, 0, 2);
        return new AirCraft(
        FlightType.DEPARTURE,
        "BAW0002",
        "British Airways",
        "LHR",
        "BRS",
        scheduledTimeTemp,
        actualEntryTimeTemp,
        0.0f,
        300.0f,
        0.0f,
        EmergencyStatus.NONE
    );
    }

    /**
     *  a
     */
    private EmergencyStatus generateEmergencyStatus(boolean isArrival) {
        return EmergencyStatus.NONE;
    }

    /** 
     *  b
     */
    public List<AirCraft> getArrivalsForTick(int currentTime, int nextTime) {
        List<AirCraft> arrivals = new ArrayList<>();
        return arrivals;
    }

    /** 
     *  c
     */
    public List<AirCraft> getDeparturesForTick(int currentTime, int nextTime) {
        List<AirCraft> departures = new ArrayList<>();
        return departures;
    }

    /**
     *  Store Operator Data: (Operator, ICAO Prefix, Count).
     */
    private class OperatorData {
        String name;
        String icao;
        int count;

        OperatorData(String name, String icao, int count) {
           this.name = name;
           this.icao = icao;
           this.count = count; 
        }
    }

    /**
     *  Store Airport Data: (City, Airport, IATA Code).
     */
    private class AirportData {
        String city;
        String airport;
        String iata;

        AirportData(String city, String airport, String iata) {
            this.city = city;
            this.airport = airport;
            this.iata = iata;
        }
    }

}