package com.era.www.movietracker.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import android.widget.Toast;

import com.era.www.movietracker.R;


/**
 * extends PreferenceFragmentCompat from v7 Preference Support Library.
 * SharedPreferenceChangeListener is triggered after any value is saved to the SharedPreferences file.
 * PreferenceChangeListener is triggered before a value is saved to the SharedPreferences file.
 * Because of this, it can prevent an invalid update to a preference. PreferenceChangeListeners
 * are also attached to a single preference.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {
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
            if (!(preference instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, value);
            }

        }
        // Add the preference listener which checks that the size is correct to the size preference
        Preference preference = findPreference(getString(R.string.pref_revenue_text_size_key));
        preference.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //finding the preference by it's key.
        Preference preference = findPreference(key);
        if (preference != null) {
            if (!(preference instanceof CheckBoxPreference)) {
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
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            //find the index of the current value of list preference which was selected.
            int prefIndex = listPreference.findIndexOfValue(value);
            //check if the index is valid.
            if (prefIndex >= 0) {
                // getting the array  of the labels and pass in the index of the current value.
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
            // For other preferences, set the summary to the value's simple string representation.
        } else {
            preference.setSummary(value);
        }
    }

    // we're using the onPreferenceChange listener for checking whether the
    // size setting was set to a valid value.
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Toast error = Toast.makeText(getContext(), "Please select a number between 18 and 22",
                Toast.LENGTH_LONG);
        // Get the key of revenue_text_size preference to check that
        // the preference is the size preference.
        String revenueSize = getString(R.string.pref_revenue_text_size_key);
        // if the preference is revenue_text_size cast the newValue to string.
        if (preference.getKey().equals(revenueSize)) {
            String stringSize = ((String) newValue).trim();

            if (stringSize.equals("")) stringSize = "18";
            try {
                float size = Float.parseFloat(stringSize);
                // If the number is outside of the acceptable range, show an error.
                if (size > 22 || size < 18) {
                    error.show();
                    return false;
                }
            } catch (NumberFormatException e) {
                // If whatever the user entered can't be parsed to a number, show an error
                error.show();
                return false;
            }
        }

        return true;
    }

    @Override
    public void onStop() {
        super.onStop();
        /* Unregister the preference change listener */
        getPreferenceManager().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
         /* Register the preference change listener */
        getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }
}
