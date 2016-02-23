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
package com.feedhenry.sdk;

import android.util.Log;
import org.json.JSONException;
import org.json.fh.JSONArray;
import org.json.fh.JSONObject;

/**
 * Represents the response data from FeedHenry when an API call completes.
 */
public class FHResponse {

    private static final String TAG = FHResponse.class.getSimpleName();

    private org.json.JSONObject mResults;
    private org.json.JSONArray mResultArray;
    private Throwable mError;
    private String mErrorMessage;

    /**
     * 
     * Constructs a response
     * 
     * @param pResults the Results of a response
     * @param pResultArray the Results array of a response
     * @param e an exception which was caught
     * @param pError the error message
     *
     * @deprecated the org.json.fh package is deprecated.
     */
    @Deprecated
    public FHResponse(JSONObject pResults, JSONArray pResultArray, Throwable e, String pError) {
        if (pResults != null) {
            try {
                mResults = new org.json.JSONObject(pResults.toString());
            } catch (JSONException ex) {
                Log.e(TAG, ex.getMessage(), ex);
            }
        }

        if (pResultArray != null) {
            try {
                mResultArray = new org.json.JSONArray(pResultArray.toJSONArray(pResultArray));
            } catch (JSONException ex) {
                Log.e(TAG, ex.getMessage(), ex);
            }

        }
        mError = e;
        mErrorMessage = pError;
    }

    public FHResponse(org.json.JSONObject pResults, org.json.JSONArray pResultArray, Throwable e, String pError) {
        mResults = pResults;
        mResultArray = pResultArray;
        mError = e;
        mErrorMessage = pError;
    }

    /**
     * Gets the response data as a JSONObject.
     *
     * @return a JSONObject
     * @deprecated the org.json.fh package is deprecated.  Please use getResult
     */
    public JSONObject getJson() {
        return new JSONObject(mResults.toString());
    }

    /**
     * Gets the response data as a JSONArray.
     *
     * @return a JSONArray
     * @deprecated the org.json.fh package is deprecated.  Please use getResultArray
     */
    public JSONArray getArray() {
        return new JSONArray(mResultArray.toString());
    }

    public org.json.JSONArray  getResultArray() {
        return mResultArray;
    }

    public org.json.JSONObject getResults() {
        return mResults;
    }

    /**
     * Gets the error.
     *
     * @return the error
     */
    public Throwable getError() {
        return mError;
    }

    /**
     * Gets the error message.
     *
     * @return the error message
     */
    public String getErrorMessage() {
        return mErrorMessage;
    }

    /**
     * Gets the raw response content.
     *
     * @return the raw response content
     */
    public String getRawResponse() {
        if (mResults != null) {
            return mResults.toString();
        } else if (mResultArray != null) {
            return mResultArray.toString();
        } else {
            return mErrorMessage;
        }
    }
}
