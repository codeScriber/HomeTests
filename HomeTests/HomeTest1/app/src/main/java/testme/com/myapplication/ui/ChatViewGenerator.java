package testme.com.myapplication.ui;

import android.nfc.NfcEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import testme.com.myapplication.R;
import testme.com.myapplication.pojo.Content;
import testme.com.myapplication.pojo.Message;

/**
 * Created by Eyal on 29-4-15.
 */
public class ChatViewGenerator {
    private LayoutInflater mInflater;

    public ChatViewGenerator(LayoutInflater inflater){
        mInflater  = inflater;
    }

    /**
     * a bit wastefull since we don't have cache here, started makig one but it takes too long...
     * @param anchor
     * @return
     */
    public View generattChatViewforType(ViewGroup anchor,
                                        Message data, ImageLoader imageLoader){
        int layout = 0;
        switch (data.getMessageContnet().getType()){
            case IMAGE:
                layout = R.layout.content_image;
                break;
            case LOCATION:
                layout = R.layout.content_image;
                break;
            case TEXT:
                layout = R.layout.content_text;
                break;
            default:
                layout = 0;
        }
        View v = layout == 0 ?  null : mInflater.inflate(layout, anchor, false);
        if( v != null ){
            anchor.removeAllViews();
            anchor.addView(v);
            setViewData(v, data, imageLoader);
        }
        return v;
    }

    public void setViewData(View view, Message data, ImageLoader imageLoader){
        switch (data.getMessageContnet().getType()){
            case IMAGE:
                NetworkImageView netImageView = (NetworkImageView)view;
                netImageView.setImageUrl(data.getMessageContnet().getContent(), imageLoader);
                break;
            case LOCATION:
                //not implemented yet
                //should create the url from content then do the same as IMAGE.
                //https://maps.googleapis.com/maps/api/staticmap?center=63.259591,-144.667969&zoom=6&size=400x400&markers=color:blue%7Clabel:S%7C62.107733,-145.541936
                break;
            case TEXT:
                TextView textView = (TextView)view;
                textView.setText(data.getMessageContnet().getContent());
                break;
            default:
        }
    }

}
