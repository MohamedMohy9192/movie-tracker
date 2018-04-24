package com.era.www.movietracker.movies;


import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.era.www.movietracker.R;
import com.era.www.movietracker.adapters.BoxOfficeAdapter;
import com.era.www.movietracker.adapters.BoxOfficeAdapter.BoxOfficeAdapterOnClickHandler;
import com.era.www.movietracker.data.MoviesContract.BoxOfficeEntry;
import com.era.www.movietracker.detail.DetailActivity;

/**
 * SharedPreferences.OnSharedPreferenceChangeListener: Interface definition for a callback to be
 * invoked when a shared preference is changed.
 */
public class BoxOfficeFragment extends Fragment implements
        BoxOfficeAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private final static String LOG_TAG = BoxOfficeFragment.class.getSimpleName();

    //The columns of data that we are interested in displaying within our BoxOfficeFragment's list of
    //box office data.
    private static final String[] MAIN_BOX_OFFICE_PROJECTION = {
            BoxOfficeEntry.COLUMN_MOVIE_REVENUE,
            BoxOfficeEntry.COLUMN_MOVIE_TITLE,
            BoxOfficeEntry.COLUMN_MOVIE_RANK,
            BoxOfficeEntry.COLUMN_MOVIE_TRAKT_ID};

    /*
     * We store the indices of the values in the array of Strings above to more quickly be able to
     * access the data from our query. If the order of the Strings above changes, these indices
     * must be adjusted to match the order of the Strings.
     */
    public static final int INDEX_MOVIE_REVENUE = 0;
    public static final int INDEX_MOVIE_TITLE = 1;
    public static final int INDEX_MOVIE_RANK = 2;
    public static final int INDEX_MOVIE_TRAKT_ID = 3;

    private static final String TRAKT_API_BOX_OFFICE_URL = "https://api.trakt.tv/movies/boxoffice";

    private static final int BOX_OFFICE_LOADER_ID = 0;

    private RecyclerView mRecyclerView;

    private BoxOfficeAdapter mBoxOfficeAdapter;

    private ProgressBar mLoadingIndicator;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    public BoxOfficeFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        /*
//         * Ensures a loader is initialized and active. If the loader doesn't already exist,
//         * created and (if fragment is currently started) starts the loader. Otherwise
//         * the last created loader is re-used.
//         */
//        loadBoxOfficeData();
//
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*
         * Ensures a loader is initialized and active. If the loader doesn't already exist,
         * created and starts the loader. Otherwise
         * the last created loader is re-used.
         * You typically initialize a Loader within the activity's onCreate() method,
         * or within the fragment's onActivityCreated() method.
         */
        loadBoxOfficeData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_box_office, container, false);

        //fake data.
        ContentValues contentValues = new ContentValues();
        contentValues.put(BoxOfficeEntry.COLUMN_MOVIE_TRAKT_ID, 112);
        contentValues.put(BoxOfficeEntry.COLUMN_MOVIE_REVENUE, 1121212121);
        contentValues.put(BoxOfficeEntry.COLUMN_MOVIE_TITLE, "Sex");
        contentValues.put(BoxOfficeEntry.COLUMN_MOVIE_RANK, 1);
        contentValues.put(BoxOfficeEntry.COLUMN_MOVIE_YEAR, 2018);
        contentValues.put(BoxOfficeEntry.COLUMN_MOVIE_OVERVIEW, "Very hot.");
        contentValues.put(BoxOfficeEntry.COLUMN_MOVIE_RELEASED, "23-84-2018");
        contentValues.put(BoxOfficeEntry.COLUMN_MOVIE_TRAILER, "vk.com");
        contentValues.put(BoxOfficeEntry.COLUMN_MOVIE_HOMEPAGE, "vk.com/sex");
        contentValues.put(BoxOfficeEntry.COLUMN_MOVIE_RATE, 9.244);
        contentValues.put(BoxOfficeEntry.COLUMN_MOVIE_CERTIFICATION, "R");

        getActivity().getContentResolver().insert(BoxOfficeEntry.CONTENT_URI, contentValues);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(refreshListener());

        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         */
        mLoadingIndicator = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);
        /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_box_office);

        /*
         * LinearLayoutManager can support HORIZONTAL or VERTICAL orientations. The reverse layout
         * parameter is useful mostly for HORIZONTAL layouts that should reverse for right to left
         * languages.
         */
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);

        /*
         * The BoxOfficeAdapter is responsible for linking our box office data with the Views that
         * will end up displaying our weather data.
         */
        mBoxOfficeAdapter = new BoxOfficeAdapter(this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mBoxOfficeAdapter);

        showLoading();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Registers the listener callback to be invoked when a change happens to a preference.
        // Registers the listener in onCreate when the fragment's is created
        // BoxOfficeFragment implement SharedPreferences.OnSharedPreferenceChangeListener
        // which i can pass this.
        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregisters OnPreferenceChangedListener callback to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param loaderId The ID whose loader is to be created.
     * @param args     Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case BOX_OFFICE_LOADER_ID:
                Uri boxOfficeQueryUri = BoxOfficeEntry.CONTENT_URI;

                return new CursorLoader(getActivity(),
                        boxOfficeQueryUri,
                        MAIN_BOX_OFFICE_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader        The Loader that has finished.
     * @param boxOfficeData The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor boxOfficeData) {

        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mBoxOfficeAdapter.swapCursor(boxOfficeData);

        if (boxOfficeData != null && boxOfficeData.getCount() != 0) {
            showMoviesDataView();
        }
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Of course, unless you use destroyLoader(),
        // this is called when everything is already dying
        // so a completely empty onLoaderReset() is
        // totally acceptable
        mBoxOfficeAdapter.swapCursor(null);
    }

    /**
     * This method is used when we are resetting data, so that at one point in time during a
     * refresh of our data, you can see that there is no data showing.
     */
    private void invalidateData() {
        mBoxOfficeAdapter.swapCursor(null);
        getActivity().getSupportLoaderManager().restartLoader(BOX_OFFICE_LOADER_ID, null, this);
    }

    /**
     * This method is overridden by our BoxOfficeFragment class in order to handle RecyclerView item
     * clicks.
     *
     * @param movieTraktId The id for the movie that was clicked
     */
    @Override
    public void onClick(int movieTraktId) {
        //lunch detail activity when recycler view item clicked

        Intent lunchDetailActivity = new Intent(getActivity(), DetailActivity.class);
        Uri movieUri = BoxOfficeEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movieTraktId)).build();
        lunchDetailActivity.setData(movieUri);


        startActivity(lunchDetailActivity);
    }

    private void loadBoxOfficeData() {
        getActivity().getSupportLoaderManager().initLoader(BOX_OFFICE_LOADER_ID, null, this);
    }

    /**
     * This method will make the View for the weather data visible and
     * hide the error message.
     */
    private void showMoviesDataView() {

        /* First, hide the loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        // Finally, make sure the box office movie data is visible
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the loading indicator visible and hide the weather View.
     */
    private void showLoading() {
        // First, hide the weather data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        // Then, show the loading indicator
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private SwipeRefreshLayout.OnRefreshListener refreshListener() {

        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                invalidateData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        };
    }

    /**
     * Called when a shared preference is changed.
     *
     * @param sharedPreferences The SharedPreferences that received the change.
     * @param key               The key of the preference that was changed.
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
