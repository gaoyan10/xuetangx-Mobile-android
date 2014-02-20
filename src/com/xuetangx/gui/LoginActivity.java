package com.xuetangx.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.xuetangx.sqlite.CourseDBManager;
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
			if (progressBar.VISIBLE == View.VISIBLE) {
				progressBar.setVisibility(View.GONE);
			}
			if (msg.what == 0) {
				//success
				CourseDBManager db = new CourseDBManager(LoginActivity.this);
				db.getDatabase();
				List<HashMap<String,String>> data = db.queryEnrollment(Utils.getUserName());
				Intent intent = new Intent (LoginActivity.this, MainActivity.class);
				intent.putExtra("data",(ArrayList)data);
				startActivity(intent);
				db.closeDB();
				
			}
			if(msg.what == -1) {
				Toast.makeText(LoginActivity.this, msg.obj.toString(),Toast.LENGTH_LONG).show();
			}
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
						String suc = analyzer.analyseJson(msg.message, msg.code);
						Message message = loginHandler.obtainMessage();
						if (suc.equals("success")) {
							message.what = 0;
						}else {
							message.what = -1;
							message.obj = suc;
						}
						loginHandler.sendMessage(message);
					}
				}.start();
			}else {
				Toast.makeText(this, this.getResources().getString(R.string.login_error_email), Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	public void register(View view) {
		
	}
	public void getBackPassword(View view) {
		Intent intent = new Intent(this, GoToBrowserActivity.class);
		Bundle b = new Bundle();
		b.putString("title", this.getResources().getString(R.string.forget_password_tips));
		intent.putExtras(b);
		startActivity(intent);
	}
	
}
