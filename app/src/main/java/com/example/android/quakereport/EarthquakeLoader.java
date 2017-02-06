package com.example.android.quakereport;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by shubham arora on 13-01-2017.
 */


public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<quake>> {

    private static final String LOG_TAG = EarthquakeLoader.class.getName();
    private String mUrl;

    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<quake> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        Log.i(LOG_TAG,"on loadinbackground method");

        // Perform the network request, parse the response, and extract a list of earthquakes.
        ArrayList<quake> earthquake = QueryUtils.fetchEarthquakeData(mUrl);
        return earthquake;

    }


}

