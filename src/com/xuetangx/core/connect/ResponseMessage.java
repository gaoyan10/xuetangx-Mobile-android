package com.xuetangx.core.connect;

public class ResponseMessage {
	public String message;
	public int code;
	public ResponseMessage(String m, int c) {
		message = m;
		code = c;
	}
}
