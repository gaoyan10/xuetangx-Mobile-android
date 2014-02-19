package com.xuetangx.core.analyzer;

import com.xuetangx.core.connect.ResponseMessage;
/**
 * interface for analyzer json。
 * @author gaoyansansheng@gmail.com
 *
 */
public interface Analyzer {
	public String analyseJson(String json, int code);
	public String createJson();
	public ResponseMessage connect();
}
