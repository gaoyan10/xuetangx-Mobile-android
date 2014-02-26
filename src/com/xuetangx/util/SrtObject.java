package com.xuetangx.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class SrtObject {
	private class Srt{
		public double start;
		public double end;
		public String content;
	}
	private List<Srt> srt;
	private double currentStart = 0;
	private double currentEnd = 0;
	private String currentContent = "";
	public SrtObject() {
		srt = new ArrayList<Srt>();
	}
	public void analyse(String content) {
		
	}
	public boolean analyseSrtFile(String path, String file) {
		srt.clear();
		String c = SDUtils.readSDFile(path, file);
		c= c.replaceAll("\n\n", "\n");
	//	System.out.println(c);
		String ti = "\\d\\d:\\d\\d:\\d\\d";
		Pattern p = Pattern.compile("(?<=\\d\n)"+ ti +",\\d\\d\\d\\s-->\\s" + ti + "[\\w\\W]+?(?=\n\\d)");
		//Pattern p = Pattern.compile("(?<=\\d+\n)\\d\\d:\\d\\d:\\d\\d,\\d\\d\\d\\s-->[\\w\\W]+?(?=\n\n+)");
		Matcher m = p.matcher(c);
		while(m.find()) {
			String item = m.group();
			int loc = item.indexOf('\n');
			if (loc >= 0) {
				Srt srt = new Srt();
				srt.content = item.substring(loc + 1, item.length());
				String time = item.substring(0, loc);
				String[] t = time.split("\\s-->\\s");
				if(t.length == 2) {
					srt.start = timeConvert(t[0]);
					srt.end = timeConvert(t[1]);
					this.srt.add(srt);
				}
			}
		}
		if(srt.size() > 0) {
			return true;
		}else {
			return false;
		}
	}
	public String findSrt(double millionSec) {
		return findStr(millionSec, 0, srt.size() - 1);
	}
	private String findStr(double sec, int start, int end) {
		//Log.e("time", sec + " " + start + " " +  end + " " + )
		Srt s = srt.get(start);
		Srt e = srt.get(end);
		Log.e("time", sec + " " + start + " " +  end + " " +  s.start + " " + s.end + " "  + e.start + " " + e.end);
		if (sec >= currentStart && sec < currentEnd) {
			return currentContent;
		}
		if(sec < s.start || sec >= e.end) {
			currentContent = "";
			return "";
		}
		if (s.start <= sec && s.end > sec) {
			currentStart = s.start;
			currentEnd = s.end;
			currentContent = s.content;
			return s.content;
		}
		if(e.start <=sec && e.end > sec) {
			currentStart = e.start;
			currentEnd = e.end;
			currentContent = e.content;
			return e.content;
		}
		int mid = (start + end ) /2;
		Srt m = srt.get(mid);
		if (m.end > sec && mid > start) {
			if (mid - start == 1 && s.end <= sec && m.start > sec) {
				return "";
			}else{
				return findStr(sec, start, mid);
			}
			
		}
		if (m.start <= sec && end > mid) {
			if (end - mid == 1 && m.end <= sec && e.start > sec) {
				return "";
			}else{
				return findStr(sec, mid, end);
			}
		}
		return "";
	}
	private double timeConvert(String str) {
		double time;
		str = str.trim();
		int loc  = str.lastIndexOf(",");
		if (loc >= 0) {
			time = Double.valueOf(str.substring(loc + 1, str.length()));
			String[] clock = str.substring(0, loc).split(":");
			int tmp = 0;
			for(int i = clock.length - 1; i >= 0; i--) {
				tmp += (Integer.valueOf(clock[i]) * Math.pow(60, clock.length - i -1));
			}
			return tmp * 1000 + time;
		}else{
			return -1;
		}
	}
	
}
