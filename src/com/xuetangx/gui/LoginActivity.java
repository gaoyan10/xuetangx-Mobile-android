package com.xuetangx.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xuetangx.R;
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
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		progressBar = (RelativeLayout)findViewById(R.id.login_loading_layout);
		username = (ClearEditText) findViewById(R.id.login_user);
		password = (ClearEditText) findViewById(R.id.login_password);
	}
	public void login(View v) {
		String userName = username.getText().toString();
		String passWord = password.getText().toString();
		if(userName.length() == 0 || passWord.length() == 0) {
			Toast.makeText(this, this.getResources().getString(R.string.login_error_input), Toast.LENGTH_SHORT).show();
		} else {
			if(Utils.checkEmail(userName)) {
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
				
			}else {
				Toast.makeText(this, this.getResources().getString(R.string.login_error_email), Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
}
