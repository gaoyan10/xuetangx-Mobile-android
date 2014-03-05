package com.xuetangx.gui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xuetangx.R;
import com.xuetangx.plugin.DefaultWebView;
/**
 * 
 * @author gaoyansansheng@gmail.com
 *
 */
public class AboutActivity extends Activity {
	private DefaultWebView webView;
	private ProgressBar progress;
	private TextView titleText;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String url = getIntent().getStringExtra("startPage");
		String title = getIntent().getStringExtra("title");
		setContentView(R.layout.activity_about);
		WebView web = (WebView)findViewById(R.id.about_webview);
		progress = (ProgressBar) findViewById(R.id.about_title_progress);
		titleText = (TextView) findViewById(R.id.about_title_text);
		if (title != null) {
			titleText.setText(title);
		}
		if(url != null) {
			webView = new DefaultWebView(web, this, url, progress);
		}else{
			url = this.getResources().getString(R.string.error_page);
			webView = new DefaultWebView(web, this, url, progress);
		}
		webView.setBrowser(true, true, true,null);
		
	}
	public void backButton(View v) {
		this.finish();
	}
	
}
