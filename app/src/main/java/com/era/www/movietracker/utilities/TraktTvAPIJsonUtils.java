package com.era.www.movietracker.utilities;


import android.content.ContentValues;
import android.text.TextUtils;

import com.era.www.movietracker.data.MoviesContract.BoxOfficeEntry;
import com.era.www.movietracker.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TraktTvAPIJsonUtils {

    public static final String SUP_MOVIE_KEY = "movie";

    public static final String MOVIE_REVENUE_KEY = "revenue";
    public static final String MOVIE_TITLE_KEY = "title";
    public static final String MOVIE_YEAR_KEY = "year";

    public static final String MOVIE_IDS_KEY = "ids";
    public static final String TRAKT_ID_KEY = "trakt";

    public static List<Movie> extractBoxOfficeMovieFromJson(String boxOfficeJsonStr) {

        if (TextUtils.isEmpty(boxOfficeJsonStr)) {
            return null;
        }
        List<Movie> boxOfficeMovieList = new ArrayList<>();

        int movieRevenue = 0;
        String movieTitle = null;
        int movieYear = 0;
        int movieTraktId = 0;
        int movieRank = 1;

        try {
            JSONArray boxOfficeMoviesArray = new JSONArray(boxOfficeJsonStr);

            for (int i = 0; i < boxOfficeMoviesArray.length(); i++) {

                JSONObject mainMovieObject = boxOfficeMoviesArray.getJSONObject(i);

                movieRank = movieRank + 1;

                if (mainMovieObject.has(MOVIE_REVENUE_KEY)) {
                    movieRevenue = mainMovieObject.optInt(MOVIE_REVENUE_KEY);
                }

                if (mainMovieObject.has(SUP_MOVIE_KEY)) {
                    JSONObject supMovieObject = mainMovieObject.getJSONObject(SUP_MOVIE_KEY);

                    if (supMovieObject.has(MOVIE_TITLE_KEY)) {
                        movieTitle = supMovieObject.optString(MOVIE_TITLE_KEY);
                    }
                    if (supMovieObject.has(MOVIE_YEAR_KEY)) {
                        movieYear = mainMovieObject.optInt(MOVIE_YEAR_KEY);
                    }

                    if (supMovieObject.has(MOVIE_IDS_KEY)) {
                        JSONObject movieIdsObject = supMovieObject.optJSONObject(MOVIE_IDS_KEY);

                        if (movieIdsObject.has(TRAKT_ID_KEY)) {
                            movieTraktId = movieIdsObject.optInt(TRAKT_ID_KEY);
                        }
                    }
                }
                Movie boxOfficeMovie =
                        new Movie(movieRevenue, movieTitle, movieYear, movieRank, movieTraktId);
                boxOfficeMovieList.add(boxOfficeMovie);
            }
            return boxOfficeMovieList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * covert movie list to contentValues array contain movie data.
     *
     * @param movieList movie list contain extracted  movies data from the json response.
     * @return array of contentValues contain movies data to insert into database.
     */
    public static ContentValues[] geContentValuesArrayFromMovieList(List<Movie> movieList) {
        if (movieList == null) {
            return null;
        }
        ContentValues[] contentValuesArray = new ContentValues[movieList.size()];

        for (int i = 0; i < movieList.size(); i++) {
            Movie movie = movieList.get(i);

            ContentValues movieContentValues = new ContentValues();
            movieContentValues.put(BoxOfficeEntry.COLUMN_MOVIE_REVENUE, movie.getRevenue());
            movieContentValues.put(BoxOfficeEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
            movieContentValues.put(BoxOfficeEntry.COLUMN_MOVIE_YEAR, movie.getYear());
            movieContentValues.put(BoxOfficeEntry.COLUMN_TRAKT_ID, movie.getMovieTraktId());
            movieContentValues.put(BoxOfficeEntry.COLUMN_MOVIE_RANK, movie.getRank());

            contentValuesArray[i] = movieContentValues;
        }
        return contentValuesArray;
    }
}
