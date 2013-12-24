package com.xuetangx.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.xuetangx.R;
import com.xuetangx.ui.ClearEditText;
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
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
}
