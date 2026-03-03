package com.cs261.app.controller;

import com.cs261.app.model.ModelOutput;

/**
 * Holds the last simulation result so the KPI and results pages can display it.
 */
public final class ResultsContext {
    private static ModelOutput lastOutput;
    private static String durationText = "";

    public static void setLastResult(ModelOutput output, String duration) {
        lastOutput = output;
        durationText = duration != null ? duration : "";
    }

    public static ModelOutput getLastOutput() {
        return lastOutput;
    }

    public static String getDurationText() {
        return durationText;
    }
}
