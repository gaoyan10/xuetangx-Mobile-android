package com.xuetangx.sqlite;

import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDBManager {
	private UserDBHelper helper;
	private SQLiteDatabase db;
	private static UserDBManager instance;
	public static synchronized UserDBManager getUserDBManager(Context context) {
		if(instance == null) {
			instance = new UserDBManager(context);
		}
		if(!instance.db.isOpen()) {
			instance.db = instance.helper.getWritableDatabase();
		}
		return instance;
	}
	private UserDBManager(Context c) {
		helper = new UserDBHelper(c);
		db = helper.getWritableDatabase();
	}
	/**
	 * keep the table has only one row.
	 * @param access
	 * @return
	 */
	public boolean insertAccess(String[] access) {
		db.beginTransaction();
		try {
			if(access.length < 5) {
				throw new Exception();
			}
			db.execSQL("delete from current_user");
			String sql = "insert into current_user values(?,?,?,?,?,?)";
			db.execSQL(sql, access);
			return true;
		}catch(Exception e) {
			return false;
		} finally {
			db.endTransaction();
		}
	}
	/**
	 * get all message.
	 * @return all message.
	 */
	public HashMap<String, Object> getUserMessage() {
		HashMap<String, Object> message = new HashMap<String, Object>();
		String sql = "select * from current_user";
		Cursor c = db.rawQuery(sql, null);
		if (c.getCount() > 1) {
			return null;
		}
		try {
			while (c.moveToNext()) {
				message.put("username", c.getString(c.getColumnIndex("username")));
				message.put("access_token", c.getString(c.getColumnIndex("access_token")));
				message.put("refresh_token", c.getString(c.getColumnIndex("refresh_token")));
				message.put("scope", c.getString(c.getColumnIndex("scope")));
				message.put("expires_in", c.getInt(c.getColumnIndex("expires_in")));
				message.put("start_time", c.getString(c.getColumnIndex("start_time")));
			}
		}catch(Exception e) {
			return null;
		}finally {
			c.close();
		}
		return message;
	}
	public String getStringField(String field) {
		HashMap<String, Object> message = new HashMap<String, Object>();
		String sql = "select * from current_user";
		Cursor c = db.rawQuery(sql, null);
		if (c.getCount() > 1) {
			return null;
		}
		try {
			if (c.moveToNext()) {
				return c.getString(c.getColumnIndex(field));
			}
		}catch(Exception e) {
			return null;
		}finally {
			c.close();
		}
		return null;
	}
	public void closeDB() {
		db.close();
	}

}
