package com.feedhenry.sdk.sync.job;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.evernote.android.job.Job;
import com.evernote.android.job.util.support.PersistableBundleCompat;
import com.feedhenry.sdk.FH;
import com.feedhenry.sdk.FHActCallback;
import com.feedhenry.sdk.FHResponse;
import com.feedhenry.sdk.sync.FHSyncClient;
import com.feedhenry.sdk.sync.FHSyncConfig;
import com.feedhenry.sdk.sync.FHSyncDataset;
import com.feedhenry.sdk.sync.FHSyncListener;
import com.feedhenry.sdk.sync.FHSyncNotificationHandler;
import com.feedhenry.sdk.sync.NotificationMessage;

import org.json.fh.JSONObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * Created by summers on 8/29/17.
 */

public class SyncJob extends Job {

    public static final String JOB_TAG = "com.feedhenry.SyncJob";

    private FHSyncClient client;

    @NonNull
    @Override
    protected Result onRunJob(final Params params) {

        final CountDownLatch latch = new CountDownLatch(1);
        client = FHSyncClient.getInstance();
        /*
          We can not guarantee that the sync is happening while the app is running so we have to recall init.
         */
        FH.init(super.getContext(), new FHActCallback() {
            @Override
            public void success(FHResponse fhResponse) {


                if (!client.isInitialised()) {//If the client is initialized it means that sync is running somewhere else and we shouldn't run it in a job here.

                    //TODO Do Sync
                    PersistableBundleCompat extras = params.getExtras();
                    Set<String> datasets = new HashSet<>(Arrays.asList(extras.getStringArray("datasets")));

                    SyncJobSyncListener listener = new SyncJobSyncListener(client, datasets,latch);

                    //TODO setup sync config
                    FHSyncConfig syncJobConfig = new FHSyncConfig();
                    syncJobConfig.setNotifySyncComplete(true);
                    syncJobConfig.setNotifySyncStarted(true);
                    client.init(getContext(), syncJobConfig, listener);

                    for (String datasetId : datasets) {
                        JSONObject storedMetaData, storedQueryParams;


                        storedQueryParams = new JSONObject(extras.getString(String.format("%s_queryParams", datasetId), "{}"));
                        storedMetaData = new JSONObject(extras.getString(String.format("%s_metadata", datasetId), "{}"));

                        client.manage(datasetId, syncJobConfig, storedQueryParams, storedMetaData);
                        client.forceSync(datasetId);
                    }


                }

            }

            @Override
            public void fail(FHResponse fhResponse) {
                //TODO Log init failure
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            //Log exception
        }
        return null;
    }

    private static class SyncJobSyncListener implements FHSyncListener {

        private final FHSyncClient client;
        private final Set<String> dataSetIds;
        private final CountDownLatch latch;

        public SyncJobSyncListener(FHSyncClient client, Set<String> dataSetIds, CountDownLatch latch) {
            this.client = client;
            this.dataSetIds = dataSetIds;
            this.latch = latch;
        }

        @Override
        public void onSyncStarted(NotificationMessage pMessage) {
            client.stop(pMessage.getDataId());
        }

        @Override
        public void onSyncCompleted(NotificationMessage pMessage) {
            dataSetIds.remove(pMessage.getDataId());
            if (dataSetIds.isEmpty()) {
                latch.countDown();
            }
        }

        @Override
        public void onUpdateOffline(NotificationMessage pMessage) {

        }

        @Override
        public void onCollisionDetected(NotificationMessage pMessage) {

        }

        @Override
        public void onRemoteUpdateFailed(NotificationMessage pMessage) {

        }

        @Override
        public void onRemoteUpdateApplied(NotificationMessage pMessage) {

        }

        @Override
        public void onLocalUpdateApplied(NotificationMessage pMessage) {

        }

        @Override
        public void onDeltaReceived(NotificationMessage pMessage) {

        }

        @Override
        public void onSyncFailed(NotificationMessage pMessage) {
            dataSetIds.remove(pMessage.getDataId());
            if (dataSetIds.isEmpty()) {
                latch.countDown();
            }
        }

        @Override
        public void onClientStorageFailed(NotificationMessage pMessage) {

        }
    }

}
