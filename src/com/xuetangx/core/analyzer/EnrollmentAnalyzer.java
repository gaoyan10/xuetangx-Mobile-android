package com.xuetangx.core.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.xuetangx.core.connect.NetConnector;
import com.xuetangx.core.connect.ResponseMessage;
import com.xuetangx.sqlite.CourseDBManager;
import com.xuetangx.sqlite.DBHelper;
import com.xuetangx.util.ConstantUtils;
import com.xuetangx.util.Utils;

public class EnrollmentAnalyzer implements Analyzer{
	private Context context;
	public EnrollmentAnalyzer(Context c) {
		context = c;
	}
	@Override
	public Object analyseJson(String json, int code) {
		// TODO Auto-generated method stub
		if (code == 200) {
			CourseDBManager db = new CourseDBManager(context);
			try{
				List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
				db.getDatabase();
				JSONObject obj = new JSONObject(json);
				JSONArray courses = obj.getJSONArray("enrollments");
				for(int i = 0 ; i < courses.length(); i ++) {
					JSONObject item = (JSONObject)courses.get(i);
					HashMap<String, Object> one = new HashMap<String, Object>();
					one.put("course_id", item.getString("course_id"));
					one.put("display_name", item.getString("display_name"));
					one.put("display_org", item.getString("display_org"));
					one.put("display_coursenum", item.getString("display_coursenum"));
					one.put("start", item.getString("start"));
					one.put("course_image_url", item.getString("course_image_url"));
					data.add(one);
				}
				if(db.refreshEnrollment(data, Utils.getUserName())) {
					//refresh data ok.
					return data;
				}
			}catch(Exception e) {
				return -1;
			}finally {
				db.closeDB();
			}
		}else {
			if(code == 403) {
				return -3;
			}
		}
		return -2;
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
		ResponseMessage msg = con.httpGet(ConstantUtils.GET_ENROLL_COURSES, params);
		return msg;
	}
	
}
