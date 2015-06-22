package org.example.eyalgal.soundcloudtracksearch.controllers;

/**
 * Created by eyal.gal on 16/04/2015.
 */
public interface IMediaPlayerStateReporter {
    enum MediaPlayerState{
        created,
        playing,
        prepared,
    }

    MediaPlayerState getMediaPlayerState();
}
