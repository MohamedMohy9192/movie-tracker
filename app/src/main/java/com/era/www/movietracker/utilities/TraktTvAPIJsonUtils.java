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
    public static final String MOVIE_OVERVIEW_KEY = "overview";
    public static final String MOVIE_RELEASED_KEY = "released";
    public static final String MOVIE_TRAILER_KEY = "trailer";
    public static final String MOVIE_HOMEPAGE_KEY = "homepage";
    public static final String MOVIE_RATE_KEY = "rate";
    public static final String MOVIE_CERTIFICATION_KEY = "certification";

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
        int movieRank = 0;
        String overview = null;
        String released = null;
        String trailer = null;
        String homePage = null;
        int rate = 0;
        String certification = null;

        try {
            JSONArray boxOfficeMoviesArray = new JSONArray(boxOfficeJsonStr);

            for (int i = 0; i < boxOfficeMoviesArray.length(); i++) {

                JSONObject mainMovieObject = boxOfficeMoviesArray.getJSONObject(i);

                movieRank++;

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
                    if (supMovieObject.has(MOVIE_OVERVIEW_KEY)) {
                        overview = supMovieObject.optString(MOVIE_OVERVIEW_KEY);
                    }
                    if (supMovieObject.has(MOVIE_RELEASED_KEY)) {
                        released = supMovieObject.optString(MOVIE_RELEASED_KEY);
                    }
                    if (supMovieObject.has(MOVIE_TRAILER_KEY)) {
                        trailer = supMovieObject.optString(MOVIE_TRAILER_KEY);
                    }
                    if (supMovieObject.has(MOVIE_HOMEPAGE_KEY)) {
                        homePage = supMovieObject.optString(MOVIE_HOMEPAGE_KEY);
                    }
                    if (supMovieObject.has(MOVIE_RATE_KEY)) {
                        rate = supMovieObject.optInt(MOVIE_RATE_KEY);
                    }
                    if (supMovieObject.has(MOVIE_CERTIFICATION_KEY)) {
                        certification = supMovieObject.optString(MOVIE_CERTIFICATION_KEY);
                    }
                }

                Movie boxOfficeMovie = new Movie(
                        movieRevenue,
                        movieTitle,
                        movieYear,
                        movieRank,
                        movieTraktId,
                        overview,
                        released,
                        trailer,
                        homePage,
                        rate,
                        certification);
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
            movieContentValues.put(BoxOfficeEntry.COLUMN_MOVIE_TRAKT_ID, movie.getMovieTraktId());
            movieContentValues.put(BoxOfficeEntry.COLUMN_MOVIE_RANK, movie.getRank());
            movieContentValues.put(BoxOfficeEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
            movieContentValues.put(BoxOfficeEntry.COLUMN_MOVIE_RELEASED, movie.getReleased());
            movieContentValues.put(BoxOfficeEntry.COLUMN_MOVIE_TRAILER, movie.getTrailer());
            movieContentValues.put(BoxOfficeEntry.COLUMN_MOVIE_HOMEPAGE, movie.getHomePage());
            movieContentValues.put(BoxOfficeEntry.COLUMN_MOVIE_RATE, movie.getRate());
            movieContentValues.put(BoxOfficeEntry.COLUMN_MOVIE_CERTIFICATION, movie.getCertification());

            contentValuesArray[i] = movieContentValues;
        }
        return contentValuesArray;
    }
}
