package org.example.eyalgal.soundcloudtracksearch.requests;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by eyal.gal on 14-04-15.
 */
public class ASoundCloudBaseRequest extends JsonArrayRequest {

    protected static final String CLIENT_ID = "d652006c469530a4a7d6184b18e16c81";
    protected static final String BASE_SOUND_CLOUD_URL = "https://api.soundcloud.com";
    //https://address/command/client_id=client_id[extra]
    protected static final String URL_FORMAT = "%s/%s?client_id=%s%s";

    protected static final String TAG = "SC_BR";

    //the API command
    protected String mCommand;
    //this is not the best of solutions but it does the trick, it puts all responsibility on the
    //developer to keep his urls clean and working, no validation so far.
    protected String mExtraParams;

    public ASoundCloudBaseRequest(String command, String extraParams,
                                  Response.Listener<JSONArray> responseListener,
                                  Response.ErrorListener errorListener){
        super(Method.GET, "", responseListener, errorListener);
        mCommand = command;
        setTag(command);
        mExtraParams = extraParams;
    }

    public ASoundCloudBaseRequest(String command,
                                  Response.Listener<JSONArray> responseListener,
                                  Response.ErrorListener errorListener){
        this(command,"", responseListener, errorListener);
    }

    public String getCommand(){
        return mCommand;
    }

    @Override
    public String getUrl() {
        if( mExtraParams == null ){
            mExtraParams = "";
        }
        String url = String.format(URL_FORMAT, BASE_SOUND_CLOUD_URL, mCommand, CLIENT_ID, mExtraParams);
        Log.d(TAG + mCommand, "url: " + url);
        return url;
    }
}
