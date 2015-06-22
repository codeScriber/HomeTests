package testme.com.myapplication.async;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.List;

import testme.com.myapplication.pojo.Message;
import testme.com.myapplication.processros.JsonMessageParser;
import testme.com.myapplication.processros.MessagesPareser;

/**
 * Created by Eyal on 29-4-15.
 */
public class JsonLoader extends AsyncTask<Void, Void, List<Message>> {
    private WeakReference<Context> mWekContext;

    public JsonLoader(Context context){
        mWekContext = new WeakReference<Context>(context);
    }

    @Override
    protected List<Message> doInBackground(Void... params) {
        final Context context = mWekContext == null ? null : mWekContext.get();
        if( context != null ){
            try {
                InputStream jsonStream = context.getAssets().open("char_sim.json");
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(jsonStream, "UTF-8"));
                StringBuilder result = new StringBuilder();
                String inputStr;
                while ((inputStr = streamReader.readLine()) != null) {
                    result.append(inputStr);
                }
                JSONObject json = new JSONObject(result.toString());
                List<Message> messages = MessagesPareser.jsonToMessages(json);
                return messages;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }



}
