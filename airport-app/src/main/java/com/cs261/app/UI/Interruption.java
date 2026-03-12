package com.cs261.app.UI;

public class Interruption 
{
    int[] time = new int[3];
    int duration;
    String status;

    /**
     * constructor for the Interruption class, which represents an interruption in the airport's operations. It takes in the time of the interruption, its duration, and its status (e.g., "runway closed", "weather delay", etc.).
     * @param time an array of three integers representing the day, hour, and minute of the interruption
     * @param duration an integer representing the duration of the interruption in minutes
     * @param status a string describing the status of the interruption (e.g., "runway closed", "weather delay", etc.)
     * 
     */
    public Interruption(int[] time, int duration, String status)
    {
        this.time = time;
        this.duration = duration;
        this.status = status;
    }
    /**
     * getter methods for the Interruption class, which allow access to the time, duration, and status of the interruption.
     * @return an array of three integers representing the day, hour, and minute of the interruption
     */
    public int[] getTime()
    {
        return this.time;
    }
    /**
     * returns the duration of the interruption in minutes.
     * @return an integer representing the duration of the interruption in minutes.
     */
    public int getDuration()
    {
        return this.duration;
    }
    /**
     * returns a string describing the status of the interruption (e.g., "runway closed", "weather delay", etc.).
     * @return a string describing the status of the interruption.
     */
    public String getStatus()
    {
        return this.status;
    }
    /**
     * debug method for the Interruption class.
     */
    public void print()
    {
        System.out.println("Interruption at day " + time[0] + ", hour " + time[1] + ", minute " + time[2] + " for duration " + duration + " with status: " + status);
    }
    
}
