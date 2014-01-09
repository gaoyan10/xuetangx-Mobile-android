package com.xuetangx.core.analyzer;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginAnalyzer implements Analyzer {

	@Override
	public boolean analyseJson(String json, int code) {
		// TODO Auto-generated method stub
		try {
			if(code == 200) {
				JSONObject obj = new JSONObject(json);
			}else{
				//django-oauth2-provider
				
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String createJson() {
		// TODO Auto-generated method stub
		return null;
	}

}
