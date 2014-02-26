package com.xuetangx.core.background;

import java.io.File;
import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xuetangx.core.connect.NetConnector;
import com.xuetangx.core.connect.ResponseMessage;
import com.xuetangx.util.ConstantUtils;
import com.xuetangx.util.PreferenceUtils;

public class BootImageReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
			downloadBootImage(context);
		}
	}
	public static void downloadBootImage(Context context) {
		NetConnector conn = NetConnector.getInstance();
		ResponseMessage msg =  conn.httpGet("tmp", null); //uncomplete.
		String url = msg.message;
		int hash = url.hashCode();
		PreferenceUtils pre = new PreferenceUtils(context, ConstantUtils.USER_PRE);
		if(pre.getInteger(ConstantUtils.BOOT_IMAGE, 0) != hash) {
			File file = new File(context.getExternalCacheDir(), ConstantUtils.BOOT_IMAGE);
			if(!file.exists()){
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(conn.httpDownloadFile(url, file)) {
				pre.putInteger(ConstantUtils.BOOT_IMAGE, hash);
			}
		}
	}

}
