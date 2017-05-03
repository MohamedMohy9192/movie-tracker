package com.era.www.movietracker.utilities;


import com.era.www.movietracker.model.BoxOfficeMovie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TraktTvAPIJsonUtils {

    public static List<BoxOfficeMovie> BoxOfficeJsonStr(String boxOfficeJsonStr) throws JSONException {

        final String MOVIE_INFO_KEY = "movie";

        final String MOVIE_TITLE_KEY = "title";

        final String MOVIE_REVENUE_KEY = "revenue";

        JSONArray boxOfficeMoviesArray = new JSONArray(boxOfficeJsonStr);

        List<BoxOfficeMovie> boxOfficeMovies = new ArrayList<>();

        byte rank = 1;

        for (int i = 0; i < boxOfficeMoviesArray.length(); i++) {

            JSONObject movieObject = boxOfficeMoviesArray.getJSONObject(i);

            int movieRevenue = movieObject.getInt(MOVIE_REVENUE_KEY);

            JSONObject movieInfo = movieObject.getJSONObject(MOVIE_INFO_KEY);

            String movieTitle = movieInfo.getString(MOVIE_TITLE_KEY);

            BoxOfficeMovie boxOfficeMovie = new BoxOfficeMovie(movieTitle, movieRevenue, rank++);

            boxOfficeMovies.add(boxOfficeMovie);
        }

        return boxOfficeMovies;
    }
}
