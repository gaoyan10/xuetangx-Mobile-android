package com.xuetangx.gui;

import java.io.File;
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
import com.xuetangx.util.ConstantUtils;
import com.xuetangx.util.PreferenceUtils;
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
		startActivity(intent);
		this.finish();
	}
	public void openLoginActivity() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				startActivity();
			}
			
		}, 2500l);
	}
	public void checkNewImage() {
	    PreferenceUtils pre = new PreferenceUtils(this, ConstantUtils.USER_PRE);
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
	}
}
