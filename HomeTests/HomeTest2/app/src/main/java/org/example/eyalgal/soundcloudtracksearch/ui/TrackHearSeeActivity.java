package org.example.eyalgal.soundcloudtracksearch.ui;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.example.eyalgal.soundcloudtracksearch.R;
import org.example.eyalgal.soundcloudtracksearch.SoundCloudTracksApplication;
import org.example.eyalgal.soundcloudtracksearch.controllers.IMediaPlayerStateReporter;
import org.example.eyalgal.soundcloudtracksearch.controllers.MediaPlayerStateTracker;
import org.example.eyalgal.soundcloudtracksearch.controllers.SoundMediaController;
import org.example.eyalgal.soundcloudtracksearch.pojos.SoundCloudTrackItem;

public class TrackHearSeeActivity extends ActionBarActivity implements MediaPlayer.OnPreparedListener{

    private MediaPlayer mMediaPlayer;
    private MediaController mControls;
    private SoundCloudTrackItem mDataItem;
    private MediaPlayerStateTracker mMPState;

    private static final String TAG  = "TRACK_HEAR_SEE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_view_listen_layout);
        mDataItem = (SoundCloudTrackItem)getIntent().getSerializableExtra(SoundCloudTrackItem.BUNDLE_NAME);
        if( mDataItem == null ){
            throw new IllegalStateException("cannot send empty intent to this activiy");
        }
        initImageView();
        initMediaPlayer();
    }

    private void initImageView() {
        Log.d(TAG, "image url: " + mDataItem.getImageUrl());
        NetworkImageView waveformImage = (NetworkImageView)findViewById(R.id.waveform_image);
        waveformImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mMediaPlayer != null
                        && mMPState.getMediaPlayerState() == IMediaPlayerStateReporter.MediaPlayerState.prepared
                        && ! mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                }
            }
        });
        waveformImage.setDefaultImageResId(R.drawable.no_image_available);
        waveformImage.setErrorImageResId(R.drawable.error_image);
        if( mDataItem.getImageUrl() != null ) {
            Log.d("XXX", "Image url: " + mDataItem.getImageUrl());
            waveformImage.setImageUrl(mDataItem.getImageUrl(),
                    ((SoundCloudTracksApplication) getApplication()).getImageLoader());
        }
    }


    private void initMediaPlayer(){
        mMPState =  new MediaPlayerStateTracker(IMediaPlayerStateReporter.MediaPlayerState.created);
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(mDataItem.getStreamUrl());
            mMediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
//            initControls();
        } catch (Exception e) {
            e.printStackTrace();
            showToastError(getString(R.string.error_opening_track));
        }
    }

    private void initControls() {
        mControls = (MediaController)findViewById(R.id.media_controlls);
        mControls.setMediaPlayer(new SoundMediaController(mMediaPlayer));
        mControls.show();
    }

    private void showToastError(String error){
        Toast.makeText(this, error, Toast.LENGTH_LONG);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if( mMediaPlayer != null ) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mMPState.setMPState(IMediaPlayerStateReporter.MediaPlayerState.prepared);
    }
}
