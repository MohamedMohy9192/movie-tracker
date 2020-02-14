package com.era.www.movietracker;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.era.www.movietracker.movies.MoviesFragment;
import com.era.www.movietracker.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "tag";
    private DrawerLayout drawerLayout;

    // index to identify current nav menu item
    private int navItemIndex = 0;

    private String[] activityTitles;

    private ActionBarDrawerToggle mActionBarDrawerToggle;

    private Toolbar toolbar;

    private NavigationView navigationView;


    private static final String TAG_MOVIES = "movies_fragment";
    private static final String TAG_TV_FRAGMENT = "tv_fragment";
    private static final String TAG_PEOPLE_FRAGMENT = "people_fragment";
    private static String CURRENT_TAG = TAG_MOVIES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(mActionBarDrawerToggle);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Gets the header view at the specified position.
        View navHeader = navigationView.getHeaderView(0);
        loadNavHeaderInfo(navHeader);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_MOVIES;
            loadHomeFragment();
        }
    }

    private ActionBarDrawerToggle setupDrawerToggle() {

        return mActionBarDrawerToggle;
    }



    private void loadNavHeaderInfo(View navHeader) {
        ImageView profileImage = (ImageView) navHeader.findViewById(R.id.header_profile_image);
        TextView userName = (TextView) navHeader.findViewById(R.id.nav_header_name);
        userName.setText("Mohamed mohamed");
        TextView userEmail = (TextView) navHeader.findViewById(R.id.nav_header_email);
        userEmail.setText("mohamed_mohamed@yahoo.com");
    }

    private void loadHomeFragment() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
        // load toolbar titles from string resources
//        String[] activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        // set toolbar title
//        setTitle(activityTitles[navItemIndex]);

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawerLayout.closeDrawers();
        }

        Fragment fragment = getSelectedFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_content, fragment, CURRENT_TAG)
                .commit();

        drawerLayout.closeDrawers();


    }

    private Fragment getSelectedFragment() {
        switch (navItemIndex) {
            case 0:
                return new MoviesFragment();
            case 1:
                return new TvShowFragment();
            case 2:
                return new PeopleFragment();
            default:
                return new MoviesFragment();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        }

        if (navItemIndex != 0) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_MOVIES;
            loadHomeFragment();
            return;
        }

        super.onBackPressed();

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_movies:
                navItemIndex = 0;
                CURRENT_TAG = TAG_MOVIES;
                break;
            case R.id.nav_tv_show:
                navItemIndex = 1;
                CURRENT_TAG = TAG_TV_FRAGMENT;
                break;
            case R.id.nav_people:
                navItemIndex = 2;
                CURRENT_TAG = TAG_PEOPLE_FRAGMENT;
                break;
            case R.id.nav_settings:
                Intent openSettingsActivity = new Intent(
                        MainActivity.this,
                        SettingsActivity.class);
                startActivity(openSettingsActivity);
                drawerLayout.closeDrawers();
                return true;
            default:
                navItemIndex = 0;
                CURRENT_TAG = TAG_MOVIES;
        }
        item.setChecked(true);
        loadHomeFragment();
        drawerLayout.closeDrawers();
        return true;
    }
}
