package test.home.org.hometest.comm;

import android.os.AsyncTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import test.home.org.hometest.data.AnimalData;
import test.home.org.hometest.data.JsonSharedPrefsWrapper;

/**
 * Created by Eyal on 02-2-15.
 */
public class JsonGetterAsyncTask extends AsyncTask<Void,Void,Void>{
    private static final String SERVER_URL = "https://s3.amazonaws.com/accells_res/mobile_test/animals.json";
    public static final int HTTP_EXCEPTION_STATUS = -1;

    private int lastHttpStatus;
    private JsonSharedPrefsWrapper jsonSharedPrefs;
    private WeakReference<IAnimalsDataListener> responseListener;
    private List<AnimalData> parsedDataLeft;
	private List<AnimalData> parsedDataRight;

    public JsonGetterAsyncTask(JsonSharedPrefsWrapper jsonSharedPrefs, IAnimalsDataListener responseListener){
        this.jsonSharedPrefs = jsonSharedPrefs;
        this.responseListener = new WeakReference<IAnimalsDataListener>(responseListener);
		parsedDataLeft = null;
		parsedDataRight = null;
        lastHttpStatus = HttpURLConnection.HTTP_OK;
    }


    @Override
    protected Void doInBackground(Void... params) {
        try {
            if( jsonSharedPrefs != null && ! jsonSharedPrefs.hasAnimalData()) {
                URL serverUrl = new URL(SERVER_URL);
                HttpsURLConnection connection = (HttpsURLConnection) serverUrl.openConnection();
                lastHttpStatus = connection.getResponseCode();
                if (lastHttpStatus == HttpURLConnection.HTTP_OK) {
                    InputStream in = connection.getInputStream();
                    String jsonString = inputStreamToString(in);
                    jsonSharedPrefs.saveJson(jsonString);
                }
            }
            List<AnimalData> temp = jsonSharedPrefs.parseAnimalsJson();
			parsedDataLeft = new ArrayList<>(temp.size()/2);
			parsedDataRight = new ArrayList<>(temp.size()/2 + temp.size()%2);
			for(int i = 0; i < temp.size(); i++ ){
				if( i % 2 == 0){
					parsedDataLeft.add(temp.get(i));
				}else{
					parsedDataRight.add(temp.get(i));
				}
			}
        }catch(Exception e){
            e.printStackTrace();
            lastHttpStatus = HTTP_EXCEPTION_STATUS;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void nothing) {
        super.onPostExecute(nothing);
        if( responseListener != null && responseListener.get() != null ){
            responseListener.get().onAnimalsDataresult(lastHttpStatus, parsedDataLeft, parsedDataRight);
        }
    }

    /**
     * reads the inputstream into string(json)
     * @param in
     * @return
     */
    private String inputStreamToString(InputStream in) {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[1024];
        try {
            final Reader reader = new InputStreamReader(in, "UTF-8");
            try {
                int readBytes = reader.read(buffer, 0, buffer.length);
                while( readBytes > 0){
                    builder.append(buffer, 0, readBytes);
                    readBytes = reader.read(buffer, 0, buffer.length);
                }
            } finally {
                in.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return builder.toString();
    }
}
