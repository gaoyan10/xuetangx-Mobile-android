package com.xuetangx.sqlite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.xuetangx.util.ConstantUtils;

public class CourseDBManager extends DBManager {
	private CourseDBHelper helper;

	public CourseDBManager(Context c) {
		helper = new CourseDBHelper(c);
		db = helper.getWritableDatabase();
		// TODO Auto-generated constructor stub
	}

	public CourseDBManager(Context c, int mode, String dbname) {
		if (dbname.equals(ConstantUtils.COURSE_DATA_DETAIL)) {
			CourseDataHelper helper = new CourseDataHelper(c);
			db = helper.getWritableDatabase();
		}
	}

	public void getDatabase() {
		if (!db.isOpen()) {
			db = helper.getWritableDatabase();
		}
	}

	public boolean refreshData(String json, String courseID) {
		getDatabase();
		ContentValues value = new ContentValues();
		value.put("course_data", json);
		value.put("course_id", courseID);
		value.put("time", System.currentTimeMillis());
		int num = db.update(ConstantUtils.T_COURSE_DATA, value,
				"course_id = ?", new String[] { courseID });
		if (num == 0) {
			long num2 = db.insert(ConstantUtils.T_COURSE_DATA, null, value);
			if (num2 == -1) {
				return false;
			} else {
				return true;
			}
		}
		return true;
	}

	public int refreshCourseList(List<ContentValues> data) {
		//return refresh(data, ConstantUtils.T_COURSE);
		try {
			db.beginTransaction();
			db.execSQL("delete from " + ConstantUtils.T_COURSE);
			for(int i = 0; i < data.size(); i ++) {
				ContentValues item = data.get(i);
				db.insert(ConstantUtils.T_COURSE, null, item);
			}
			db.setTransactionSuccessful();
			return 0;
		}catch(Exception e) {
			e.printStackTrace();
			return -1;
		}finally {
			db.endTransaction();
		}
	}
	private boolean refresh(List<HashMap<String, Object>> data, String table) {
		getDatabase();
		db.beginTransaction();
		try {
			db.execSQL("delete from " + table);
			for (HashMap<String, Object> item : data) {
				ContentValues values = new ContentValues();
				Iterator iter = item.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					values.put(entry.getKey().toString(), entry.getValue()
							.toString());
				}
				db.insert(table, null, values);
			}
			db.setTransactionSuccessful();
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			db.endTransaction();
		}
	}

	public boolean refreshEnrollment(List<HashMap<String, Object>> data,
			String username) {
		for (HashMap<String, Object> item : data) {
			item.put("username", username);
		}
		return refresh(data, ConstantUtils.T_ENROLLMENT);

	}

	public List<HashMap<String, String>> queryUpdateList(String courseID, String limit) {
		List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		Cursor c = null;
		try {
			c = db.query(ConstantUtils.T_UPDATE ,null, "course_id = ?", new String[]{courseID}, null, null, "no desc", limit);
			if (c.getCount() <= 0) {
				return data;
			}else {
				c.moveToLast();
				do {
					HashMap<String ,String> item = new HashMap<String, String>();
					item.put("course_id", c.getString(c.getColumnIndex("course_id")));
					item.put("id", c.getString(c.getColumnIndex("id")));
					item.put("content", c.getString(c.getColumnIndex("content")));
					item.put("date", c.getString(c.getColumnIndex("date")));
					data.add(item);
				}while(c.moveToPrevious());
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if (c != null) {
				c.close();
			}
		}
		return data;
	}
	public int refreshUpdate(List<ContentValues> data) {
		try {
			db.beginTransaction();
			db.execSQL("delete from " + ConstantUtils.T_UPDATE);
			for(int i = 0; i < data.size(); i ++) {
				ContentValues item = data.get(i);
				item.put("is_show", 1);
			//	int count = db.update(ConstantUtils.T_UPDATE, item, "id=?", new String[]{item.get("id").toString()});
			//	if (count <= 0) {
					item.put("is_show", 0);
					db.insert(ConstantUtils.T_UPDATE, null, item);
			//	}
			}
			db.setTransactionSuccessful();
			return 0;
		}catch(Exception e) {
			e.printStackTrace();
			return -1;
		}finally {
			db.endTransaction();
		}
	}
	public List<HashMap<String, Object>> queryCourseList(String username) {
		List courseList = new ArrayList<HashMap<String, String>>();
		Cursor c = null;
		try {
			c = db.query(ConstantUtils.T_COURSE, null, "username = ?",
					new String[] { username }, null, null, null);
			c.moveToFirst();
			while (!c.isAfterLast()) {
				courseList.add(getCourseItem(c));
				c.moveToNext();
			}
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return courseList;
	}
	public HashMap<String, Object> getCourseItem(Cursor c) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("course_id", c.getString(c.getColumnIndex("course_id")));
			map.put("display_name",c.getString(c.getColumnIndex("display_name")));
			map.put("display_org", c.getString(c.getColumnIndex("display_org")));
			map.put("display_coursenum", c.getString(c.getColumnIndex("display_coursenum")));
			map.put("start", c.getString(c.getColumnIndex("start")));
			map.put("advertised_start", c.getString(c.getColumnIndex("advertised_start")));
			map.put("short_description", c.getString(c.getColumnIndex("short_description")));
			map.put("course_image_url", c.getString(c.getColumnIndex("course_image_url")));
			map.put("marketing_video_url", c.getString(c.getColumnIndex("marketing_video_url")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public HashMap<String, Object> queryOneCourseData(String courseID) {
		Cursor c = null;
		HashMap<String, Object> data = new HashMap<String, Object>();
		try {
			c = db.query(ConstantUtils.T_COURSE_DATA, new String[] {
					"course_data", "time" }, "course_id = ?",
					new String[] { courseID }, null, null, null, "1");
			if (c.getCount() != 1) {
				return null;
			} else {
				c.moveToFirst();
				data.put("json", c.getString(c.getColumnIndex("course_data")));
				data.put("time", c.getLong(c.getColumnIndex("time")));
				return data;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return null;
	}

	public HashMap<String, String> getEnrollmentItem(Cursor c) {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			map.put("course_id", c.getString(c.getColumnIndex("course_id")));
			map.put("display_name",
					c.getString(c.getColumnIndex("display_name")));
			map.put("display_org", c.getString(c.getColumnIndex("display_org")));
			map.put("start", c.getString(c.getColumnIndex("start")));
			map.put("course_image_url",
					c.getString(c.getColumnIndex("course_image_url")));
		} catch (Exception e) {

		}
		return map;
	}

	public List<HashMap<String, String>> queryEnrollment(String username) {
		List enrollment = new ArrayList<HashMap<String, String>>();
		Cursor c = null;
		try {
			c = db.query(ConstantUtils.T_ENROLLMENT, null, "username = ?",
					new String[] { username }, null, null, null);
			c.moveToFirst();
			while (!c.isAfterLast()) {
				enrollment.add(getEnrollmentItem(c));
				c.moveToNext();
			}
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return enrollment;
	}

	/**
	 * this must close
	 */
	public void closeDB() {
		db.close();
	}

}
