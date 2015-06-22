package org.example.eyalgal.soundcloudtracksearch.requests;

import com.android.volley.Response;

import org.json.JSONArray;

/**
 * Created by eyal.gal on 14/04/2015.
 */
public class TracksSearchRequest extends ASoundCloudBaseRequest {

    public TracksSearchRequest(CharSequence searchString, Response.Listener<JSONArray> responseListener, Response.ErrorListener errorListener) {
        super("tracks", responseListener, errorListener);
        mExtraParams = String.format("&q=%s",searchString);
    }
}
