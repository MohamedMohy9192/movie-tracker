package com.era.www.movietracker.data;

import android.net.Uri;
import android.provider.BaseColumns;


public final class MoviesContract {

    /*
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website. A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * Play Store.
     */
    public static final String CONTENT_AUTHORITY = "com.era.www.movietracker";

    /*
     * Use CONTENT_AUTHORITY to create the base of all URI's which the app will use to contact
     * the content provider for MovieTracker.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * possible paths for accessing data that can be appended to BASE_CONTENT_URI
     * to form valid URI's that MovieTracker can handle
     * This is the path for the "box_office" table.
     */
    public static final String PATH_BOX_OFFICE = "box_office";

    /**
     * To prevent someone from accidentally instantiating the contract class,
     * make the constructor private.
     */
    private MoviesContract() {

    }

    /**
     * BoxOfficeEntry is an inner class that defines the contents of the box_office table
     * BaseColumns interface automatically includes a constant representing
     * the primary key field called _ID.
     */
    public static final class BoxOfficeEntry implements BaseColumns {

        /**
         * The CONTENT_URI used to query the box_office table from the content provider.
         */
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOX_OFFICE);

        public static final String TABLE_NAME = "box_office";

        //We didn't need to create a constant string for the id because that's what the BaseColumns
        //interface already does for us.

        public static final String COLUMN_MOVIE_REVENUE = "revenue";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_YEAR = "year";
        public static final String COLUMN_MOVIE_TRAKT_ID = "trakt_id";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_RELEASED = "released";
        public static final String COLUMN_MOVIE_TRAILER = "trailer";
        public static final String COLUMN_MOVIE_HOMEPAGE = "homepage";
        public static final String COLUMN_MOVIE_RATE = "rate";
        public static final String COLUMN_MOVIE_CERTIFICATION = "certification";
        public static final String COLUMN_MOVIE_RANK = "rank";


    }
}
