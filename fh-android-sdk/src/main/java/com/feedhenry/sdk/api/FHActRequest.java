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
import com.feedhenry.sdk.FHRemote;
import com.feedhenry.sdk.utils.FHLog;
import cz.msebera.android.httpclient.Header;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;


/**
 * The request for calling the cloud side function of the app. Example:
 * 
 * <pre>
 * {@code
 *   //calling a cloud side function called "getTweets" and pass in the keywords
 *   FHActRequest request = FH.buildActRequest("getTweets", new JSONObject().put("keyword",
 * "FeedHenry"));
 *   reqeust.executeAsync(new FHActCallback(){
 *     public void success(FHResponse pResp){
 *       JSONObject tweetsObj = pResp.getJson();
 *       ...
 *     }
 * 
 *     public void fail(FHResponse pResp){
 *       //process error data
 *       ...
 *     }
 *   });
 * }
 * </pre>
 */
public class FHActRequest extends FHRemote {

    private String mRemoteAct;
    protected org.json.JSONObject mArgs = new org.json.JSONObject();

    protected static String LOG_TAG = "com.feedhenry.sdk.api.FHActRequest";

    /**
     * Constructor
     * 
     * @param context the applicaiton context
     */
    public FHActRequest(Context context) {
        super(context);
    }

    protected String getApiURl() {
        String host = CloudProps.getInstance().getCloudHost();
        String path = getPath();
        String hostUrl = host + (path.startsWith("/") ? path : '/' + path);
        return hostUrl;
    }

    /**
     * The name of the cloud side function
     * 
     * @param pAction cloud side function name
     */
    public void setRemoteAction(String pAction) {
        mRemoteAct = pAction;
    }

    /**
     * Set the parameters for the cloud side function
     * 
     * @param pArgs the parameters that will be passed to the cloud side function
     * @deprecated use {@link #setArgs(org.json.JSONObject) } instead.
     */
    @Deprecated
    public void setArgs(org.json.fh.JSONObject pArgs) {
        try {
            setArgs(new org.json.JSONObject(pArgs.toString()));
        } catch (JSONException ex) {
            FHLog.e(LOG_TAG, ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }

    public void setArgs(org.json.JSONObject pArgs) {
        mArgs = pArgs;
        // keep backward compatibility
        if (!mArgs.has("__fh")) {
            try {
                mArgs.put("__fh", FH.getDefaultParams2());
            } catch (Exception e) {

            }
        }
    }

    
    protected org.json.fh.JSONObject getRequestArgs() {
        return new org.json.fh.JSONObject(mArgs.toString());
    }

    protected org.json.JSONObject getRequestArgs2() {
        return mArgs;
    }
    
    @Override
    protected String getPath() {
        return "cloud/" + mRemoteAct;
    }

    @Override
    protected Header[] buildHeaders(Header[] pHeaders) throws Exception {
        return FH.getDefaultParamsAsHeaders(pHeaders);
    }

}
