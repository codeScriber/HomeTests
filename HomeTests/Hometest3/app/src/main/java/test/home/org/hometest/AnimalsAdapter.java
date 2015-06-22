package test.home.org.hometest;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.List;

import test.home.org.hometest.comm.GetAndProcessImageTask;
import test.home.org.hometest.data.AnimalData;

/**
 * Created by Eyal on 02-2-15.
 */
public class AnimalsAdapter extends BaseAdapter {
    private List<AnimalData> data;
    private Context activityContext;
	private android.support.v4.util.LruCache<String, Bitmap> imgCache;
	private static final String TAG  = "adapter";

    public AnimalsAdapter(Context context, List<AnimalData> data, android.support.v4.util.LruCache<String, Bitmap> imgCache){
        this.activityContext = context;
        this.data = data;
		this.imgCache = imgCache;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public AnimalData getItem(int position) {
        return data == null ? null : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "getting view for position: " + position + " view null ? " + (convertView == null));
		ViewSwitcher cellInList = null;
        if(convertView == null ){
            cellInList = inflateNewView(parent);
        }else{
            cellInList = (ViewSwitcher)convertView;
        }
		ViewHolder holder = (ViewHolder)cellInList.getTag();
		AnimalData animalData = data.get(position);
		holder.textView.setText(animalData.getName());
		holder.animalName = animalData.getName();
		if( imgCache.get(animalData.getName()) != null ){
			holder.imageView.setImageBitmap(imgCache.get(animalData.getName()));
			if(cellInList.getCurrentView().getId() == R.id.waiting) {
				cellInList.showNext();
			}
			Log.d(TAG, "setting image for: position" + position + ", name: "+ holder.animalName);
		}else{
			GetAndProcessImageTask task = new GetAndProcessImageTask(activityContext, animalData,imgCache, cellInList);
			task.execute();
		}
        return cellInList;
    }

    private ViewSwitcher inflateNewView(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(activityContext);
        ViewSwitcher switcher = (ViewSwitcher)inflater.inflate(R.layout.animal_cell_layout, parent, false);
        ViewHolder holder = new ViewHolder();
        holder.imageView = (ImageView)switcher.findViewById(R.id.image);
        holder.textView = (TextView)switcher.findViewById(R.id.name);
        switcher.setTag(holder);
        return switcher;
    }

    public static class ViewHolder{
        public ImageView imageView;
        public TextView textView;
		public volatile String animalName;//for verification of image load.
    }
}
