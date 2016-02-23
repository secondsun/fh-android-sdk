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
package com.feedhenry.sdk.api;

import android.content.Context;
import com.feedhenry.sdk.CloudProps;
import com.feedhenry.sdk.FH;
import com.feedhenry.sdk.FHActCallback;
import com.feedhenry.sdk.FHHttpClient;
import com.feedhenry.sdk.FHRemote;
import com.feedhenry.sdk.utils.FHLog;
import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class FHCloudRequest extends FHRemote {

    public enum Methods {
        GET, POST, PUT, DELETE;

        public static Methods parse(String pMethod) throws Exception {
            try {
                return Methods.valueOf(pMethod.toUpperCase());
            } catch (Exception e) {
                throw new Exception("Unsupported HTTP method: " + pMethod);
            }
        }
    }

    protected static final String LOG_TAG = "com.feedhenry.sdk.api.FHCloudRequest";

    private String mPath = "";
    private Methods mMethod = Methods.GET;
    private Header[] mHeaders = null;
    private org.json.JSONObject mArgs = new org.json.JSONObject();

    public FHCloudRequest(Context context) {
        super(context);
    }

    public void setPath(String pPath) {
        mPath = pPath;
    }

    public void setMethod(Methods pMethod) {
        mMethod = pMethod;
    }

    public void setHeaders(Header[] pHeaders) {
        mHeaders = pHeaders;
    }

    @Deprecated
    public void setRequestArgs(org.json.fh.JSONObject pArgs) {
        try {
            mArgs = new JSONObject(mArgs.toString());
        } catch (JSONException ex) {
            FHLog.e(LOG_TAG, ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }

    public void setRequestArgs(org.json.JSONObject pArgs) {
        mArgs = pArgs;
    }

    
    @Override
    protected String getPath() {
        return mPath;
    }

    @Override
    protected org.json.fh.JSONObject getRequestArgs() {
        return new org.json.fh.JSONObject(mArgs.toString());
    }

    @Override
    protected org.json.JSONObject getRequestArgs2() {
        return mArgs;
    }
    
    @Override
    public void executeAsync(FHActCallback pCallback) throws Exception {
        try {
            switch (mMethod) {
                case GET:
                    new com.feedhenry.sdk2.FHHttpClient().get(getURL(), buildHeaders(mHeaders), mArgs, pCallback, false);
                    break;
                case PUT:
                    new com.feedhenry.sdk2.FHHttpClient().put(getURL(), buildHeaders(mHeaders), mArgs, pCallback, false);
                    break;
                case POST:
                    new com.feedhenry.sdk2.FHHttpClient().post(getURL(), buildHeaders(mHeaders), mArgs, pCallback, false);
                    break;
                case DELETE:
                    new com.feedhenry.sdk2.FHHttpClient().delete(getURL(), buildHeaders(mHeaders), mArgs, pCallback, false);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            FHLog.e(LOG_TAG, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void execute(FHActCallback pCallback) throws Exception {
        try {
            switch (mMethod) {
                case GET:
                    new com.feedhenry.sdk2.FHHttpClient().get(getURL(), buildHeaders(mHeaders), mArgs, pCallback, true);
                    break;
                case PUT:
                    new com.feedhenry.sdk2.FHHttpClient().put(getURL(), buildHeaders(mHeaders), mArgs, pCallback, true);
                    break;
                case POST:
                    new com.feedhenry.sdk2.FHHttpClient().post(getURL(), buildHeaders(mHeaders), mArgs, pCallback, true);
                    break;
                case DELETE:
                    new com.feedhenry.sdk2.FHHttpClient().delete(getURL(), buildHeaders(mHeaders), mArgs, pCallback, true);
                    break;
            }
        } catch (Exception e) {
            FHLog.e(LOG_TAG, e.getMessage(), e);
            throw e;
        }
    }

    private String getURL() {
        String host = CloudProps.getInstance().getCloudHost();
        return host + (getPath().startsWith("/") ? getPath() : '/' + getPath());
    }

    protected Header[] buildHeaders(Header[] pHeaders) throws Exception {
        return FH.getDefaultParamsAsHeaders(pHeaders);
    }
}
