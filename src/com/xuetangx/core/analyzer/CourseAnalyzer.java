package com.xuetangx.core.analyzer;

import java.util.HashMap;

import android.content.Context;

import com.xuetangx.core.connect.NetConnector;
import com.xuetangx.core.connect.ResponseMessage;
import com.xuetangx.util.ConstantUtils;
import com.xuetangx.util.Utils;

public class CourseAnalyzer implements Analyzer {
	private String course;
	private Context context;
	public CourseAnalyzer(String c, Context context) {
		course = c;
		this.context = context;
	}
	@Override
	public Object analyseJson(String json, int code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createJson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseMessage connect() {
		// TODO Auto-generated method stub
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(ConstantUtils.KEY, Utils.getAPIKey(context));
		params.put(ConstantUtils.ACCESS, Utils.getAccessToken());
		NetConnector con = NetConnector.getInstance();
		ResponseMessage msg = con.httpsGet(ConstantUtils.URL + ConstantUtils.GET_ENROLL_COURSES, params);
		return msg;
		return null;
	}

}
