package com.era.www.movietracker.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import com.era.www.movietracker.R;


/**
 * extends PreferenceFragmentCompat from v7 Preference Support Library.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        //Inflates the preference_main XML resource file.
        addPreferencesFromResource(R.xml.preference_main);

        // make the settings fragment update the summary of the rank text color list preference programmatically.
        // getPreferenceManger Returns the PreferenceManager used by this fragment.
        // PreferenceManager Used to help create Preference hierarchies from activities or XML,
        // create preference hierarchies from XML via addPreferencesFromResource(int).
        // getSharedPreferences Returns a SharedPreferences instance pointing to the file that
        // contain the values of preferences that are managed by this PreferenceManager.
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        // getPreferenceScreen Gets the root of the preference hierarchy that this fragment is showing.
        // get a reference to the PreferenceScreen so that i can get a count of the total numbers of
        // preferences.
        PreferenceScreen prefScreen = getPreferenceScreen();
        int count = prefScreen.getPreferenceCount();
        //iterate through all of my preferences.
        for (int i = 0; i < count; i++) {
            Preference preference = prefScreen.getPreference(i);
            //check if it's not a check box preference.
            // i intentionally excluding the CheckBoxPreference because i changed the check box summary
            // by using the on summary and off summary xml attribute also call getString would
            // cause runtime error because for the CheckBoxPreference, the value type is boolean.
            if (!(preference instanceof CheckBoxPreference)){
                String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, value);
            }

        }
    }

    // helper method to set the correct preference summary.
    // for setting the correct summary for a list preference we can't just use the key or the value string.
    // in the summary we want to display the current lable because if we want to add another
    // language to the app the labels will translated but the values will not.
    private void setPreferenceSummary(Preference preference, String value) {
        // check if the preference is an instance of the list preference.
        if (preference instanceof ListPreference){
            ListPreference listPreference = (ListPreference) preference;
            //find the index of the current value of list preference which was selected.
            int prefIndex = listPreference.findIndexOfValue(value);
            //check if the index is valid.
            if (prefIndex >= 0){
                // getting the array  of the labels and pass in the index of the current value.
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //finding the preference by it's key.
        Preference preference = findPreference(key);
        if (preference != null){
            if (!(preference instanceof CheckBoxPreference)){
                String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, value);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceManager().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
