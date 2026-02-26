package com.cs261.app;
import java.util.List;
import java.util.ArrayList;


public class Runway 
{
    float length;
    float bearing;
    String operating;
    List<Interruption> interruptions = new ArrayList<Interruption>();

    public Runway(float length, float bearing, String operating)
    {
        this.length = length;
        this.bearing = bearing;
        this.operating = operating;
    }

    public void addInterruption(Interruption i)
    {
        this.interruptions.add(i);
    }
    public List<Interruption> getInterruptions()
    {
        return this.interruptions;
    }

    public Float getLength()
    {
        return this.length;
    }
    public float getBearing()
    {
        return this.bearing;
    }
    public String getOperating()
    {
        return this.operating;
    }





}
