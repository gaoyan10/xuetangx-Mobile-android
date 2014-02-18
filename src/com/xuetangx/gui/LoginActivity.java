package com.xuetangx.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xuetangx.R;
import com.xuetangx.core.analyzer.LoginAnalyzer;
import com.xuetangx.core.connect.ResponseMessage;
import com.xuetangx.ui.ClearEditText;
import com.xuetangx.util.Utils;
/**
 * login activity.
 * if the secret token is expired, the user should login on this page.
 * @author gaoyansansheng@gmail.com
 *
 */
public class LoginActivity extends Activity {
	private RelativeLayout progressBar;
    private ClearEditText username, password;
    private LoginAnalyzer analyzer;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		progressBar = (RelativeLayout)findViewById(R.id.login_loading_layout);
		username = (ClearEditText) findViewById(R.id.login_user);
		password = (ClearEditText) findViewById(R.id.login_password);
		Bundle b = getIntent().getExtras();
		if(b != null && b.getString("username") != null && b.getString("username").length() > 0) {
			username.setText(b.getString("username"));
		}
		analyzer = new LoginAnalyzer(this);
	}
	private Handler loginHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
		}
	};
	public void login(View v) {
		String userName = username.getText().toString();
		String passWord = password.getText().toString();
		if(userName.length() == 0 || passWord.length() == 0) {
			Toast.makeText(this, this.getResources().getString(R.string.login_error_input), Toast.LENGTH_SHORT).show();
		} else {
			if(Utils.checkEmail(userName)) {
				progressBar.setVisibility(View.VISIBLE);
				analyzer.setContent(userName, passWord);
				new Thread() {
					public void run() {
						ResponseMessage msg = analyzer.connect();
						boolean suc = analyzer.analyseJson(msg.message, msg.code);
						Message message = loginHandler.obtainMessage();
						if (suc) {
							message
						}
					}
				};
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
				
			}else {
				Toast.makeText(this, this.getResources().getString(R.string.login_error_email), Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
}
