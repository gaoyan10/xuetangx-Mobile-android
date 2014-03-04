package com.xuetangx.gui;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuetangx.R;

public class UpdateDetailActivity extends Activity {
	private TextView title, content;
	private RelativeLayout layout;
	private ProgressBar loading;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_detail);
		findViews();
		getBundleData();
	}
	public void findViews() {
		layout = (RelativeLayout) findViewById(R.id.update_detail_title);
		loading = (ProgressBar) layout.findViewById(R.id.title_progress);
		loading.setVisibility(View.GONE);
		title = (TextView) layout.findViewById(R.id.title_text);
		content = (TextView) findViewById(R.id.update_detail_content);
	}
	public void getBundleData() {
		Bundle b = getIntent().getExtras();
		if (b != null) {
			title.setText(b.getString("title"));
			content.setText(Html.fromHtml("<html>" + b.getString("content") + "</html>"));
		}
		
	}
	public void backButton(View view) {
		this.finish();
	}
}
