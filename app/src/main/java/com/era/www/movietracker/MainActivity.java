package com.era.www.movietracker;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.era.www.movietracker.movies.MoviesFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "tag";
    private DrawerLayout drawerLayout;

    // index to identify current nav menu item
    private int navItemIndex = 0;

    private String[] activityTitles;

    private ActionBarDrawerToggle drawerToggle;

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
        drawerToggle = setupDrawerToggle();

        drawerLayout.addDrawerListener(drawerToggle);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setUpNavigationView(navigationView);


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
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        return drawerToggle;
    }

    private void setUpNavigationView(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
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
                            default:
                                navItemIndex = 0;
                                CURRENT_TAG = TAG_MOVIES;
                        }
                        item.setChecked(true);
                        loadHomeFragment();
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
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
        }else {

            super.onBackPressed();
        }

//        if (navItemIndex != 0) {
//            navItemIndex = 0;
//            CURRENT_TAG = TAG_MOVIES;
//            loadHomeFragment();
//            return;
//        }


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
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
}
