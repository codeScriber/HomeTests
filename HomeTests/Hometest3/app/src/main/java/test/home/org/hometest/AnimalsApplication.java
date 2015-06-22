package test.home.org.hometest;

import android.app.Application;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import test.home.org.hometest.data.JsonSharedPrefsWrapper;

/**
 * Created by Eyal on 02-2-15.
 */
public class AnimalsApplication extends Application {

    private JsonSharedPrefsWrapper jsonSharedPrefs;
	LruCache<String, Bitmap> imageCache;
	private static final String TAG = "app";

    @Override
    public void onCreate() {
        super.onCreate();
        jsonSharedPrefs = new JsonSharedPrefsWrapper(this);
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

		// Use 1/8th of the available memory for this memory cache.
		final int cacheSize = maxMemory / 8;
		Log.i(TAG, "cache size set to: " + cacheSize);
		imageCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
			}
		};
    }

    public JsonSharedPrefsWrapper getJsonSharedPrefs(){
        return jsonSharedPrefs;
    }
	public LruCache<String, Bitmap> getImageCache(){
		return imageCache;
	}
}
