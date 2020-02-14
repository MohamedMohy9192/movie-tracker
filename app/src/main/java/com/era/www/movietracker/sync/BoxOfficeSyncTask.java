package com.era.www.movietracker.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.era.www.movietracker.data.MoviesContract;
import com.era.www.movietracker.model.Movie;
import com.era.www.movietracker.utilities.NetworkUtils;
import com.era.www.movietracker.utilities.NotificationUtils;
import com.era.www.movietracker.utilities.TraktTvAPIJsonUtils;

import java.net.URL;
import java.util.List;

public class BoxOfficeSyncTask {

    private static final String TRAKT_API_BOX_OFFICE_URL = "https://api.trakt.tv/movies/boxoffice";

    /**
     * Performs the network request for updated BoxOfficeMovie, parses the JSON from that request, and
     * inserts the new Movies information into our ContentProvider. Will notify the user that new
     * BoxOffice Movies has been loaded if the user hasn't been notified
     * and they haven't disabled notifications in the preferences screen.
     *
     * @param context Used to access utility methods and the ContentResolver
     */
    synchronized public static void syncBoxOfficeMovie(Context context) {

        URL boxOfficeRequestUrl = NetworkUtils.buildUrl(TRAKT_API_BOX_OFFICE_URL);
        if (boxOfficeRequestUrl != null) {

            String boxOfficeJsonResponse = NetworkUtils.getResponseFromHttpUrl(boxOfficeRequestUrl);
            if (boxOfficeJsonResponse != null) {

                List<Movie> boxOfficeMovies =
                        TraktTvAPIJsonUtils.extractBoxOfficeMovieFromJson(boxOfficeJsonResponse);


                if (boxOfficeMovies != null && boxOfficeMovies.size() != 0) {
                    ContentValues[] movieContentValues =
                            TraktTvAPIJsonUtils.geContentValuesArrayFromMovieList(boxOfficeMovies);

                    /* Get a handle on the ContentResolver to delete and insert data */
                    ContentResolver movieTrackerContentResolver = context.getContentResolver();

                    movieTrackerContentResolver.delete(MoviesContract.BoxOfficeEntry.CONTENT_URI,
                            null, null);

                    movieTrackerContentResolver.bulkInsert(MoviesContract.BoxOfficeEntry.CONTENT_URI,
                            movieContentValues);


                }
            }
        }

        NotificationUtils.notifyUserOfNewBoxOffice(context);

    }
}
