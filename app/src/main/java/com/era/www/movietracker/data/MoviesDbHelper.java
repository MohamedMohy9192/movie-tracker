package com.era.www.movietracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.era.www.movietracker.data.MoviesContract.BoxOfficeEntry;

/**
 * A helper class to manage database creation and version management.
 */
public class MoviesDbHelper extends SQLiteOpenHelper {

    /**
     * Name of the database file,  this will be the name of local file on android device that
     * will store all our data.
     */
    private static final String DATABASE_NAME = "movies.db";
    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Call the parent constructor and let the parent class handle every thing for us.
     *
     * @param context the context of the calling activity.
     */
    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the creation of
     * tables and the initial population of the tables should happen.
     * SQLiteDatabase has methods to create, delete, execute SQL commands,
     * and perform other common database management tasks.
     *
     * @param db instance of SQLiteDatabase class.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_BOX_OFFICE_ENTRY = "CREATE TABLE " +
                BoxOfficeEntry.TABLE_NAME + " (" +
                BoxOfficeEntry._ID + " INTEGER PRIMARY KEY AUTO INCREMENT, " +
                BoxOfficeEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, " +
                BoxOfficeEntry.COLUMN_MOVIE_REVENUE + " REAL NOT NULL, " +
                BoxOfficeEntry.COLUMN_MOVIE_RANK + " INTEGER NOT NULL" + ");";

        //Execute a single SQL statement that is NOT a SELECT/INSERT/UPDATE/DELETE.
        db.execSQL(SQL_CREATE_BOX_OFFICE_ENTRY);
    }

    /**
     * This only called when the version number we defined earlier becomes larger than the version
     * number of the database on the device.
     * so database needs to be upgraded the implementation should use this method to
     * drop tables, add tables, or do anything else it needs to upgrade to the new schema version.
     *
     * @param db:         The database.
     * @param oldVersion: The old database version.
     * @param newVersion: The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        final String SQL_DELETE_BOX_OFFICE_ENTRY =
                "DROP TABLE IF EXISTS" + BoxOfficeEntry.TABLE_NAME;
        db.execSQL(SQL_DELETE_BOX_OFFICE_ENTRY);
        onCreate(db);
    }
}
