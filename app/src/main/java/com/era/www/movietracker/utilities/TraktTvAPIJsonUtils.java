package com.era.www.movietracker.utilities;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TraktTvAPIJsonUtils {

    public static String[] BoxOfficeJsonStr(String boxOfficeJsonStr) throws JSONException {

        final String MOVIE_INFO_KEY = "movie";

        final String MOVIE_TITLE_KEY = "title";

        final String MOVIE_YEAR_KEY = "year";

        final String MOVIE_REVENUE_KEY = "revenue";

        JSONArray boxOfficeMoviesArray = new JSONArray(boxOfficeJsonStr);


        String[] parsedBoxOfficeData = new String[boxOfficeMoviesArray.length()];

        byte rank = 1;

        for (int i = 0; i < boxOfficeMoviesArray.length(); i++) {

            JSONObject movieObject = boxOfficeMoviesArray.getJSONObject(i);

            int movieRevenue = movieObject.getInt(MOVIE_REVENUE_KEY);

            double douRevenue = movieRevenue / 1000000.0;

            String s = "$" + douRevenue + "M";

            JSONObject movieInfo = movieObject.getJSONObject(MOVIE_INFO_KEY);

            String movieTitle = movieInfo.getString(MOVIE_TITLE_KEY);

            int movieYear = movieInfo.getInt(MOVIE_YEAR_KEY);

            parsedBoxOfficeData[i] = rank++ + " - " + movieTitle + " - " + movieYear + " - " + s;

        }

        return parsedBoxOfficeData;
    }
}
