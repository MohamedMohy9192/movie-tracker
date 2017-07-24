package com.era.www.movietracker.movies;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.era.www.movietracker.R;
import com.era.www.movietracker.adapters.BoxOfficeAdapter;
import com.era.www.movietracker.adapters.BoxOfficeAdapter.BoxOfficeAdapterOnClickHandler;
import com.era.www.movietracker.detail.DetailActivity;
import com.era.www.movietracker.model.BoxOfficeMovie;
import com.era.www.movietracker.utilities.NetworkUtils;
import com.era.www.movietracker.utilities.TraktTvAPIJsonUtils;

import java.net.URL;
import java.util.List;

/**
 * SharedPreferences.OnSharedPreferenceChangeListener: Interface definition for a callback to be
 * invoked when a shared preference is changed.
 */
public class BoxOfficeFragment extends Fragment implements
        BoxOfficeAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<List<BoxOfficeMovie>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private final static String LOG_TAG = BoxOfficeFragment.class.getSimpleName();

    private static final String TRAKT_API_BOX_OFFICE_URL = "https://api.trakt.tv/movies/boxoffice";

    private static final int BOX_OFFICE_LOADER_ID = 0;

    private RecyclerView mRecyclerView;

    private BoxOfficeAdapter mBoxOfficeAdapter;

    private ProgressBar mLoadingIndicator;

    private TextView mErrorMessageTextView;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    public BoxOfficeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        /*
         * Ensures a loader is initialized and active. If the loader doesn't already exist,
         * created and (if fragment is currently started) starts the loader. Otherwise
         * the last created loader is re-used.
         */
        loadBoxOfficeData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_box_office, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(refreshListener());

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

        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         */
        mLoadingIndicator = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageTextView = (TextView) view.findViewById(R.id.tv_error_message);

        readFromSharedPreferences();
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
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<List<BoxOfficeMovie>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<BoxOfficeMovie>>(getActivity()) {

            /* This String array will hold and help cache data */
            List<BoxOfficeMovie> mBoxOfficeMovies;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if (mBoxOfficeMovies != null) {
                    deliverResult(mBoxOfficeMovies);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            /**
             * This is the method of the AsyncTaskLoader that will load and parse the JSON data
             * from TRAKT API in the background.
             *
             * @return List BoxOfficeMovie data from TRAKT API as an List of BoxOfficeMovie.
             *         null if an error occurs
             */
            @Override
            public List<BoxOfficeMovie> loadInBackground() {

                URL url = NetworkUtils.buildUrl(TRAKT_API_BOX_OFFICE_URL);

                try {
                    String result = NetworkUtils.getResponseFromHttpUrl(url);

                    List<BoxOfficeMovie> parsedBoxOfficeData = TraktTvAPIJsonUtils.BoxOfficeJsonStr(result);

                    return parsedBoxOfficeData;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            /**
             * Sends the result of the load to the registered listener.
             *
             * @param data The result of the load
             */
            @Override
            public void deliverResult(List<BoxOfficeMovie> data) {
                mBoxOfficeMovies = data;
                super.deliverResult(data);
            }
        };
    }

    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader        The Loader that has finished.
     * @param boxOfficeData The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<List<BoxOfficeMovie>> loader, List<BoxOfficeMovie> boxOfficeData) {

        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mBoxOfficeAdapter.setBoxOfficeData(boxOfficeData);

        if (boxOfficeData != null) {
            showResultDataView();
        } else {
            showErrorMessageView();
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
    public void onLoaderReset(Loader<List<BoxOfficeMovie>> loader) {

    }

    /**
     * This method is used when we are resetting data, so that at one point in time during a
     * refresh of our data, you can see that there is no data showing.
     */
    private void invalidateData() {
        mBoxOfficeAdapter.setBoxOfficeData(null);
        getActivity().getSupportLoaderManager().restartLoader(BOX_OFFICE_LOADER_ID, null, this);
    }

    /**
     * This method is overridden by our BoxOfficeFragment class in order to handle RecyclerView item
     * clicks.
     *
     * @param boxOfficeMovie The weather for the day that was clicked
     */
    @Override
    public void onClick(String boxOfficeMovie) {
        //lunch detail activity when recycler view item clicked

        Intent lunchDetailActivity = new Intent(getActivity(), DetailActivity.class);

        lunchDetailActivity.putExtra(Intent.EXTRA_TEXT, boxOfficeMovie);

        startActivity(lunchDetailActivity);
    }

    private void loadBoxOfficeData() {
        getActivity().getSupportLoaderManager().initLoader(BOX_OFFICE_LOADER_ID, null, this);
    }

    /**
     * This method will make the View for the weather data visible and
     * hide the error message.
     */
    private void showResultDataView() {

        // First, make sure the error is invisible
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        // Then, make sure the JSON data is visible
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the weather
     * View.
     */
    private void showErrorMessageView() {

        // First, hide the currently visible data
        mRecyclerView.setVisibility(View.INVISIBLE);
        // Then, show the error
        mErrorMessageTextView.setVisibility(View.VISIBLE);
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

    private void readFromSharedPreferences() {
        // Read from share preferences
        // SharedPreferences: Interface for accessing and modifying preference data.
        // PreferenceManager: Used to help create Preference hierarchies from activities or XML,
        // create preference hierarchies from XML via  addPreferencesFromResource(int).
        // getDefaultSharedPreferences: Gets a SharedPreferences instance  that points to the default
        // file that is used by the preference framework in the given context.
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        // Get data form preferences file, which get method to use depends on the type object stored in the file.
        // "show_revenue" :the string which is the key of the preference
        // true :Value to return if this preference does not exist.
        boolean showRevenuePref = sharedPreferences.getBoolean(
                getString(R.string.pref_show_revenue_key),
                getResources().getBoolean(R.bool.pref_show_revenue_default));
        // set the visibility of the revenue text view to true or false
        // depend on show_revenue check box user preference.
        mBoxOfficeAdapter.setShowRevenue(showRevenuePref);
        //set the rank text view text color depend on the string value return form list preference
        //pref_rank_text_color.
        String rankTextColorValue = sharedPreferences.getString(getString(R.string.pref_rank_text_color_key),
                getString(R.string.pref_color_black_value));
        mBoxOfficeAdapter.setRankTextColor(getActivity(), rankTextColorValue);

        //get the revenue_text_size edit text preference value and convert it to float
        // to set the revenue text size to what user choice.
        float revenueTextSize = Float.parseFloat(sharedPreferences.getString(
                getString(R.string.pref_revenue_text_size_key),
                getString(R.string.pref_revenue_text_size_default_value)));
        mBoxOfficeAdapter.setRevenueTextSize(revenueTextSize);


    }

    /**
     * Called when a shared preference is changed.
     *
     * @param sharedPreferences The SharedPreferences that received the change.
     * @param key               The key of the preference that was changed.
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //check what preference changed
        if (key.equals(getString(R.string.pref_show_revenue_key))) {
            boolean showRevenue = sharedPreferences.getBoolean(key,
                    getResources().getBoolean(R.bool.pref_show_revenue_default));
            mBoxOfficeAdapter.setShowRevenue(showRevenue);
        } else if (key.equals(getString(R.string.pref_rank_text_color_key))) {
            String s = sharedPreferences.getString(key, getString(R.string.pref_color_black_value));
            mBoxOfficeAdapter.setRankTextColor(getActivity(), s);
        } else if (key.equals(getString(R.string.pref_revenue_text_size_key))) {
            float revenueTextSize = Float.parseFloat(sharedPreferences.getString(
                    key,
                    getString(R.string.pref_revenue_text_size_default_value)));
            mBoxOfficeAdapter.setRevenueTextSize(revenueTextSize);
        }
    }
}
