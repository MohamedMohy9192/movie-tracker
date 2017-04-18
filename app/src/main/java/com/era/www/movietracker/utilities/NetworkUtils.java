package com.era.www.movietracker.utilities;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class NetworkUtils {

    private static final String BOX_OFFICE_URL = "\\fgfgfgf n";

    private static final String CLIENT_ID =
            "6fae043eb46bee348d4fc1ef2d2e9f46f6eceaa8dcd9eb99a90d5d8353b724bf";

    private final static String LOG_TAG = NetworkUtils.class.getSimpleName();

    public static URL buildUrl(String requestUrl) {

        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }


    public static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("trakt-api-version", "2");
            urlConnection.setRequestProperty("trakt-api-key", CLIENT_ID);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the json results", e);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader streamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(streamReader);

            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * this method return the whole result from the HTTP response.
     *
     * @param url The URL object to fetch the response from.
     * @return The content of HTTP response.
     * @throws IOException relate to network and stream reading.
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        // Open connection on URL object.
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestProperty("trakt-api-key", CLIENT_ID);


        try {
            // Get InputStream form the connection.
            InputStream in = urlConnection.getInputStream();
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
        } finally {
            // close thee connection.
            urlConnection.disconnect();
        }
    }


}
