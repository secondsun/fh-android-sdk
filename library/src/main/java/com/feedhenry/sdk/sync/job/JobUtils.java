package com.feedhenry.sdk.sync.job;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

/**
 * This class will save and restore job IDs from Shared Preferences.
 */

public final class JobUtils {


    private static final String JOB_PREFERENCES_KEY = "JobUtils.JOB_PREFERENCES_KEY";
    private static final String JOB_ID_KEY = "JobUtils.JOB_ID";
    public static final int NO_JOB_SCHEDULED = -1;


    /**
     *
     * If there is a scheduled sync job, return the jobs ID otherwise return NO_JOB_SCHEDULED. (-1)
     *
     * @param context context to load shared prefernces from
     * @return the JobId or NO_JOB_SCHEDULED (-1)
     */
    public static int getSyncJobId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(JOB_PREFERENCES_KEY, Context.MODE_PRIVATE);
        return prefs.getInt(JOB_ID_KEY, NO_JOB_SCHEDULED);
    }

    /**
     *
     * If there is a scheduled sync job, return the jobs ID otherwise return NO_JOB_SCHEDULED. (-1)
     *
     * @param context context to load shared prefernces from
     * @param jobId the JobId to save.  Must not be negative.
     */
    public static void setSyncJobId(int jobId, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(JOB_PREFERENCES_KEY, Context.MODE_PRIVATE);
        prefs.edit().putInt(JOB_ID_KEY, jobId).commit();
    }
}
