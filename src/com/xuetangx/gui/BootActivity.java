package com.xuetangx.gui;

import java.util.Timer;
import java.util.TimerTask;

import com.xuetangx.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
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
		
	}
}
