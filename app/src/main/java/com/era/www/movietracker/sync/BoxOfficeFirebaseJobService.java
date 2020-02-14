package com.era.www.movietracker.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;

public class BoxOfficeFirebaseJobService extends JobService {

    private AsyncTask<Void, Void, Void> mSyncBoxOfficeMovieBackgroundTask;

    /**
     * this method is going to execute syncBoxOffice Task which download box office movies and inset
     * them into database, by default this method is run on the application's main thread,
     * so we need to offload work to a background thread.
     * @param job
     * @return
     */
    @Override
    public boolean onStartJob(final JobParameters job) {
        mSyncBoxOfficeMovieBackgroundTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = BoxOfficeFirebaseJobService.this;
                BoxOfficeSyncTask.syncBoxOfficeMovie(context);
                return null;
            }
            /*
             * Once the AsyncTask is finished, the job is finished. To inform JobManager that
             * you're done, you call jobFinished with the JobParameters that were passed to your
             * job, the jobParameters are a bundle of key value arguments and a boolean representing
              * whether the job needs to be rescheduled. this is usually if something didn't work
              * and you want the job to try running again. the job is successful so no need to reschedule
             */
            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(job, false);
            }
        };
        mSyncBoxOfficeMovieBackgroundTask.execute();
        //return true which indicate that our job is still doing some work which doing it in other
        //thread (AsyncTask)
        return true;
    }

    /**
     * Called when the scheduling engine has decided to interrupt the execution of a running job,
     * most likely because the runtime constraints associated with the job are no longer satisfied.
     *
     * @return whether the job should be retried
     * @see Job.Builder#setRetryStrategy(RetryStrategy)
     * @see RetryStrategy
     */
    @Override
    public boolean onStopJob(JobParameters job) {
        if (mSyncBoxOfficeMovieBackgroundTask != null)
            mSyncBoxOfficeMovieBackgroundTask.cancel(true);

        //return true means is as soon as the conditions are re-met
        //the job should retried again
        return false;
    }
}
