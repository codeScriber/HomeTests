package testme.com.myapplication.processros;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import testme.com.myapplication.pojo.Content;

/**
 * Created by Exam on 4/27/2015.
 */
public class JsonContentParser {

    private static final String TEXT_TYPE_TAG = "text";
    private static final String LOCATION_TYPE_TAG = "location";
    private static final String IMAGE_TYPE_TAG = "image";

    private static final String CONTENT_TAG = "content";
    private static final String CONTENT_TYPE_TAG = "type";

    public static Content.ContentType JsonToContentType(String jsonString){
        Content.ContentType contentType = null;
        if( ! TextUtils.isEmpty(jsonString) ){
            if( jsonString.equals(TEXT_TYPE_TAG)){
                contentType = Content.ContentType.TEXT;
            }else if( jsonString.equals(LOCATION_TYPE_TAG) ){
                contentType = Content.ContentType.LOCATION;
            }else if( jsonString.equals(IMAGE_TYPE_TAG)){
                contentType = Content.ContentType.IMAGE;
            }
        }
        return contentType;
    }

    public static Content jsonToContent(JSONObject jsonContent)throws JSONException{
        Content content = new Content();
        content.setContent(jsonContent.getString(CONTENT_TAG));
        Content.ContentType type = JsonToContentType(jsonContent.getString(CONTENT_TYPE_TAG));
        if( type == null ){
            throw new JSONException("bad content type in json");
        }
        content.setType(type);
        return content;
    }


}
