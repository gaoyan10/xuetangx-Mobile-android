package com.xuetangx.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xuetangx.util.ConstantUtils;

public class UserDBHelper extends SQLiteOpenHelper {
	private Context context;
	private static final int VERSION = 1;
	public UserDBHelper(Context c) {
		super(c, ConstantUtils.USER_DATA, null, VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "create table if not exists "  + ConstantUtils.T_USER + "(username TEXT not null unique, access_token TEXT, scope TEXT, expires_in INTEGER, refresh_token TEXT, start_time TEXT)";
		db.execSQL(sql);
		db.execSQL("create table if not exists " + ConstantUtils.T_HISTORY + "(course_id TEXT not null unique, chapter TEXT, sequence TEXT, video TEXT, time long)");
		db.execSQL("create table if not exists "+ ConstantUtils.T_DOWNLOAD + "(course_id TEXT not null unique, chapter TEXT, sequence TEXT, video TEXT, time long)");
	}  
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if(newVersion > oldVersion) {
			// update db, add field.
		}
	}

}
