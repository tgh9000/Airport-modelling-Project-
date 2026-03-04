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
    /* Pre generate flight queues, sorted by actual entry time. */
    private Queue<AirCraft> scheduledArrivals;
    private Queue<AirCraft> scheduledDepartures;
    private Random random;

    /* Data required for aircraft attributes in the simulation. */
    private List<OperatorData> operators;
    private List<AirportData> airports;
    private String ourAirport = "LHR"; // Default: London Heathrow.

    private double emergencyProbability = 0.01; // Default: 1/100 Chance of an Emergency occuring.


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
     *  Initialise entries for operator and airport lists.
     *  Prioritising UK Airlines and Destinations but also including those from EU (due to the "home" airport and fuel limitations -> somewhat realistic).
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
     * 
     *  @param runtime Desired simulation time in minutes.
     *  @param arrivalRate Arrivals per hour.
     *  @param departureRate Departures per hour.
     */
    private void generateFlights(int runtime, int arrivalRate, int departureRate) {
        /* Calculate ideal interval between flights (in minutes). */
        int arrivalInterval = 60 / arrivalRate;
        int departureInterval = 60 / departureRate;

        /* Generate arrivals. */
        for (int scheduledMinutes = 0; scheduledMinutes < runtime; scheduledMinutes += arrivalInterval) {
            LocalDateTime scheduledTime = Utils.simulationStart.plusMinutes(scheduledMinutes);
            LocalDateTime actualEntryTime = applyBoxMuller(scheduledMinutes);

            AirCraft arrival = generateArrival(scheduledTime, actualEntryTime);
            scheduledArrivals.add(arrival);
        }

        /* Generate departures. */
        for (int scheduledMinutes = 0; scheduledMinutes < runtime; scheduledMinutes += departureInterval) {
            LocalDateTime scheduledTime = Utils.simulationStart.plusMinutes(scheduledMinutes);
            LocalDateTime actualEntryTime = applyBoxMuller(scheduledMinutes);

            AirCraft departure = generateDeparture(scheduledTime, actualEntryTime);
            scheduledDepartures.add(departure);
        }
    }

    /** 
     *  Handle variance in flight times via applying the Box-Muller transformation.
     *  Generates normally distributed random variance with mean = scheduledMinutes and s.d = 5.
     * 
     *  @param scheduledMinutes Ideal scheduled time in minutes from simulation start.
     *  @return Actual entry time with variance applied.
     */
    private LocalDateTime applyBoxMuller(int scheduledMinutes) {
        double u1 = random.nextDouble();
        double u2 = random.nextDouble();

        double x1 = Math.sqrt(-2 * Math.log(u2)) * Math.cos(2 * Math.PI * u1);

        int variance = (int)(5 * x1);
        int actualMinutes = scheduledMinutes + variance;

        if (actualMinutes < 0) {
            actualMinutes = 0;
        }

        /* Convert to LocalDateTime and return. */
        return Utils.simulationStart.plusMinutes(actualMinutes);
    }

    /** 
     *  Handle generation of a single arriving aircraft and it's attributes.
     * 
     *  Scheduled arrival time = actual entry time + landing duration (runway time).
     *  Altitude is set to the base value (304.8m/1000ft) but will be recalculated based on the state of the holding queue when the aircraft is enqueued.
     * 
     *  @param scheduledTime Scheduled time of generation. 
     *  @param actualEntryTime Time the aircraft actually enters the simulation.
     *  @return the configured arriving AirCraft object.
     */
    private AirCraft generateArrival(LocalDateTime scheduledTime, LocalDateTime actualEntryTime) {
        /* Generate operator and callsign. */
        OperatorData operator = operators.get(random.nextInt(operators.size()));
        operator.count++;
        String callsign = operator.icao + operator.count;

        /* Generate origin and destination. */
        AirportData originAirport = airports.get(random.nextInt(airports.size()));
        String origin = originAirport.iata;
        String destination = ourAirport;

        float altitude = Utils.baseArrivalAltitudeMetres; // Base altitude assuming holding queue is empty.
        
        float speed = Utils.groundSpeedKnots; // Ground Speed in knots.

        /* Generate fuel level uniformly distributed between 20-60 minutes remaining. */
        float fuelMin = (Utils.groundSpeedKnots * (1.0f/3) * Utils.fuelBurn) / Utils.fuelDensity; // 20 Minutes / 1/3 Hours Remaining.
        float fuelMax = (Utils.groundSpeedKnots * 1 * Utils.fuelBurn) / Utils.fuelDensity; // 60 Minutes / 1 Hour Remaining.
        float fuel = fuelMin + random.nextFloat() * (fuelMax - fuelMin);

        /* Generate emergency status for an arriving aircraft. */
        EmergencyStatus emergency = generateEmergencyStatus(true);
        int emergencyTime = -1;
        if (emergency != EmergencyStatus.NONE) {
            emergencyTime = Utils.convertDateToTicks(actualEntryTime);
        }

        /** scheduledArrival is the ideal time of generation + the constant runway time (including landing etc) */
        LocalDateTime scheduledArrival = scheduledTime.plusMinutes(Utils.runwayTime);
        
        /* Create aircraft and return. */
        return new AirCraft(
            FlightType.ARRIVAL,
            callsign,
            operator.name,
            origin,
            destination,
            scheduledArrival,
            actualEntryTime,
            speed,
            fuel,
            altitude,
            emergency,
            emergencyTime
        );
    }

    /** 
     *  Handle generation of a single departing aircraft and it's attributes.
     * 
     *  Scheduled departure time = actual entry time + uniform random offset (10-30 minutes).
     *  Departures are generated on the ground (altitude = 0, speed = 0) and never spawn with an emergency status.
     * 
     *  @param scheduledTime Scheduled time of generation. 
     *  @param actualEntryTime Time the aircraft actually enters the simulation.
     *  @return the configured departing AirCraft object.
     */
    private AirCraft generateDeparture(LocalDateTime scheduledTime, LocalDateTime actualEntryTime) {
        /* Generate operator and callsign. */
        OperatorData operator = operators.get(random.nextInt(operators.size()));
        operator.count++;
        String callsign = operator.icao + operator.count;

        /* Generate origin and destination. */
        String origin = ourAirport;
        AirportData destinationAirport = airports.get(random.nextInt(airports.size()));
        String destination = destinationAirport.iata;

        float altitude = 0.0f; // on Ground.

        float speed = 0.0f; // Stationary until takeoff.

        /* Generate fuel level uniformly distributed between 20-60 minutes remaining. */
        float fuelMin = (Utils.groundSpeedKnots * (1.0f/3) * Utils.fuelBurn) / Utils.fuelDensity; // 20 Minutes / 1/3 Hours Remaining.
        float fuelMax = (Utils.groundSpeedKnots * 1 * Utils.fuelBurn) / Utils.fuelDensity; // 60 Minutes / 1 Hour Remaining.
        float fuel = fuelMin + random.nextFloat() * (fuelMax - fuelMin);

        EmergencyStatus emergency = EmergencyStatus.NONE; // Departures: on ground -> not generated with emergency.
        int emergencyTime = -1;

        /** scheduledDeparture is the ideal time of generation + uniform random offset (10-30 minutes). */
        int departureOffset = 10 + random.nextInt(21); // uniform(10,30)
        LocalDateTime scheduledDeparture = scheduledTime.plusMinutes(departureOffset);

        /* Create aircraft and return. */
        return new AirCraft(
            FlightType.DEPARTURE,
            callsign,
            operator.name,
            origin,
            destination,
            scheduledDeparture,
            actualEntryTime,
            speed,
            fuel,
            altitude,
            emergency,
            emergencyTime
        );
    }

    /**
     *  Assign emergency status randomly based on probability.
     * 
     *  Arrivals have a 1% chance (default) of generating with an emergency.
     *  Of this 1%, 
     *      75% chance: Passenger Health
     *      25% chance: Mechanical Failure
     * 
     *  Departures never generate with emergency status.
     *  Fuel emergencies are not generated; they occur dynamically during simulation.
     * 
     *  @param isArrival true if assigning emergency status of arriving aircraft, false otherwise.
     *  @return Emergency Status: HEALTH, MECH or NONE.
     */
    private EmergencyStatus generateEmergencyStatus(boolean isArrival) {
        if (!isArrival) {
            return EmergencyStatus.NONE; // Departures never generate with an emergency. 
        }

        if (random.nextDouble() < emergencyProbability) {
            if (random.nextDouble() < 0.75) {
                return EmergencyStatus.HEALTH;
            } else {
                return EmergencyStatus.MECH;
            }
        }

        return EmergencyStatus.NONE;
    }

    /** 
     *  Retrieve arriving aircraft which should be enqueued this tick.
     * 
     *  @param currentTime Start of current tick (in minutes from simulation start).
     *  @param nextTime End of current tick.
     *  @return List of arriving Aircraft objects that should enter the simulation this tick.
     */
    public List<AirCraft> getArrivalsForTick(int currentTime, int nextTime) {
        List<AirCraft> arrivals = new ArrayList<>();
        LocalDateTime nextT = Utils.simulationStart.plusMinutes(nextTime);

        while (!scheduledArrivals.isEmpty() && !scheduledArrivals.peek().getEntryTime().isAfter(nextT)) {
            arrivals.add(scheduledArrivals.poll());
        }
        System.out.println("getArrivalsForTick");
        return arrivals;
    }

    /** 
     *  Retrieve departing aircraft which should be enqueued this tick.
     * 
     *  @param currentTime Start of current tick (in minutes from simulation start).
     *  @param nextTime End of current tick.
     *  @return List of departing Aircraft objects that should enter the simulation this tick.
     */
    public List<AirCraft> getDeparturesForTick(int currentTime, int nextTime) {
        List<AirCraft> departures = new ArrayList<>();
        LocalDateTime nextT = Utils.simulationStart.plusMinutes(nextTime);

        while (!scheduledDepartures.isEmpty() && !scheduledDepartures.peek().getEntryTime().isAfter(nextT)) {
            departures.add(scheduledDepartures.poll());
        }
        System.out.println("getDeparturesForTick");
        return departures;
    }

    /**
     *  Store Operator Data: (Operator, ICAO Prefix, Count).
     *  Count tracks the number of flights that have been generated for callsign numbering.
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
     *  Additional information stored for UI.
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