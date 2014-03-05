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
import com.xuetangx.sqlite.CourseDBManager;
import com.xuetangx.util.ConstantUtils;
import com.xuetangx.util.Utils;

public class AllCourseAnalyzer implements Analyzer {
	private Context context;
	private String username;

	public AllCourseAnalyzer(Context context, String username) {
		this.context = context;
		this.username = username;
	}

	@Override
	public Object analyseJson(String json, int code) {
		// TODO Auto-generated method stub
		CourseDBManager db = null;
		try {
			
			if (code == 200) {
				List<ContentValues> courses = new ArrayList<ContentValues>();
				JSONObject obj = new JSONObject(json);
				if (obj.getString("status").equals("success")) {
					JSONArray list = obj.getJSONArray("courses");
					for (int i = 0; i < list.length(); i++) {
						JSONObject item = list.getJSONObject(i);
						// HashMap<String, Object> map = new HashMap<String,
						// Object>();
						ContentValues map = new ContentValues();
						map.put("course_id", item.getString("course_id"));
						map.put("display_name", item.getString("display_name"));
						map.put("display_org", item.getString("display_org"));
						map.put("display_coursenum",
								item.getString("display_coursenum"));
						map.put("start", item.getString("start"));
						map.put("advertised_start",
								item.getString("advertised_start"));
						map.put("short_description",
								item.getString("short_description"));
						map.put("course_image_url",
								item.getString("course_image_url"));
						map.put("marketing_video_url",
								item.getString("marketing_video_url"));
						map.put("username", username);
						courses.add(map);
					}
					db = new CourseDBManager(context);
					db.getDatabase();
					db.refreshCourseList(courses);
				}
			}
			//return db.queryCourseList(username);
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				List data = db.queryCourseList(username);
				db.closeDB();
				return data;
			}
		}
		return  -1;

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
		ResponseMessage msg = con.httpGet(ConstantUtils.URL
				+ ConstantUtils.GET_ALL_COURSES, params);
		return msg;
	}

}
