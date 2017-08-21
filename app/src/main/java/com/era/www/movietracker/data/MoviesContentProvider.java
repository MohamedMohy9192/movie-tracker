package com.era.www.movietracker.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.UnsupportedSchemeException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.era.www.movietracker.data.MoviesContract.BoxOfficeEntry;


public class MoviesContentProvider extends ContentProvider {

    // Member variable for a MoviesDbHelper that's initialized in onCreate method.
    private MoviesDbHelper mMoviesDbHelper;

    //final integer constants for the directory of box_office movies and a single item.
    // It's convention to use 100, 200, 300, etc for directories,
    // and related ints (101, 102, ..) for items in that directory.
    public static final int BOX_OFFICE = 100;
    public static final int BOX_OFFICE_ID = 101;

    //a static variable for the Uri matcher that you construct
    private static UriMatcher sUriMatcher = buildUriMatcher();

    /**
     * a static buildUriMatcher method that associates URI's with their int match
     * Initialize a new matcher object without any matches,
     * then use .addURI(String authority, String path, int match) to add matches
     */
    private static UriMatcher buildUriMatcher() {
        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor.
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*
         All paths added to the UriMatcher have a corresponding int.
         For each kind of uri you may want to access, add the corresponding match with addURI.
         The two calls below add matches for the box_office directory and a single item by ID.
         */
        uriMatcher.addURI(
                MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_BOX_OFFICE, BOX_OFFICE);
        uriMatcher.addURI(
                MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_BOX_OFFICE + "/#", BOX_OFFICE_ID);

        return uriMatcher;
    }

    /**
     * onCreate() which is called when the provider initialized in general onCreate is where you
     * should initialize anything you’ll need to setup and access
     * your underlying data source.
     * In this case, you’re working with a SQLite database, so you’ll need to
     * initialize a DbHelper to gain access to it.
     */
    @Override
    public boolean onCreate() {

        Context context = getContext();
        mMoviesDbHelper = new MoviesDbHelper(context);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        // Get access to the task database (to write new data to).
        SQLiteDatabase database = mMoviesDbHelper.getWritableDatabase();

        //Write URI matching code to identify the match for the box_office directory
        int match = sUriMatcher.match(uri);

        Uri returnUri; // URI to be returned

        switch (match) {
            case BOX_OFFICE:
                // Inserting values into box_office table
                long rowId = database.insert(BoxOfficeEntry.TABLE_NAME, null, values);
                if (rowId > 0) {
                    returnUri = ContentUris.withAppendedId(BoxOfficeEntry.CONTENT_URI, rowId);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                // Default case throws an UnsupportedOperationException
                break;
            default:
                throw new UnsupportedOperationException("Insertion is not supported for " + uri);
        }

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
