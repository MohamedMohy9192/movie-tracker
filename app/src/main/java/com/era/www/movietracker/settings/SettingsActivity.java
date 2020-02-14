package com.era.www.movietracker.settings;

import androidx.fragment.app.FragmentManager;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.era.www.movietracker.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.frame_pref, new SettingsFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        // Respond to the action bar's Up/Home button
        if (itemId == android.R.id.home) {
            // NavUtils provides helper functionality for applications implementing
            // recommended Android UI navigation patterns.
            // navigateUpFromSameTask(), it finishes the current activity and starts
            // (or resumes) the appropriate parent activity
            // If the parent activity has launch mode <singleTop>, the parent activity is brought
            // to the top (the existing instance of MainActivity) of the stack, and receives
            // the intent through its onNewIntent() method.
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
