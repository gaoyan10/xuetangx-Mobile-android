package com.xuetangx.sqlite;

import com.xuetangx.util.ConstantUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CourseDBHelper extends SQLiteOpenHelper {
	private static final int VERSION = 1;
	public CourseDBHelper (Context c) {
		super(c, ConstantUtils.COURSE_DATA, null, VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE IF NOT EXISTS " + ConstantUtils.T_ENROLLMENT + 
				"(username TEXT, course_id TEXT not null unique, display_name TEXT, display_org TEXT, display_coursenum TEXT, start TEXT, course_image_url TEXT)");
		/*db.execSQL("CREATE TABLE IF NOT EXISTS " + ConstantUtils.T_COURSE_DATA + 
				"(course_id TEXT not null unique, course_data TEXT)");*/
		db.execSQL("CREATE TABLE IF NOT EXISTS " + ConstantUtils.T_COURSE + 
				"(username TEXT, course_id TEXT not null unique, display_name TEXT, display_org TEXT, display_coursenum TEXT, start TEXT, "
				+ "advertised_start TEXT, short_description TEXT ,course_image_url TEXT, marketing_video_url TEXT)");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method st
		//db.execSQL("ALTER TABLE " + tableName + "ADD COLUMN other STRING");;
	}

}
