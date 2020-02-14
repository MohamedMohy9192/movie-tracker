package com.era.www.movietracker.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.era.www.movietracker.data.MoviesContract.BoxOfficeEntry;


public class MoviesContentProvider extends ContentProvider {

    // Member variable for a MoviesDbHelper that's initialized in onCreate method.
    private MoviesDbHelper mMoviesDbHelper;

    /**
     * These constant will be used to match URIs with the data they are looking for.
     * it's convention to use 100, 200, 300, etc for directories,
     * and related ints (101, 102, ..) for items in that directory.
     */
    public static final int BOX_OFFICE = 100;
    public static final int BOX_OFFICE_ID = 101;

    /**
     * The URI Matcher used by this content provider. The leading "s" in this variable name
     * signifies that this UriMatcher is a static member variable of MoviesContentProvider.
     */
    private static UriMatcher sUriMatcher = buildUriMatcher();

    /**
     * a static buildUriMatcher method that associates URI's with their int constant match.
     *
     * @return A UriMatcher that correctly matches the constants for BOX_OFFICE and BOX_OFFICE_ID.
     */
    private static UriMatcher buildUriMatcher() {
        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor.
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*
         All paths added to the UriMatcher have a corresponding code to return when a match is found
         For each type of URI you want to add, create a corresponding code (BOX_OFFICE and BOX_OFFICE_ID),
         The two calls below add matches for the box_office directory and a single item by ID.
         */
        uriMatcher.addURI(
                MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_BOX_OFFICE, BOX_OFFICE);
        uriMatcher.addURI(
                MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_BOX_OFFICE + "/#", BOX_OFFICE_ID);

        return uriMatcher;
    }

    /**
     * onCreate() which is called when the provider initialized in general onCreate is where i
     * should initialize anything i will need to setup and access
     * your underlying data source.
     * In this case, iam working with a SQLite database, so i will need to
     * initialize a DbHelper to gain access to it.
     */
    @Override
    public boolean onCreate() {

        Context context = getContext();
        mMoviesDbHelper = new MoviesDbHelper(context);
        return false;
    }

    /**
     * handle requests to insert a set of new rows, we are only going to be
     * inserting multiple rows of data at a time of box office movies.
     *
     * @param uri:    The content:// URI of the insertion request.
     * @param values: An array of sets of column_name/value pairs to add to the database.
     * @return The number of values that were inserted.
     */
    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        // Get access to the task database (to write new data to).
        SQLiteDatabase database = mMoviesDbHelper.getWritableDatabase();
        //match the uri that passed with the code in uriMatcher to determine which path to go.
        int match = sUriMatcher.match(uri);

        switch (match) {
            case BOX_OFFICE:
                // initiate the variable that hold the total number of rows that will insert.
                int rowsInserted = 0;
                //a database transaction is usually used to ensure that the set of operations
                //will either all succeed or fail
                // Begins a transaction
                database.beginTransaction();
                try {
                    for (ContentValues contentValues : values) {
                        long rowId = database.insert(BoxOfficeEntry.TABLE_NAME,
                                null,
                                contentValues);
                        if (rowId != -1) {
                            rowsInserted++;
                        }
                    }
                    //Indicates that the transaction should be committed
                    database.setTransactionSuccessful();
                } finally {
                    // Ends the transaction causing a commit if setTransactionSuccessful() has been called
                    database.endTransaction();
                }
                if(rowsInserted > 0){
                    //notify the resolver
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            default:
                //If the URI does match match BOX_OFFICE, return the super implementation of bulkInsert
                return super.bulkInsert(uri, values);
        }
    }

    /**
     * Handles query requests from clients. We will use this method in MovieTracker to query for all
     * of our box_office data as well as to query for the single box_office movie by it's id.
     *
     * @param uri           The URI to query
     * @param projection    The list of columns to put into the cursor. If null, all columns are
     *                      included.
     * @param selection     A selection criteria to apply when filtering rows. If null, then all
     *                      rows are included.
     * @param selectionArgs You may include ?s in selection, which will be replaced by
     *                      the values from selectionArgs, in order that they appear in the
     *                      selection.
     * @param sortOrder     How the rows in the cursor should be sorted.
     * @return A Cursor containing the results of the query. In our implementation,
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        //Get access to underlying database (read-only for query)
        SQLiteDatabase database = mMoviesDbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor cursor;

        switch (match) {
            /*
              When sUriMatcher's match method will return the code that indicates to us that we need
              to return all of the movies in our box_office table.
              In this case, we want to return a cursor that contains every row of movie data
              in our box_office table.
             */
            case BOX_OFFICE:
                cursor = database.query(
                        BoxOfficeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            /*
              When sUriMatcher's match method will return the code that indicates to us that we need
              to return a row of movie data for a particular id.
              In this case, we want to return a cursor that contains one row of movie data for
              a particular id.
             */
            case BOX_OFFICE_ID:
                /*
                 * In order to determine the date associated with the URI, we look at the
                 * path segment. and extract the id append to the URI.
                 */
                long rowId = ContentUris.parseId(uri);
                // Selection is the _ID column = ?, and the Selection args = the row ID from the URI
                selection = BoxOfficeEntry.COLUMN_MOVIE_TRAKT_ID + "=?";
                selectionArgs = new String[]{String.valueOf(rowId)};

                cursor = database.query(
                        BoxOfficeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        //notify the cursor that data changed at specific Uri
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return cursor;

    }

    /**
     * getType() handles requests for the MIME type of data
     * We are working with two types of data:
     * 1) a directory and 2) a single row of data.
     *
     * @param uri
     * @return
     */
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

    /**
     * Deletes data at a given URI with optional arguments for more fine tuned deletions.
     *
     * @param uri           The full URI to query
     * @param selection     An optional restriction to apply to rows when deleting.
     * @param selectionArgs Used in conjunction with the selection statement
     * @return The number of rows deleted
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Get access to the task database (to write new data to).
        SQLiteDatabase database = mMoviesDbHelper.getWritableDatabase();

        /*
         * If we pass null as the selection to SQLiteDatabase#delete, our entire table will be
         * deleted. However, if we do pass null and delete all of the rows in the table, we won't
         * know how many rows were deleted. According to the documentation for SQLiteDatabase,
         * passing "1" for the selection will delete all rows and return the number of rows
         * deleted, which is what the caller of this method expects.
         */
        if (selection == null) selection = "1";
        //match the uri that passed with the code in uriMatcher to determine which path to go.
        int match = sUriMatcher.match(uri);

        int rowsDeleted;

        switch (match) {
            case BOX_OFFICE:
                rowsDeleted = database.delete(
                        BoxOfficeEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Insertion is not supported for " + uri);
        }
         /* If we actually deleted any rows, notify that a change has occurred to this URI */
         if (rowsDeleted != 0){
             getContext().getContentResolver().notifyChange(uri, null);
         }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
