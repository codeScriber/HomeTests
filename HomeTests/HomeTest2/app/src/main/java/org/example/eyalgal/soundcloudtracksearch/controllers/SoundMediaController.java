package org.example.eyalgal.soundcloudtracksearch.controllers;

import android.media.MediaPlayer;
import android.widget.MediaController;

/**
 * Created by eyal.gal on 16/04/2015.
 */
public class SoundMediaController implements MediaController.MediaPlayerControl {

    private MediaPlayer mMediaPlayer;
    private MediaPlayerStateTracker mStateTracker;

    public SoundMediaController(MediaPlayer player){
        mMediaPlayer = player;
    }

    @Override
    public void start() {
        if( mMediaPlayer != null
                && mStateTracker.getMediaPlayerState() == IMediaPlayerStateReporter.MediaPlayerState.prepared ){
            mMediaPlayer.start();
        }
    }

    @Override
    public void pause() {
        if( mMediaPlayer != null ){
            mMediaPlayer.pause();
        }
    }

    @Override
    public int getDuration() {
        return mMediaPlayer == null ? 0 : mMediaPlayer.getDuration() ;
    }

    @Override
    public int getCurrentPosition() {
        return mMediaPlayer == null ? 0 : mMediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        if( mMediaPlayer != null  &&
                mStateTracker.getMediaPlayerState() == IMediaPlayerStateReporter.MediaPlayerState.prepared ){
            mMediaPlayer.seekTo(pos);
        }
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer == null ? false : mMediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return mMediaPlayer != null;
    }

    @Override
    public boolean canSeekBackward() {
        return mMediaPlayer != null;
    }

    @Override
    public boolean canSeekForward() {
        return mMediaPlayer != null;
    }

    @Override
    public int getAudioSessionId() {
        return mMediaPlayer == null ? 0 : mMediaPlayer.getAudioSessionId();
    }
}
