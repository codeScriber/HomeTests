package testme.com.myapplication.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.List;

import testme.com.myapplication.ChatApplication;
import testme.com.myapplication.ChatSimulationAdapter;
import testme.com.myapplication.R;
import testme.com.myapplication.pojo.Message;
import testme.com.myapplication.processros.MessagesPareser;


public class ChatActivity extends ActionBarActivity {

    private ListView mChatList;
    private ChatSimulationAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mChatList = (ListView)findViewById(R.id.chat_list);
        mAdapter = new ChatSimulationAdapter(this,getChatApp().getImageLoader());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    protected ChatApplication getChatApp(){
        return (ChatApplication)getApplication();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private  class JsonLoader extends AsyncTask<Void, Void, List<Message>> {
        @Override
        protected List<Message> doInBackground(Void... params) {
            try {
                InputStream jsonStream = getAssets().open("char_sim.json");
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
            return null;
        }

        @Override
        protected void onPostExecute(List<Message> messages) {
            super.onPostExecute(messages);
            mAdapter.setMessages(messages);
            mAdapter.notifyDataSetChanged();
        }
    }
}
