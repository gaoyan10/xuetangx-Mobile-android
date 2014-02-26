package com.xuetangx.core.analyzer;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.xuetangx.core.connect.NetConnector;
import com.xuetangx.core.connect.ResponseMessage;
import com.xuetangx.sqlite.CourseDBManager;
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
		if (code == 200) {
			CourseDBManager db = new CourseDBManager(context, 0, ConstantUtils.COURSE_DATA_DETAIL);
			try{
				JSONObject obj = new JSONObject(json);
				if(obj.get("status").equals("success")) {
					String data = obj.get("course").toString();
					if(db.refreshData(data, course)) {
						return data;
					}
				}else {
					return -1;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return -1;
			}
			finally{
				db.closeDB();
			}
			
		}else {
			if(code == 403) {
				//unenrollment.
				return 403;
			}else{
				
			}
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
		ResponseMessage msg = con.httpGet(ConstantUtils.URL + ConstantUtils.COURSE_NAVIGATION + course, params);
		return msg;
	}
	public void productData(String json) {
		
	}

}
