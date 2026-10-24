package com.cs261.app.perf;

import com.cs261.app.UI.ArrivalRow;
import com.cs261.app.UI.Globals;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

/*
Compares the time-stamp search used by the results tables (Globals.getArrivalsTableDataRange,
a TreeMap.subMap range query) against a linear scan of the same rows held in a flat list.
Each query asks for a one-hour window (60 ticks) at a random point in the run.
Run with: mvn test -Dbenchmarks
*/
@Tag("benchmark")
public class RangeQueryBenchmark {

    private static final int WINDOW_TICKS = 60;
    private static final int QUERIES = 2_000;
    private static final int ROWS_PER_TICK = 2; // roughly one arrival + one departure per simulated minute

    private record TimedRow(int tick, ArrivalRow row) {}

    private static long sink; // stops the JIT discarding query results

    @Test
    void rangeQuery_treeMapVsLinearScan() throws IOException {
        int[] tableSizes = {1_000, 10_000, 100_000, 1_000_000, 5_000_000};

        StringBuilder csv = new StringBuilder("rows,treeMapUsPerQuery,linearUsPerQuery,speedup\n");
        System.out.println("\n=== Time-stamp range query, 1-hour window (median us/query over " + QUERIES + " queries) ===");
        System.out.printf("%10s %16s %16s %10s%n", "rows", "TreeMap (us)", "linear (us)", "speedup");

        for (int rows : tableSizes) {
            int ticks = rows / ROWS_PER_TICK;
            TreeMap<Integer, List<ArrivalRow>> tree = new TreeMap<>();
            List<TimedRow> flat = new ArrayList<>(rows);
            for (int i = 0; i < rows; i++) {
                int tick = i / ROWS_PER_TICK;
                ArrivalRow r = new ArrivalRow("BAW" + i, "1", Integer.toString(tick), Integer.toString(tick), Integer.toString(tick + 2));
                tree.computeIfAbsent(tick, k -> new ArrayList<>()).add(r);
                flat.add(new TimedRow(tick, r));
            }
            Globals.arrivalsTree = tree;

            Random rnd = new Random(42);
            int[] starts = new int[QUERIES];
            for (int i = 0; i < QUERIES; i++) starts[i] = rnd.nextInt(Math.max(1, ticks - WINDOW_TICKS));

            // fewer linear queries at the largest sizes so the run stays practical
            int linearQueries = Math.max(50, Math.min(QUERIES, 20_000_000 / rows));

            // warm up both paths
            for (int i = 0; i < 500; i++) {
                treeQuery(starts[i % QUERIES]);
                linearQuery(flat, starts[i % QUERIES]);
            }

            // both approaches must return the same rows
            int s0 = starts[0];
            assertEquals(treeQuery(s0).size(), linearQuery(flat, s0).size());

            double treeUs = medianUs(starts, QUERIES, s -> treeQuery(s).size());
            double linUs = medianUs(starts, linearQueries, s -> linearQuery(flat, s).size());

            System.out.printf("%,10d %16.2f %16.2f %9.0fx%n", rows, treeUs, linUs, linUs / treeUs);
            csv.append(String.format("%d,%.3f,%.3f,%.1f%n", rows, treeUs, linUs, linUs / treeUs));
        }
        System.out.println("(sink " + sink + ")");
        SimulationSpeedBenchmark.write("range_query.csv", csv);
    }

    private static ObservableList<ArrivalRow> treeQuery(int start) {
        return Globals.getArrivalsTableDataRange(start, start + WINDOW_TICKS);
    }

    private static List<ArrivalRow> linearQuery(List<TimedRow> flat, int start) {
        int end = start + WINDOW_TICKS;
        List<ArrivalRow> out = new ArrayList<>();
        for (TimedRow t : flat) {
            if (t.tick() >= start && t.tick() < end) out.add(t.row());
        }
        return out;
    }

    private static double medianUs(int[] starts, int n, java.util.function.IntUnaryOperator query) {
        double[] us = new double[n];
        for (int i = 0; i < n; i++) {
            long t0 = System.nanoTime();
            sink += query.applyAsInt(starts[i]);
            us[i] = (System.nanoTime() - t0) / 1e3;
        }
        Arrays.sort(us);
        return us[n / 2];
    }
}
