package com.era.www.movietracker.sync;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;

public class BoxOfficeIntentService extends IntentService {


    public BoxOfficeIntentService() {
        super("BoxOfficeIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        BoxOfficeSyncTask.syncBoxOfficeMovie(this);
    }
}
