package com.era.www.movietracker.movies;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
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

public class BoxOfficeFragment extends Fragment implements
        BoxOfficeAdapterOnClickHandler, LoaderManager.LoaderCallbacks<List<BoxOfficeMovie>> {

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

        return view;
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     *
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
     * @param loader The Loader that has finished.
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
}
