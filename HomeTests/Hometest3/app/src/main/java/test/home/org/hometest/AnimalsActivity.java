package test.home.org.hometest;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.List;

import test.home.org.hometest.comm.IAnimalsDataListener;
import test.home.org.hometest.comm.JsonGetterAsyncTask;
import test.home.org.hometest.data.AnimalData;
import test.home.org.hometest.data.JsonSharedPrefsWrapper;


public class AnimalsActivity extends ActionBarActivity implements View.OnTouchListener, AbsListView.OnScrollListener,IAnimalsDataListener{


    private ProgressDialog waitDialog;
    private JsonGetterAsyncTask jsonGetterTask;
    private ListView left, right;

	private static final String TAG = "MainActivity";

	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		left.dispatchTouchEvent(event);
		return false;
	}


	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		View v=view.getChildAt(0);
		if(v != null){
			right.setSelectionFromTop(firstVisibleItem, v.getTop());
		}
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animals_list_screen);
        left = (ListView)findViewById(R.id.listOne);
		left.setOnScrollListener(this);
        right = (ListView)findViewById(R.id.listTwo);
		right.setOnTouchListener(this);
		findViewById(R.id.spacer).setMinimumHeight(getResources().getDimensionPixelSize(R.dimen.image_height)/2);
    }

	@Override
	protected void onResume() {
		super.onResume();
		showWaitDialog();
		JsonSharedPrefsWrapper JsonPrefs = getAnimalsApplication().getJsonSharedPrefs();
		jsonGetterTask = new JsonGetterAsyncTask(JsonPrefs, this);
		jsonGetterTask.execute();
	}

	private void showWaitDialog(){
        DialogInterface.OnCancelListener cancelListener = new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if( jsonGetterTask != null ){
                    jsonGetterTask.cancel(true);
                    finish();
                }
            }
        };
        waitDialog = ProgressDialog.show(this, "" , getString(R.string.wait_dialog_msg), true, true, cancelListener);
    }

    private void dismissWaitDialog(){
        if( waitDialog != null && waitDialog.isShowing() ){
            waitDialog.dismiss();;
            waitDialog = null;
        }
    }

    public AnimalsApplication getAnimalsApplication(){
        return (AnimalsApplication)getApplication();
    }

    @Override
    public void onAnimalsDataresult(int errorcode, List<AnimalData> parsedDataLeft, List<AnimalData> parsedDataRight) {
		Log.d(TAG, "got result");
        dismissWaitDialog();
        jsonGetterTask = null;
		Log.d(TAG, "error code: " + errorcode);
        if(errorcode != HttpURLConnection.HTTP_OK){

            Toast.makeText(this, getString(R.string.err_json_get), Toast.LENGTH_LONG);
            finish();
        }else{
			LruCache<String, Bitmap> imgCache = getAnimalsApplication().getImageCache();
			Log.d(TAG, "size of parsed data left: " +parsedDataLeft.size());
			AnimalsAdapter adapterLeft = new AnimalsAdapter(this, parsedDataLeft, imgCache);
			AnimalsAdapter adapterRight = new AnimalsAdapter(this, parsedDataRight, imgCache);
			left.setAdapter(adapterLeft);
			right.setAdapter(adapterRight);
        }
    }
}
