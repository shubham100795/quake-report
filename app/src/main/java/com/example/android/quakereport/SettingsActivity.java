package com.example.android.quakereport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class EarthquakePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener  {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);
            Preference minMagnitude = findPreference(getString(R.string.settings_min_magnitude_key));//find prefrence min_magnitude
            bindPreferenceSummaryToValue(minMagnitude);//bind its value to it

            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderBy);
        }
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);//Returns the index of the given value (in the entry values array).
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();//The list of entries to be shown in the list in subsequent dialogs.
                    preference.setSummary(labels[prefIndex]);//labels array k us index wale element ko show karna
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);//also set onprefrence change listener on this prefrence
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());//get the default stored value of this prfrence
            String preferenceString = preferences.getString(preference.getKey(), "");//get key name(min_magnitude)
            onPreferenceChange(preference, preferenceString);//set summary
        }

    }
}