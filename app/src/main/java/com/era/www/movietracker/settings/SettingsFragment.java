package com.era.www.movietracker.settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.era.www.movietracker.R;


/**
 * extends PreferenceFragmentCompat from v7 Preference Support Library.
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        //Inflates the preference_main XML resource file.
        addPreferencesFromResource(R.xml.preference_main);
    }
}
