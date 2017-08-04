package com.era.www.movietracker.data;

import android.provider.BaseColumns;


public final class MoviesContract {

    /**
     * To prevent someone from accidentally instantiating the contract class,
     * make the constructor private.
     */
    private MoviesContract() {

    }

    /**
     * BaseColumns interface automatically includes a constant representing
     * the primary key field called _ID.
     */
    public static final class BoxOfficeEntry implements BaseColumns {

        public static final String TABLE_NAME = "box_office";

        //We didn't need to create a constant string for the id because that's what the BaseColumns
        //interface already does for us.
        public static final String COLUMN_MOVIE_NAME = "name";
        public static final String COLUMN_MOVIE_REVENUE = "revenue";
        public static final String COLUMN_MOVIE_RANK = "rank";


    }
}
