package com.cs261.app.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *  Handles storing and loading of simulation metrics as CSV.
 *  Each row represents a completed run of the simulation identified by it's date and time.
 */
public class HandleOutput {
    private static final String header = 
        "simName,totalArrivals,totalDepartures,totalLanded,totalTakenOff," +
        "maxHoldQ,maxTOQ,maxArrDelay,maxDepDelay," +
        "avgArrDelay,avgDepDelay,AvgHoldWait,avgTOWait";

    private static final String filePath = "simMetrics.csv";

    /**
     * Store metrics from a completed simulation to the CSV file.
     * Runs are idenified by the date and time when calling storeMetrics(). 
     * @param output the ModelOutput containing our metrics.
     */
    public void storeMetrics(ModelOutput output) {
        File file = new File(filePath);
        boolean needsHeader = !file.exists() || file.length() == 0;

        /** Get current date and time. */
        String simName = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String row = simName + ","
            + output.getTotalArrivalsGenerated() + ","
            + output.getTotalDeparturesGenerated() + ","
            + output.getTotalAircraftLanded() + ","
            + output.getTotalAircraftTakenOff() + ","
            + output.getMaxHoldingQueueLength() + ","
            + output.getMaxTakeOffQueueLength() + ","
            + output.getMaxArriveDelay() + ","
            + output.getMaxDepartDelay() + ","
            + String.format("%.2f", output.getAvgArriveDelay()) + ","
            + String.format("%.2f", output.getAvgDepartDelay()) + ","
            + String.format("%.2f", output.getAverageHoldingWaitTime()) + ","
            + String.format("%.2f", output.getAverageTakeOffWaitTime());

        /** Append row to file and add header on first run. */
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
            if (needsHeader) {
                writer.write(header);
                writer.newLine();
            }
            writer.write(row);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.err.println("Failed to store metrics: " + e.getMessage());
        }
    }

    /**
     * Load all simulation runs that have been saved in the CSV file.
     * @return list of String arrays, one per run. 
     */
    public List<String[]> loadAllMetrics() {
        List<String[]> results = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            System.err.println("Metrics file not found: " + filePath);
            return results;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine(); // Skip header.

            /** Read each data row and split into columns. */
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 13) {
                    results.add(parts);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Failed to load metrics: " + e.getMessage());
        }

        return results;
    }

    /**
     * Given a run's date and time, load all metrics.
     * @param simName the date and time when the run was saved.
     * @return String array of the metrics.
     */
    public String[] loadMetrics(String simName) {
        List<String[]> all = loadAllMetrics();

        /** Search for the matching runName. */
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i)[0].equals(simName)) {
                return all.get(i);
            }
        }

        System.err.println("No metrics found for: " + simName);
        return null;
    }
    
    /**
     * Return column headers of the CSV file.
     * @return array of header strings.
     */
    public static String[] getHeaders() {
        return header.split(",");
    }

    /**
     * Return list of all simulation runs by their simName (date and time).
     * @return list of simulation run names.
     */
    public List<String> listStoredRuns() {
        List<String> names = new ArrayList<>();
        List<String[]> all = loadAllMetrics();

        for (int i = 0; i < all.size(); i++) {
            names.add(all.get(i)[0]);
        }

        return names;
    }
}
