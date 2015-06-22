package org.example.eyalgal.soundcloudtracksearch.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.example.eyalgal.soundcloudtracksearch.R;
import org.example.eyalgal.soundcloudtracksearch.pojos.SoundCloudTrackItem;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by eyal.gal on 14/04/2015.
 */
public class TracksAdapter extends BaseAdapter {

    private JSONArray mItems;
    private LayoutInflater mInflater;
    private String mNoTitle;

    public TracksAdapter(Context context){
        mInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        mNoTitle = context.getString(R.string.no_title_item);
    }

    public void setItems(JSONArray items){
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems == null ? 0 : mItems.length();
    }

    @Override
    public JSONObject getItem(int position) {
        return mItems == null ? null : mItems.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView)convertView;
        if( textView == null ){
            textView = (TextView)mInflater.inflate(R.layout.track_item_layout, parent, false);
        }
        JSONObject item = getItem(position);
        if( item != null ){
            String title = item.optString(SoundCloudTrackItem.TITLE_SLAG);
            if(TextUtils.isEmpty(title)){
                title = mNoTitle;
            }
            textView.setText(title);
        }else{
            textView = null;
        }
        return textView;
    }
}
