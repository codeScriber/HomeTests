package org.example.eyalgal.soundcloudtracksearch.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.example.eyalgal.soundcloudtracksearch.R;
import org.example.eyalgal.soundcloudtracksearch.SoundCloudTracksApplication;
import org.example.eyalgal.soundcloudtracksearch.adapters.TracksAdapter;
import org.example.eyalgal.soundcloudtracksearch.pojos.SoundCloudTrackItem;
import org.example.eyalgal.soundcloudtracksearch.requests.TracksSearchRequest;
import org.json.JSONArray;
import org.json.JSONObject;


public class SoundCloudSearch extends ActionBarActivity {

    private TextView mTrackSearchText;
    private ListView mTracksView;
    private TracksAdapter mTracksAdapter;
    private ResponseListeners mNetworkListeners;
    private String mCurrentRequestTag;
    private SoundCloudTrackItem mLastSentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_cloud_search);
        mTrackSearchText = (TextView)findViewById(R.id.tracksSearchText);
        mTracksView = (ListView)findViewById(R.id.tracks_list);
        mTracksView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject obj = mTracksAdapter.getItem(position);
                if( obj != null ){
                    if(mLastSentItem == null ){
                        mLastSentItem = new SoundCloudTrackItem();
                    }
                    mLastSentItem.fillFromJson(obj);
                    Intent openTrackIntent = new Intent(SoundCloudSearch.this, TrackHearSeeActivity.class);
                    openTrackIntent.putExtra(SoundCloudTrackItem.BUNDLE_NAME, mLastSentItem);
                    startActivity(openTrackIntent);
                }
            }
        });
        Button go = (Button)findViewById(R.id.go_button);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mTrackSearchText.getText() != null && mTrackSearchText.getText().length() != 0 ){
                    InputMethodManager imm = (InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mTrackSearchText.getWindowToken(), 0);
                    requestTracks(mTrackSearchText.getText());
                }
            }
        });
        mNetworkListeners = new ResponseListeners();
    }

    private void requestTracks(CharSequence text) {
        TracksSearchRequest request = new TracksSearchRequest(text, mNetworkListeners, mNetworkListeners);
        mCurrentRequestTag = (String)request.getTag();
        getNetQueue().add(request);
    }

    private SoundCloudTracksApplication getMyApplication(){
        return (SoundCloudTracksApplication)getApplication();
    }

    private RequestQueue getNetQueue(){
        return getMyApplication().getRequestQueue();
    }

    private class ResponseListeners implements Response.Listener<JSONArray>,Response.ErrorListener{

        @Override
        public void onErrorResponse(VolleyError error) {
            emptyList();
        }

        @Override
        public void onResponse(JSONArray response) {
            fillTheList(response);
        }
    }

    private void fillTheList(JSONArray response) {
        if( mTracksAdapter == null ){
            mTracksAdapter = new TracksAdapter(this);
            mTracksView.setAdapter(mTracksAdapter);
        }
        mTracksAdapter.setItems(response);
        mTracksAdapter.notifyDataSetChanged();
    }

    private void emptyList() {
        if( mTracksAdapter != null ){
            mTracksAdapter.setItems(null);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(! TextUtils.isEmpty(mCurrentRequestTag)) {
            getNetQueue().cancelAll(mCurrentRequestTag);
        }
    }
}
