package testme.com.myapplication.processros;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import testme.com.myapplication.pojo.Message;

/**
 * Created by Eyal on 29-4-15.
 */
public class MessagesPareser {
    private static final String MESSAGES_TAG = "messages";

    public static List<Message> jsonToMessages(JSONObject originalJson){
        List<Message> messages = null;
        try{
            JSONArray array = originalJson.getJSONArray(MESSAGES_TAG);
            messages = new ArrayList<>(array.length());
            for(int i = 0; i < array.length() ; i++){
                try {
                    JSONObject jsonMessage = array.getJSONObject(i);
                    messages.add(JsonMessageParser.jsonToMessage(jsonMessage));
                }catch(JSONException innerException){
                    Log.e("MessageParser", "could not parse message " + i);
                    innerException.printStackTrace();
                }
            }
        }catch(JSONException je){
            je.printStackTrace();
            //for outer exceptions return null;
            messages = null;
        }
        return messages;
    }
}
