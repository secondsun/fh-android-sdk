package com.feedhenry.sdk.sync.job;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by summers on 8/24/17.
 */

public class SyncJobCreator implements JobCreator {
    @Override
    public Job create(String tag) {
        switch (tag) {
            case SyncJob.JOB_TAG:
                return new SyncJob();
            default:
                return null;
        }
    }
}
