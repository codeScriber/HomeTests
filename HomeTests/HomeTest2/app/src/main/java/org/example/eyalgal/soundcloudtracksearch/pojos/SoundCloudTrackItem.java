package org.example.eyalgal.soundcloudtracksearch.pojos;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by eyal.gal on 14-04-15.
 */
public class SoundCloudTrackItem implements IJsonConverter, Serializable{

    public static final String STREAM_URL_SLAG = "stream_url";
    public static final String TITLE_SLAG = "title";
    public static final String IMAGE_URL_SLAG = "artwork_url";

    public static final String BUNDLE_NAME = "TRACK_ITEM";

    private String mStreamUrl;
    private String mTitle;
    private String mImageUrl;

    public String getStreamUrl() {
        return mStreamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        mStreamUrl = streamUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    @Override
    public void fillFromJson(JSONObject item) {
        mStreamUrl = item.optString(STREAM_URL_SLAG);
        mTitle = item.optString(TITLE_SLAG);
        mImageUrl = item.optString(IMAGE_URL_SLAG);
    }
}
