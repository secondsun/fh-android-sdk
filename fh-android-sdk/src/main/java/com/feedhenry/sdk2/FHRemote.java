/**
 * Copyright Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feedhenry.sdk2;

import android.content.Context;
import com.feedhenry.sdk.AppProps;
import com.feedhenry.sdk.FHActCallback;
import com.feedhenry.sdk.utils.FHLog;
import com.feedhenry.sdk.utils.StringUtils;
import cz.msebera.android.httpclient.Header;
import org.json.fh.JSONObject;

/**
 * The base class that implements {@link FHAct}.
 */
public abstract class FHRemote implements FHAct {

    public static final String PATH_PREFIX = "/box/srv/1.1/";

    protected static String LOG_TAG = "com.feedhenry.sdk.FHRemote";

    protected FHActCallback mCallback;
    protected Context mContext;
    private final FHHttpClient mFHHttpClient = new FHHttpClient();

    public FHRemote(Context context) {
        mContext = context;
    }

    @Override
    public void execute() throws Exception {
        execute(mCallback);
    }

    @Override
    public void execute(FHActCallback pCallback) throws Exception {
        try {
            mFHHttpClient.post(getApiURl(), buildHeaders(null), getRequestArgs2(), pCallback, false);
        } catch (Exception e) {
            FHLog.e(LOG_TAG, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void setCallback(FHActCallback pCallback) {
        mCallback = pCallback;
    }

    protected String getApiURl() {
        String apiUrl = StringUtils.removeTrailingSlash(AppProps.getInstance().getHost());
        return apiUrl + PATH_PREFIX + getPath();
    }

    protected abstract String getPath();

    @Deprecated
    protected abstract JSONObject getRequestArgs();

    protected abstract org.json.JSONObject getRequestArgs2();

    protected abstract Header[] buildHeaders(Header[] pHeaders) throws Exception;
}
