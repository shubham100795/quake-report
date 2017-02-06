package com.example.android.quakereport;

/**
 * Created by shubham arora on 08-01-2017.
 */


public class quake {

    private double magnitude;
    private String place;
    private long date;
    public static String quakeurl;

    public quake(double mag,String pla,long dat,String url)
    {
        magnitude=mag;
        place=pla;
        date=dat;
        quakeurl=url;
    }

    public double getmagnitude()
    {
        return magnitude;
    }
    public String getplace()
    {
        return place;
    }

    public long getdate()
    {
        return date;
    }

}
