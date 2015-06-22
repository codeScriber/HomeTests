package testme.com.myapplication.processros;

import org.json.JSONException;
import org.json.JSONObject;

import testme.com.myapplication.pojo.Sender;

/**
 * Created by Exam on 4/27/2015.
 */
public class JsonSenderParser {
    private static final String ID_TAG = "id";
    private static final String FNAME_TAG = "firstname";
    private static final String LNAME_TAG = "lastname";
    private static final String AVATAR_TAG = "avatar";
    private static final String IS_USER_TAG = "is_me";

    public static Sender jsonToContent(JSONObject jsonSender)throws JSONException {
        Sender sender = new Sender();
        sender.setId(jsonSender.getInt(ID_TAG));
        sender.setAvatar(jsonSender.getString(AVATAR_TAG));
        sender.setLastName(jsonSender.getString(LNAME_TAG));
        sender.setFirstName(jsonSender.getString(FNAME_TAG));
        sender.setUser(jsonSender.getBoolean(IS_USER_TAG));
        return sender;
    }



}
