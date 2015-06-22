package testme.com.myapplication.processros;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import testme.com.myapplication.pojo.Content;
import testme.com.myapplication.pojo.Message;

/**
 * Created by Exam on 4/27/2015.
 */
public class JsonMessageParser {
    public static final String ID_TAG = "id";
    public static final String RECIVED_AT_TAG = "received_at";
    public static final String SENDER_TAG = "sender";
    public static final String CONTENT_TAG = "content";

    public static Message jsonToMessage(JSONObject jsonMessage)throws JSONException {
        Message message = new Message();
        message.setId(jsonMessage.getLong(ID_TAG));
        message.setRecievedTime(jsonMessage.getLong(RECIVED_AT_TAG));
        JSONObject jsonContent = jsonMessage.getJSONObject(CONTENT_TAG);
        message.setMessageContnet(JsonContentParser.jsonToContent(jsonContent));
        JSONObject jsonSender = jsonMessage.getJSONObject(SENDER_TAG);
        message.setMessageSender(JsonSenderParser.jsonToContent(jsonSender));
        return message;
    }
}
