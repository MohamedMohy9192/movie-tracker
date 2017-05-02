package com.era.www.movietracker;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.era.www.movietracker.BoxOfficeAdapter.BoxOfficeAdapterOnClickHandler;
import com.era.www.movietracker.utilities.NetworkUtils;
import com.era.www.movietracker.utilities.TraktTvAPIJsonUtils;

import java.net.URL;

public class BoxOfficeFragment extends Fragment implements BoxOfficeAdapterOnClickHandler {

    private final static String LOG_TAG = BoxOfficeFragment.class.getSimpleName();

    private static final String TRAKT_API_BOX_OFFICE_URL = "https://api.trakt.tv/movies/boxoffice";

    private RecyclerView mRecyclerView;

    private BoxOfficeAdapter mBoxOfficeAdapter;

    private ProgressBar mLoadingIndicator;

    private TextView mErrorMessageTextView;

    private Toast mToast;

    public BoxOfficeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        loadBoxOfficeData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_box_office, container, false);

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



    private void showResultData() {

        // First, make sure the error is invisible
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        // Then, make sure the JSON data is visible
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {

        // First, hide the currently visible data
        mRecyclerView.setVisibility(View.INVISIBLE);
        // Then, show the error
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

    private void loadBoxOfficeData() {

        mBoxOfficeAdapter.setBoxOfficeData(null);
        showResultData();
        new BoxOfficeAsyncTask().execute(TRAKT_API_BOX_OFFICE_URL);
    }

    /**
     * This method is overridden by our BoxOfficeFragment class in order to handle RecyclerView item
     * clicks.
     *
     * @param boxOfficeMovie The weather for the day that was clicked
     */
    @Override
    public void onClick(String boxOfficeMovie) {

        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(getActivity(), boxOfficeMovie, Toast.LENGTH_LONG);

        mToast.show();
    }

    private class BoxOfficeAsyncTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... urls) {

            /* If there's no url, there's nothing to look up. */
            if (urls.length == 0) {
                return null;
            }

            URL url = NetworkUtils.buildUrl(urls[0]);

            try {
                String result = NetworkUtils.getResponseFromHttpUrl(url);

                String[] parsedBoxOfficeData = TraktTvAPIJsonUtils.BoxOfficeJsonStr(result);

                return parsedBoxOfficeData;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] boxOfficeData) {

            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (boxOfficeData != null) {

                showResultData();
                mBoxOfficeAdapter.setBoxOfficeData(boxOfficeData);

            } else {
                showErrorMessage();
            }
        }
    }
}
