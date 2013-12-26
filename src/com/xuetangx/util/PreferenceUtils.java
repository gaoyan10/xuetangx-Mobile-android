package com.xuetangx.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {
	private SharedPreferences pre;
	public PreferenceUtils(Context c, String name) {
		pre = c.getSharedPreferences(name, c.MODE_PRIVATE);
	}
	public boolean putPreference(String name, String content) {
	    SharedPreferences.Editor edit = pre.edit();
	    return edit.putString(name, content).commit();
		
	}
	public String getPreference(String name, String defaults) {
		return pre.getString(name, defaults);
	}
	public boolean putBoolean(String name, boolean value) {
		SharedPreferences.Editor edit = pre.edit();
		return edit.putBoolean(name, value).commit();
	}
	public boolean getBoolean(String name, boolean def) {
		return pre.getBoolean(name, def);
	}
	public int getInteger(String name, int def) {
		return pre.getInt(name, def);
	}
	public boolean putInteger(String name, int value) {
		SharedPreferences.Editor edit = pre.edit();
		return edit.putInt(name, value).commit();
	}
}
