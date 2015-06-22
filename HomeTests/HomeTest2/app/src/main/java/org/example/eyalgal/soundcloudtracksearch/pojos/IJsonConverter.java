package org.example.eyalgal.soundcloudtracksearch.pojos;

import org.json.JSONObject;

/**
 * Created by eyal.gal on 15-04-15.
 */
public interface IJsonConverter {
    void fillFromJson(JSONObject item);
}
