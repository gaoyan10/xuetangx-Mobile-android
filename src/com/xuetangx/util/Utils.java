package com.xuetangx.util;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class Utils {
	public Utils() {
		
	}
	public static String getAPIKey(Context c) {
		ApplicationInfo appInfo;
		String key = null;
		try {
			appInfo = c.getPackageManager().getApplicationInfo(c.getPackageName(), PackageManager.GET_META_DATA);
			key = appInfo.metaData.getString(ConstantUtils.KEY);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(key != null) {
			return key;
		}else {
			return "";
		}
	}
	public static String getSecretToken(Context c, String filename) {
		PreferenceUtils pre = new PreferenceUtils(c, filename);
		String src = easyDecode(pre.getPreference(ConstantUtils.ACCESS, ""));
		pre.putPreference(ConstantUtils.ACCESS, easyEncode(src));
		return src;
	}
	public static String easyEncode(String source) {
		int length = source.length();
	    char[] des = new char[length  + 2];
	    char[] seed = new char[2];
	    Random random = new Random();
	    seed[0] = (char)random.nextInt();// % Character.MAX_VALUE;
	    seed[1] = (char)random.nextInt();
	    for(int i = 0; i < length; i += 2) {
	    	des[i] = (char)(seed[0] ^ source.charAt(i));
	    	des[i + 1] = (char)(seed[1] ^ source.charAt(i));
	    }
	    return new String(des) + seed;
		
	}
	public static String easyDecode(String des) {
		int length = des.length();
		if(length <= 2) {
			return "";
		}else {
			char[] source = new char[length - 2];
			char[] seed = new char[2];
			seed[0] = des.charAt(length - 2);
			seed[1] = des.charAt(length -1);
			for(int i = 0; i < length; i += 2) {
		    	source[i] = (char)(seed[0] ^ des.charAt(i));
		    	source[i + 1] = (char)(seed[1] ^ des.charAt(i));
		    }
			return source.toString();
		}
	}
	public static int dip2px(Context context, float dipValue){              
        final float scale = context.getResources().getDisplayMetrics().density;                   
        return (int)(dipValue * scale + 0.5f);           
    }              
    public static int px2dip(Context context, float pxValue){                  
        final float scale = context.getResources().getDisplayMetrics().density;                   
        return (int)(pxValue / scale + 0.5f);           
    }
    public static boolean checkEmail(String email) {
    	String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    	Pattern regex = Pattern.compile(check);
    	Matcher matcher = regex.matcher(email);
    	boolean isMatched = matcher.matches();
    	return isMatched;
    }
}
