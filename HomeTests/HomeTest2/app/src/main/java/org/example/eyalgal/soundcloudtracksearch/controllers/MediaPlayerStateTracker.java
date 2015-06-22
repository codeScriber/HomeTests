package org.example.eyalgal.soundcloudtracksearch.controllers;

/**
 * Created by eyal.gal on 16/04/2015.
 */
public class MediaPlayerStateTracker implements IMediaPlayerStateReporter {
    private MediaPlayerState mState;

    public MediaPlayerStateTracker(){
        mState = null;
    }

    public MediaPlayerStateTracker(MediaPlayerState state){
        setMPState(state);
    }

    public void setMPState(MediaPlayerState state){
        mState = state;
    }

    @Override
    public MediaPlayerState getMediaPlayerState() {
        return mState;
    }
}
