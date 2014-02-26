package com.xuetangx.core.analyzer;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.xuetangx.R;
import android.content.Context;

import com.xuetangx.core.connect.NetConnector;
import com.xuetangx.core.connect.ResponseMessage;
import com.xuetangx.sqlite.UserDBManager;
import com.xuetangx.util.ConstantUtils;
import com.xuetangx.util.Utils;

public class LoginAnalyzer implements Analyzer {
	private String username;
	private String password;
	private Context context;
	public LoginAnalyzer(Context c) {
		context = c;
	}
	public LoginAnalyzer(String u, String p) {
		username = u;
		password = p;
	}
	public void setContent(String u, String p) {
		username = u;
		password = p;
	}
	@Override
	public String analyseJson(String json, int code) {
		// TODO Auto-generated method stub
		try {
			if(code == 200) {
				JSONObject obj = new JSONObject(json);
				String accessToken = obj.getString("access_token");
				String scope = obj.getString("scope");
				String expire = obj.getString("expires_in");
				String refreshToken = obj.getString("refresh_token");
				String time = String.valueOf(System.currentTimeMillis());
				String[] data = new String[]{username, accessToken, scope, expire, refreshToken, time};
				UserDBManager manager = UserDBManager.getUserDBManager(context);
				manager.insertAccess(data);
				manager.closeDB();
				Utils.initialUserMessage(username, accessToken);
				return "success";
			}else{
				if(code == 400) {
					JSONObject obj = new JSONObject(json);
					String err = obj.getString("error");
					if (err.equals("invalid_client")) {
						return context.getResources().getString(R.string.invalid_client);
					}
					if (err.equals("invalid_grant")) {
						return context.getResources().getString(R.string.invalid_grant);
					}
					
				}
				//django-oauth2-provider
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return context.getResources().getString(R.string.json_syntax_error);
			//e.printStackTrace();
		}catch(Exception e) {
			
		}
		return context.getResources().getString(R.string.json_status_error);
	}

	@Override
	public String createJson() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResponseMessage connect() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(ConstantUtils.KEY, Utils.getAPIKey(context));
		params.put("Content-Type", "application/x-www-form-urlencoded");
		StringBuilder sb = new StringBuilder();
		sb.append("client_id=5dd0cac824fe2f9b2f7d&client_secret=c2f7c92ccd29d3da54f2b045661ea657f29a9303&grant_type=password&");
		sb.append("&username=" + username + "&password=" + password);
		NetConnector net = NetConnector.getInstance();
		ResponseMessage response = net.httpPost(ConstantUtils.URL + ConstantUtils.LOGIN, sb.toString(), params);
		return response;
	}

}
