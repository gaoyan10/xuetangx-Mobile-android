package com.xuetangx.core.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;

import com.xuetangx.core.connect.NetConnector;
import com.xuetangx.core.connect.ResponseMessage;
import com.xuetangx.util.ConstantUtils;
import com.xuetangx.util.Utils;

public class UpdateAnalyzer implements Analyzer {
	private String courseID;
	private Context context;
	public UpdateAnalyzer(String courseID, Context context) {
		this.courseID = courseID;
		this.context = context;
	}
	@Override
	public Object analyseJson(String json, int code) {
		// TODO Auto-generated method stub
		if (code == 200) {
			try {
				JSONObject updates = new JSONObject(json);
				JSONArray list = updates.getJSONArray("updates");
				List<ContentValues> data = new ArrayList<ContentValues>();
				for (int i = 0;i < list.length(); i ++) {
					ContentValues value = new ContentValues();
					JSONObject obj = list.getJSONObject(i);
				//	value.put("time", obj.getString("time"));
					String id = obj.getString("id");
					value.put("id", id);
					String no = id.substring(id.lastIndexOf("/") + 1, id.length());
					value.put("no", Integer.valueOf(no));
					value.put("content", obj.getString("content"));
					value.put("date", obj.getString("date"));
					value.put("course_id", courseID);
					data.add(value);
				}
				return data;
			}catch(JSONException e) {
				e.printStackTrace();
			}
		}else {
			return -1;
		}
		return -1;
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
		ResponseMessage msg = con.httpGet(ConstantUtils.URL + ConstantUtils.UPDATE + courseID, params);
		return msg;
	}

}
