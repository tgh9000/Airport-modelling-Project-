package com.cs261.app;

public class Interruption 
{
    int[] time = new int[3];
    int duration;
    String status;

    public Interruption(int[] time, int duration, String status)
    {
        this.time = time;
        this.duration = duration;
        this.status = status;
    }
    public int[] getTime()
    {
        return this.time;
    }
    public int getDuration()
    {
        return this.duration;
    }
    public String getStatus()
    {
        return this.status;
    }
    public void print()
    {
        System.out.println("Interruption at day " + time[0] + ", hour " + time[1] + ", minute " + time[2] + " for duration " + duration + " with status: " + status);
    }
    
}
