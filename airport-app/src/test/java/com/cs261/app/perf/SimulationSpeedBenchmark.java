package com.cs261.app.perf;

import com.cs261.app.controller.SimLoop;
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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
Measures how long it takes to simulate a run with N flights, and the resulting ticks per second.
The loop below mirrors SimLoop.call() so the flow rate (and therefore N) can be varied;
SimLoop itself always builds DataGen with Utils.flow (15/hr). Results go to target/benchmarks/.
Run with: mvn test -Dbenchmarks
*/
@Tag("benchmark")
public class SimulationSpeedBenchmark {

    private static final int TICKS_PER_DAY = 24 * 60;
    private static final int WARMUP_RUNS = 3;
    private static final int MEASURED_RUNS = 7;

    // exposes SimLoop.call() so the real class can be timed synchronously
    private static class TestableSimLoop extends SimLoop {
        TestableSimLoop(RunWay[] runways, int runtime) { super(runways, runtime); }
        ModelOutput runSync() throws Exception { return super.call(); }
    }

    private static RunWay[] runways() {
        RunWay.OperatingMode[] modes = {
                RunWay.OperatingMode.LANDING, RunWay.OperatingMode.LANDING,
                RunWay.OperatingMode.TAKEOFF, RunWay.OperatingMode.TAKEOFF,
                RunWay.OperatingMode.MIXED
        };
        RunWay[] r = new RunWay[modes.length];
        for (int i = 0; i < modes.length; i++) {
            r[i] = new RunWay(3000, 36 * i, modes[i], new RunWay.OperationStatus[0], new int[0][0]);
        }
        return r;
    }

    /** One full simulation: schedule generation, tick loop and final statistics. */
    private static ModelOutput simulate(int runtime, int arrivalFlow, int departureFlow) {
        AirCraftMap aircraftMap = new AirCraftMap();
        HoldingQueue holdingPattern = new HoldingQueue(500);
        TakeOffQueue takeOffQueue = new TakeOffQueue();
        RunWayMap runwayMap = new RunWayMap(runways());
        ArrivalModel arrivalModel = new ArrivalModel(aircraftMap, holdingPattern);
        DepartureModel departureModel = new DepartureModel(aircraftMap, takeOffQueue);
        TrafficController traffic = new TrafficController(aircraftMap, runwayMap, holdingPattern, takeOffQueue, arrivalModel, departureModel);
        DataGen dataGen = new DataGen(runtime, arrivalFlow, departureFlow);
        ModelOutput output = new ModelOutput(aircraftMap, departureModel, arrivalModel, runwayMap);

        int currentTime = 0;
        while (currentTime != runtime) {
            List<AirCraft> newArrivals = dataGen.getArrivalsForTick(currentTime, currentTime + Utils.timeInc);
            List<AirCraft> newDepartures = dataGen.getDeparturesForTick(currentTime, currentTime + Utils.timeInc);
            arrivalModel.updateArrivals(newArrivals, currentTime);
            departureModel.updateDepartures(newDepartures, currentTime);
            ArrayList<String> exits = traffic.updateTraffic(currentTime);
            currentTime += Utils.timeInc;
            output.updateOutput(exits, holdingPattern.size(), takeOffQueue.size(), currentTime);
        }
        output.finishCalculations();
        return output;
    }

    private static double median(double[] xs) {
        double[] s = xs.clone();
        Arrays.sort(s);
        return s[s.length / 2];
    }

    @Test
    void simulationSpeed_byFlightCount() throws IOException {
        // {days, flights per hour each way}; DataGen spaces flights 60/rate minutes apart (integer),
        // so only rates that divide 60 give distinct schedules
        int[][] configs = {
                {1, 10}, {1, 15}, {1, 20}, {1, 30}, {1, 60},
                {7, 30}, {30, 30}
        };

        StringBuilder csv = new StringBuilder("days,flowPerHour,flights,ticks,medianMs,minMs,ticksPerSec,flightsPerSec\n");
        System.out.println("\n=== Simulation speed (median of " + MEASURED_RUNS + " runs, 5 runways) ===");
        System.out.printf("%5s %6s %8s %8s %10s %10s %14s%n", "days", "flow/h", "flights", "ticks", "median ms", "min ms", "ticks/sec");

        for (int[] c : configs) {
            int runtime = c[0] * TICKS_PER_DAY;
            int flow = c[1];
            int flights = 0;

            for (int i = 0; i < WARMUP_RUNS; i++) simulate(runtime, flow, flow);

            double[] ms = new double[MEASURED_RUNS];
            for (int i = 0; i < MEASURED_RUNS; i++) {
                long t0 = System.nanoTime();
                ModelOutput out = simulate(runtime, flow, flow);
                ms[i] = (System.nanoTime() - t0) / 1e6;
                flights = out.getTotalArrivalsGenerated() + out.getTotalDeparturesGenerated();
                assertTrue(flights > 0);
            }

            double med = median(ms);
            double min = Arrays.stream(ms).min().orElse(med);
            double tps = runtime / (med / 1000.0);
            System.out.printf("%5d %6d %8d %8d %10.2f %10.2f %,14.0f%n", c[0], flow, flights, runtime, med, min, tps);
            csv.append(String.format("%d,%d,%d,%d,%.3f,%.3f,%.0f,%.0f%n", c[0], flow, flights, runtime, med, min, tps, flights / (med / 1000.0)));
        }

        write("simulation_speed.csv", csv);
    }

    @Test
    void simulationSpeed_realSimLoop_oneDayDefaults() throws Exception {
        int runtime = TICKS_PER_DAY;
        for (int i = 0; i < WARMUP_RUNS; i++) new TestableSimLoop(runways(), runtime).runSync();

        double[] ms = new double[MEASURED_RUNS];
        int flights = 0;
        for (int i = 0; i < MEASURED_RUNS; i++) {
            long t0 = System.nanoTime();
            ModelOutput out = new TestableSimLoop(runways(), runtime).runSync();
            ms[i] = (System.nanoTime() - t0) / 1e6;
            flights = out.getTotalArrivalsGenerated() + out.getTotalDeparturesGenerated();
        }
        double med = median(ms);
        System.out.printf("%n=== Real SimLoop, 1 day at default 15/h: %d flights, median %.2f ms, %,.0f ticks/sec ===%n",
                flights, med, runtime / (med / 1000.0));
        write("simloop_oneday.csv", new StringBuilder(String.format("flights,ticks,medianMs,ticksPerSec%n%d,%d,%.3f,%.0f%n",
                flights, runtime, med, runtime / (med / 1000.0))));
    }

    static void write(String name, CharSequence content) throws IOException {
        Path dir = Path.of("target", "benchmarks");
        Files.createDirectories(dir);
        Files.writeString(dir.resolve(name), content);
    }
}
