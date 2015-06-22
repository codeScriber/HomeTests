package test.home.org.hometest.comm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ViewSwitcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import test.home.org.hometest.AnimalsAdapter;
import test.home.org.hometest.R;
import test.home.org.hometest.data.AnimalData;

/**
 * Created by Eyal on 02-2-15.
 */
public class GetAndProcessImageTask extends AsyncTask<Void, Void, Bitmap> {
	private static final String BIG_IMG_SUFFIGX = "_big";
    private AnimalData animalData;
	private WeakReference<Context> context;
	private boolean hadError;
	private int imgWidth,imgHeight;
	private LruCache<String, Bitmap> imgCache;
	private WeakReference<ViewSwitcher> viewForBitmap;
	private static final String TAG = "ImageTask";


	public GetAndProcessImageTask(Context context, AnimalData animalData, LruCache<String, Bitmap> imgCache, ViewSwitcher view){
		this.context = new WeakReference<>(context);
		imgWidth = context.getResources().getDimensionPixelSize(R.dimen.image_width);
		imgHeight = context.getResources().getDimensionPixelSize(R.dimen.image_height);
		this.animalData = animalData;
		this.imgCache = imgCache;
		this.viewForBitmap = new WeakReference<ViewSwitcher>(view);
		hadError = false;
	}

	private File getImageCacheFile(Context context, AnimalData animalData, boolean isOrig){
		File result = null;
		File cacheDir = context.getCacheDir();
		String filename = String.format("%s%s", animalData.getName(), isOrig ? BIG_IMG_SUFFIGX: "");
		result = new File(cacheDir, filename);
		return result;
	}

    private void downloadImage(File bigImageCache)throws IOException{
		URL url = new URL(animalData.getImageUrl());
		HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
		if( connection.getResponseCode() == HttpURLConnection.HTTP_OK){
			InputStream in = connection.getInputStream();
			if( bigImageCache != null && ! isCancelled() ){
				FileOutputStream out = new FileOutputStream(bigImageCache);
				pump(in, out);
			}
		}
    }

	private void pump(InputStream in, OutputStream out)throws IOException{
		byte[] buffer = new byte[2048];
		int readBytes = in.read(buffer, 0 , buffer.length);
		while( readBytes > 0){
			out.write(buffer, 0, readBytes);
			readBytes = in.read(buffer, 0 , buffer.length);
		}
		try {
			in.close();
		}catch(Exception ignore){}
		out.flush();
		try {
			out.close();
		}catch(Exception ignore){}
	}

	private Bitmap loadSmallImage(File smallImageFile){
		return BitmapFactory.decodeFile(smallImageFile.getAbsolutePath());
	}

	private Bitmap createMnifiedImage(File smallImageFile, File bigImageFile){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(bigImageFile.getAbsolutePath(), options);
		options.inSampleSize = calculateInSampleSize(options, imgWidth, imgHeight);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(bigImageFile.getAbsolutePath(), options);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(smallImageFile);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
			// PNG is a lossless format, the compression factor (100) is ignored
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	/**
	 * from google's training...
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

    @Override
    protected Bitmap doInBackground(Void... params) {
		Context realContext = context == null ? null : context.get();
		File bigCacheFile = null;
		File smallImageFile = null;
		if( realContext != null ){
			smallImageFile = getImageCacheFile(realContext , animalData, false);
			bigCacheFile = getImageCacheFile(realContext , animalData, true);
		}
		Log.d(TAG, "looking for files: " + smallImageFile.getName() +", "+bigCacheFile.getName() );
		realContext = null;
		Bitmap bitmap = imgCache.get(animalData.getName());
		if( bitmap == null ) { //no mem cache
			Log.d(TAG, animalData.getName() + " not in cache, checking tuhmbnail");
			if (smallImageFile.exists()) {
				Log.d(TAG, smallImageFile.getName() + " exists, loading");
				//we have thumbnail, load it.
				bitmap = loadSmallImage(smallImageFile);
			} else if( bigCacheFile.exists()) {
				Log.d(TAG, smallImageFile.getName() + "does not exists, big file does, converting.");
				bitmap = createMnifiedImage(smallImageFile, bigCacheFile);
			}else{
				try {
					Log.d(TAG,"no files, downloading " + bigCacheFile.getName());
					downloadImage(bigCacheFile);
					Log.d(TAG,bigCacheFile.getName() + " download complete");
				} catch (Exception e) {
					e.printStackTrace();
					hadError = true;
				}
				Log.d(TAG,animalData.getName() + " had error ?" + hadError + " is cancell ? " + isCancelled());
				if (!hadError && !isCancelled()) {
					Log.d(TAG,animalData.getName() + " creating thumbnail");
					bitmap = createMnifiedImage(smallImageFile, bigCacheFile);
				}
			}
		}
		if( bitmap != null ){
			Log.d(TAG,animalData.getName() + " putting thumbnail in cache");
			imgCache.put(animalData.getName(), bitmap);
		}else{
			Log.d(TAG,animalData.getName() + " bitmap null, had error");
		}
        return bitmap;
    }

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		super.onPostExecute(bitmap);
		ViewSwitcher switcher = this.viewForBitmap.get();
		if (viewForBitmap != null ) {
			Log.d(TAG,animalData.getName() + " viewholder still exists");
			Log.d(TAG,animalData.getName() + " bitmap null ?" + (bitmap == null));
			AnimalsAdapter.ViewHolder holder = (AnimalsAdapter.ViewHolder) switcher.getTag();
			Log.d(TAG,animalData.getName() + " holder name: " +  holder.animalName);
			//views can be recycled, but then they'll have new data, make sure we place the right image in the right place.
			if( ((AnimalsAdapter.ViewHolder) switcher.getTag()).animalName == animalData.getName() ) {
				if (bitmap != null) {
					holder.imageView.setImageBitmap(bitmap);
				} else {
					holder.imageView.setImageResource(R.drawable.error_image_drawable);
				}
				if(switcher.getCurrentView().getId() == R.id.waiting) {
					switcher.showNext();
				}
			}
		}

	}
}
