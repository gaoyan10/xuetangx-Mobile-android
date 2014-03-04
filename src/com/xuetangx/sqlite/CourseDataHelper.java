package com.xuetangx.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xuetangx.util.ConstantUtils;

public class CourseDataHelper extends SQLiteOpenHelper{
	private static final int VERSION = 1;
	public CourseDataHelper(Context c) {
		super(c, ConstantUtils.COURSE_DATA_DETAIL, null, VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE IF NOT EXISTS " + ConstantUtils.T_COURSE_DATA + 
				"(course_id TEXT not null unique, course_data TEXT, time TEXT)");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + ConstantUtils.T_UPDATE + 
				"(course_id TEXT not null, id TEXT not null unique, content TEXT, date TEXT, is_show INTEGER, no INTEGER not null unique)");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
}
