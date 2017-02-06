/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.android.quakereport.QueryUtils.LOG_TAG;
import static com.example.android.quakereport.quake.quakeurl;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<quake>> {

    private static final String USGS_REQUEST_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query";
    private static final int EARTHQUAKE_LOADER_ID = 1;
    quakeAdapter mAdapter;
    TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        ListView lv = (ListView) findViewById(R.id.list);
        lv.setEmptyView(mEmptyStateTextView);
        mAdapter = new quakeAdapter(this, new ArrayList<quake>());
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null) {

            getSupportLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }
        else
        {
            ProgressBar spin=(ProgressBar)findViewById(R.id.loading_spinner);
            spin.setVisibility(View.GONE);
            mEmptyStateTextView.setText("NO INTERNET CONNECTION");
        }

    }

    @Override//to restart the same process after the user turns on the network connection
    protected void onPostResume() {
        super.onPostResume();
        setContentView(R.layout.earthquake_activity);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        ListView lv = (ListView) findViewById(R.id.list);
        lv.setEmptyView(mEmptyStateTextView);
        mAdapter = new quakeAdapter(this, new ArrayList<quake>());
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null) {

            getSupportLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }
        else
        {
            ProgressBar spin=(ProgressBar)findViewById(R.id.loading_spinner);
            spin.setVisibility(View.GONE);
            mEmptyStateTextView.setText("NO INTERNET CONNECTION");
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<ArrayList<quake>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));//getString (String key,String defValue)..return the value of key (min_magnitude)elae return default(6)
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );


        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "20");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeLoader(this, uriBuilder.toString());
    }


    @Override
    public void onLoadFinished(Loader<ArrayList<quake>> loader, ArrayList<quake> earthquake) {
        mAdapter.clear();

        ProgressBar spin=(ProgressBar)findViewById(R.id.loading_spinner);
        spin.setVisibility(View.GONE);
        mEmptyStateTextView.setText("NO EARTHQUAKES FOUND");
        if (earthquake != null && !earthquake.isEmpty()) {
            ListView lv = (ListView) findViewById(R.id.list);
            mAdapter.addAll(earthquake);
            lv.setAdapter(mAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Uri webpage = Uri.parse(quakeurl);
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            });
        }
        Log.i(LOG_TAG,"on load finished method");
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<quake>> loader) {
        mAdapter.clear();
    }
}