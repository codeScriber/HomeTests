package test.home.org.hometest.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Eyal on 02-2-15.
 */
public class JsonSharedPrefsWrapper {
    private static final String SHARED_PREFS_NAME = "animals_json";
    private static final String ANIMAL_LIST_KEY = "animals";
    private Context appContext;

    public JsonSharedPrefsWrapper(Context context){
        this.appContext = context.getApplicationContext();
    }

    public boolean hasAnimalData(){
        return ! TextUtils.isEmpty( getJsonString());
    }

    public void saveJson(String jsonString){
        SharedPreferences.Editor editor =  appContext.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(ANIMAL_LIST_KEY, jsonString).commit();
    }

    public String getJsonString(){
        return appContext.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE).getString(ANIMAL_LIST_KEY, "");
    }

    public List<AnimalData> parseAnimalsJson(){
        List<AnimalData> animalsData = null;
        try{
            String jsonString = getJsonString();
            JSONObject json = new JSONObject(jsonString);
            JSONArray animalsArray = json.getJSONArray("animals");
            int len = animalsArray.length();
            animalsData = new ArrayList<>(len);
            for(int i = 0; i < len ; i++ ){
                JSONObject animalJson = animalsArray.getJSONObject(i);
                AnimalData animalData = new AnimalData();
                Iterator<String> temp = animalJson.keys();
                if( temp.hasNext() ){
                    animalData.setName(temp.next());
                    animalJson = animalJson.getJSONObject(animalData.getName());
                    animalData.setDescription(animalJson.getString("description"));
                    animalData.setImageUrl(animalJson.getString("image"));
                    animalsData.add(animalData);
                }else{
                    throw new JSONException("malformed json");
                }

            }
        }catch(JSONException jse){
            jse.printStackTrace();
        }
        return animalsData;
    }


}
