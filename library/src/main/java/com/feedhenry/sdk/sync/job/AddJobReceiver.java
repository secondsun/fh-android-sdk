package com.feedhenry.sdk.sync.job;

import android.content.Context;
import android.support.annotation.NonNull;

import com.evernote.android.job.JobCreator;
import com.evernote.android.job.JobManager;

/**
 * This class will register the SyncJobCreator when it receives the correct broadcast.
 */

public class AddJobReceiver  extends JobCreator.AddJobCreatorReceiver {
    @Override
    protected void addJobCreator(@NonNull Context context, @NonNull JobManager manager) {
        manager.addJobCreator(new SyncJobCreator());
    }
}
