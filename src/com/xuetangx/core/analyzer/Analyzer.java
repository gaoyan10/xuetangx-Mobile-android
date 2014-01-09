package com.xuetangx.core.analyzer;

import org.json.JSONObject;
/**
 * interface for analyzer json。
 * @author gaoyansansheng@gmail.com
 *
 */
public interface Analyzer {
	public boolean analyseJson(String json, int code);
	public String createJson();
}
