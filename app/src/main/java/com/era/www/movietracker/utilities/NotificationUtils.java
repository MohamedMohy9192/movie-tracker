package com.era.www.movietracker.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.era.www.movietracker.R;
import com.era.www.movietracker.data.MoviesContract.BoxOfficeEntry;
import com.era.www.movietracker.detail.DetailActivity;

public class NotificationUtils {

    /*
     * The columns of data that we are interested in displaying within our notification to let
     * the user know there is new BoxOffice data available.
     */
    public static final String[] BOX_OFFICE_NOTIFICATION_PROJECTION = {
            BoxOfficeEntry.COLUMN_MOVIE_TRAKT_ID,
            BoxOfficeEntry.COLUMN_MOVIE_TITLE,
            BoxOfficeEntry.COLUMN_MOVIE_REVENUE,
    };

    /*
     * We store the indices of the values in the array of Strings above to more quickly be able
     * to access the data from our query. If the order of the Strings above changes, these
     * indices must be adjusted to match the order of the Strings.
     */
    public static final int COLUMN_MOVIE_TRAKT_ID = 0;
    public static final int COLUMN_MOVIE_TITLE = 1;
    public static final int COLUMN_MOVIE_REVENUE = 2;


    /*
     * This notification ID can be used to access our notification after we've displayed it. This
     * can be handy when we need to cancel the notification, or perhaps update it. This number is
     * arbitrary and can be set to whatever you like. 3004 is in no way significant.
     */
//  a constant int value to identify the notification
    private static final int BOX_OFFICE_NOTIFICATION_ID = 3004;
    private static final String BOX_OFFICE_NOTIFICATION_CHANNEL_ID = "box_office_notification_channel";
    private static final int Box_OFFICE_DETAILS_PENDING_INTENT_ID = 5564;


    /**
     * Constructs and displays a notification for the newly updated BoxOffice for today.
     *
     * @param context Context used to query our ContentProvider and use various Utility methods
     */
    public static void notifyUserOfNewBoxOffice(Context context) {

        Uri queryUri = BoxOfficeEntry.CONTENT_URI;

        String selection = BoxOfficeEntry.COLUMN_MOVIE_REVENUE +
                " = (SELECT max(" + BoxOfficeEntry.COLUMN_MOVIE_REVENUE + ") FROM " +
                BoxOfficeEntry.TABLE_NAME + ")";

        Cursor topBoxOfficeMovie = context.getContentResolver().query(
                queryUri,
                BOX_OFFICE_NOTIFICATION_PROJECTION,
                selection,
                null,
                null);



        /*
         * If topBoxOfficeMovie is empty, moveToFirst will return false. If our cursor is not
         * empty, we want to show the notification.
         */
        if (topBoxOfficeMovie.moveToFirst()) {
            int movieTraktId = topBoxOfficeMovie.getInt(COLUMN_MOVIE_TRAKT_ID);
            String movieTitle = topBoxOfficeMovie.getString(COLUMN_MOVIE_TITLE);
            int movieRev = topBoxOfficeMovie.getInt(COLUMN_MOVIE_REVENUE);


            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(
                        BOX_OFFICE_NOTIFICATION_CHANNEL_ID,
                        context.getString(R.string.box_office_notification_channel_name),
                        NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(mChannel);
            }
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,
                    BOX_OFFICE_NOTIFICATION_CHANNEL_ID)
                    .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .setSmallIcon(R.drawable.nav_movie_icon)
                    .setContentTitle(context.getString(R.string.box_office_notification_title))
                    .setContentText(movieTitle + " - " + FormatUtils.formatMovieRevenue(movieRev))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(
                            movieTitle + " - " + FormatUtils.formatMovieRevenue(movieRev)))
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentIntent(contentIntent(context, movieTraktId))
                    .setAutoCancel(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                    && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
            }
            notificationManager.notify(BOX_OFFICE_NOTIFICATION_ID, notificationBuilder.build());

        }
        topBoxOfficeMovie.close();
    }

    private static PendingIntent contentIntent(Context context, int movieTraktId) {
        Intent startActivityIntent = new Intent(context, DetailActivity.class);

        startActivityIntent.setData(BoxOfficeEntry.CONTENT_URI.buildUpon()
                .appendPath(String.valueOf(movieTraktId)).build());
        return PendingIntent.getActivity(
                context,
                Box_OFFICE_DETAILS_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
