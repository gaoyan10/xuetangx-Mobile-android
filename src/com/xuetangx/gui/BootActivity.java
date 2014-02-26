package com.xuetangx.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.xuetangx.R;
import com.xuetangx.core.background.BootImageReceiver;
import com.xuetangx.sqlite.CourseDBManager;
import com.xuetangx.util.ConstantUtils;
import com.xuetangx.util.PreferenceUtils;
import com.xuetangx.util.Utils;
/**
 * The application boot page.
 * loading data for mainActivity.
 * @author gaoyansansheng@gmail.com.
 *
 */
public class BootActivity extends Activity {
	private RelativeLayout newPage;
	private RelativeLayout defaultPage;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boot);
		newPage = (RelativeLayout)findViewById(R.id.boot_page_image);
		defaultPage = (RelativeLayout) findViewById(R.id.boot_default_layout);
		checkNewImage();
		openLoginActivity();
	    setAnimation();
	}
	public void setAnimation() {
		Animation bootAnimation = AnimationUtils.loadAnimation(this, R.anim.boot_animation);
		if(newPage.getVisibility() == View.VISIBLE) {
			newPage.startAnimation(bootAnimation);
		}else {
			defaultPage.startAnimation(bootAnimation); // an error.
		}
	}
	public void startActivity() {
		Intent intent = new Intent(this, LoginActivity.class);
		Bundle b = new Bundle();
		b.putString("username", Utils.getUserName());
		intent.putExtras(b);
		startActivity(intent);
		this.finish();
	}
	public void openLoginActivity() {
		Utils.initialUserMessage(this);
		if (Utils.getAccessToken() != null) { //access token 未过期。
			CourseDBManager db = new CourseDBManager(BootActivity.this);
			db.getDatabase();
			List<HashMap<String,String>> data = db.queryEnrollment(Utils.getUserName());
			Intent intent = new Intent (BootActivity.this, MainActivity.class);
			intent.putExtra("data",(ArrayList)data);
			startActivity(intent); 
			db.closeDB();
			this.finish();
		}else{
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					startActivity();
				}
				
			}, 1500l);
		}
		
	}
	/**
	 * check new image and change the boot image.
	 * download the new image if it exists.
	 */
	public void checkNewImage() {
	    PreferenceUtils pre = new PreferenceUtils(this, ConstantUtils.DEFAULT_PRE);
	    ConstantUtils.URL = pre.getPreference("url", "https://59.66.131.141:3000/");
	    boolean isNewImage = pre.getBoolean(ConstantUtils.BOOT_IMAGE_DISPLAY, false);
	    if(isNewImage) {
	    	File file = new File(ConstantUtils.BOOT_IMAGE);
	    	if (file.exists()) {
	    		Bitmap bitmap = BitmapFactory.decodeFile(this.getExternalCacheDir().getAbsolutePath()  + "/" + ConstantUtils.BOOT_IMAGE);
	    		newPage.setVisibility(View.VISIBLE);
		    	defaultPage.setVisibility(View.GONE);
		    	newPage.setBackgroundDrawable(new BitmapDrawable(bitmap));
	    	}
	    	
	    }
	    Thread downThread = new Thread() {
	    	public void run() {
	    		BootImageReceiver.downloadBootImage(BootActivity.this);
	    	}
	    };
	    downThread.start();
	}
}
