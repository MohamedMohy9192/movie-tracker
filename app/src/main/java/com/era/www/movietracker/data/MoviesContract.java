package com.era.www.movietracker.data;

import android.net.Uri;
import android.provider.BaseColumns;


public final class MoviesContract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String CONTENT_AUTHORITY = "com.era.www.movietracker";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Define the possible paths for accessing data in this contract,
    // This is the path for the "box_office" directory]
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
         * The content URI to access the box_office data in the provider
         * BoxOfficeEntry content URI = base content URI + path
         */
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOX_OFFICE);

        public static final String TABLE_NAME = "box_office";

        //We didn't need to create a constant string for the id because that's what the BaseColumns
        //interface already does for us.
        public static final String COLUMN_MOVIE_NAME = "name";
        public static final String COLUMN_MOVIE_REVENUE = "revenue";
        public static final String COLUMN_MOVIE_RANK = "rank";


    }
}
