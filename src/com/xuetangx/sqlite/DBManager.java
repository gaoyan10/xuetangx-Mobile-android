package com.xuetangx.sqlite;

import java.util.HashMap;
import java.util.Vector;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

	private DBHelper helper;
	private SQLiteDatabase db;
	private String tableName;
	public DBManager(Context c,  String tableName) {
		helper = new DBHelper(c, tableName);
		db = helper.getWritableDatabase();
		this.tableName = tableName;
	}
	public void add(int level, long time, String status) {
		db.beginTransaction();
		db.execSQL("INSERT INTO " + tableName +" VALUES(?, ? , ?)", new Object[] {
			time, level, status
		});
		db.setTransactionSuccessful();
		db.endTransaction();
	}
	public void delete(int id) {
		db.delete(tableName, "_id<=?", new String[]{id+""});
	}
	public HashMap<String, Object[]> query(int num) {
		return null;
		
	}
	public void closeDB() { 
		db.close();
	}
 }
