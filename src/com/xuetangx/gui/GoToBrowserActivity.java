package com.xuetangx.gui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xuetangx.R;

public class GoToBrowserActivity extends Activity {
	private TextView title;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gotobrowser);
		title = (TextView) findViewById(R.id.forget_title);
		Bundle b = getIntent().getExtras();
		if(b != null && b.getString("title") != null) {
			title.setText(b.getString("title"));
		}
	}
	public void closeBtn(View v){
		this.finish();
	}

}
