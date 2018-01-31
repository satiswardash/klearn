package com.kortain.klearn.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by satiswardash on 22/01/18.
 */

public class NetworkStateService extends JobService {

    public static String NETWORK_STATE_ACTION = "network_state_change";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        Intent intent = new Intent(NETWORK_STATE_ACTION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        jobFinished(jobParameters, true);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
