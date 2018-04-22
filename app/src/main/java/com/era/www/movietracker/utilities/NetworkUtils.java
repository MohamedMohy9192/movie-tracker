package com.era.www.movietracker.utilities;

import android.util.Log;

import com.era.www.movietracker.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class NetworkUtils {

    private final static String LOG_TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIE_API_KEY = BuildConfig.TRAKT_API_KEY;

    public static URL buildUrl(String requestUrl) {

        URL url;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
            return null;
        }
        return url;
    }

    /**
     * this method return the whole result from the HTTP response.
     *
     * @param url The URL object to fetch the response from.
     * @return The content of HTTP response.
     * @throws IOException relate to network and stream reading.
     */
    public static String getResponseFromHttpUrl(URL url) {

        if (url == null) {
            return null;
        }
        // Open connection on URL object.
        HttpURLConnection urlConnection = null;
        InputStream in = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("trakt-api-key", MOVIE_API_KEY);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get InputStream form the connection.
                in = urlConnection.getInputStream();
                // Use scanner to reade the contents of InputStream
                // and buffer the data as needed it also handle the character encoding.
                Scanner scanner = new Scanner(in);
                // Set the delimiter to \A which matches the beginning of InputSteam to
                // force Scanner to read the entire contents
                // from beginning to next beginning.
                scanner.useDelimiter("\\A");
                // Check if Scanner has another contents InputStream to read.
                boolean input = scanner.hasNext();
                // If true Finds and returns the next contents of InputStream.
                // If else return null.
                if (input) {
                    return scanner.next();
                } else {
                    return null;
                }
            } else {
                Log.d(LOG_TAG, "HttpURLConnection error response code: " + urlConnection.getResponseCode());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                // close thee connection.
                urlConnection.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
