package com.era.www.movietracker.detail;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.loader.app.LoaderManager;
import androidx.core.app.ShareCompat;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.era.www.movietracker.R;
import com.era.www.movietracker.data.MoviesContract.BoxOfficeEntry;
import com.era.www.movietracker.utilities.FormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    /*
     * The columns of data that we are interested in displaying within our DetailActivity's
     * movie display.
     */
    public static final String[] MOVIE_DETAIL_PROJECTION = {
            BoxOfficeEntry.COLUMN_MOVIE_RANK,
            BoxOfficeEntry.COLUMN_MOVIE_REVENUE,
            BoxOfficeEntry.COLUMN_MOVIE_TITLE,
            BoxOfficeEntry.COLUMN_MOVIE_OVERVIEW,
            BoxOfficeEntry.COLUMN_MOVIE_RELEASED,
            BoxOfficeEntry.COLUMN_MOVIE_TRAILER,
            BoxOfficeEntry.COLUMN_MOVIE_HOMEPAGE,
            BoxOfficeEntry.COLUMN_MOVIE_RATE,
            BoxOfficeEntry.COLUMN_MOVIE_CERTIFICATION};

    /*
        * We store the indices of the values in the array of Strings above to more quickly be able
        * to access the data from our query. If the order of the Strings above changes, these
        * indices must be adjusted to match the order of the Strings.
        */
    public static final int INDEX_MOVIE_RANK = 0;
    public static final int INDEX_MOVIE_REVENUE = 1;
    public static final int INDEX_MOVIE_TITLE = 2;
    public static final int INDEX_MOVIE_OVERVIEW = 3;
    public static final int INDEX_MOVIE_RELEASED = 4;
    public static final int INDEX_MOVIE_TRAILER = 5;
    public static final int INDEX_MOVIE_HOMEPAGE = 6;
    public static final int INDEX_MOVIE_RATE = 7;
    public static final int INDEX_MOVIE_CERTIFICATION = 8;

    private static final int DETAIL_LOADER_ID = 2;

    /* A summary of the movie that can be shared by clicking the share button in the ActionBar */
    private String mMovieSummary;

    @BindView(R.id.tv_rank)
    TextView mRankTextView;
    @BindView(R.id.tv_revenue)
    TextView mRevenueView;
    @BindView(R.id.tv_title)
    TextView mTitleTextView;
    @BindView(R.id.tv_overview)
    TextView mOverviewTextView;
    @BindView(R.id.tv_released)
    TextView mReleasedTextView;
    @BindView(R.id.tv_trailer)
    TextView mTrailerTextView;
    @BindView(R.id.tv_homepage)
    TextView mHomepageTextView;
    @BindView(R.id.tv_rate)
    TextView mRateTextView;
    @BindView(R.id.tv_certification)
    TextView mCertificationTextView;

    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mUri = getIntent().getData();
        if (mUri == null) throw new NullPointerException("URI for DetailActivity cannot be null");

        getSupportLoaderManager().initLoader(DETAIL_LOADER_ID, null, this);

    }

    @OnClick(R.id.fab)
    public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void createShareIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mMovieSummary)
                .createChooserIntent();

        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(shareIntent);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case DETAIL_LOADER_ID:
                return new CursorLoader(this,
                        mUri,
                        MOVIE_DETAIL_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
/*
         * Before we bind the data to the UI that will display that data, we need to check the
         * cursor to make sure we have the results that we are expecting. In order to do that, we
         * check to make sure the cursor is not null and then we call moveToFirst on the cursor.
         * Although it may not seem obvious at first, moveToFirst will return true if it contains
         * a valid first row of data.
         *
         * If we have valid data, we want to continue on to bind that data to the UI. If we don't
         * have any data to bind, we just return from this method.
         */
        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            /* We have valid data, continue on to bind the data to the UI */
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */
            return;
        }

        mRankTextView.setText(String.valueOf(data.getInt(INDEX_MOVIE_RANK)));
        mRevenueView.setText(FormatUtils.formatMovieRevenue(data.getInt(INDEX_MOVIE_REVENUE)));
        mTitleTextView.setText(data.getString(INDEX_MOVIE_TITLE));
        mOverviewTextView.setText(data.getString(INDEX_MOVIE_OVERVIEW));
        mReleasedTextView.setText(data.getString(INDEX_MOVIE_RELEASED));
        mTrailerTextView.setText(data.getString(INDEX_MOVIE_TRAILER));
        mHomepageTextView.setText(data.getString(INDEX_MOVIE_HOMEPAGE));
        mRateTextView.setText(data.getString(INDEX_MOVIE_RATE));
        mCertificationTextView.setText(data.getString(INDEX_MOVIE_CERTIFICATION));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menuId = item.getItemId();

        if (menuId == R.id.action_share) {
            createShareIntent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
