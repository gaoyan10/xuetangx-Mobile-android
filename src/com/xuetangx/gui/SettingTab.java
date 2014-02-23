package com.xuetangx.gui;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuetangx.R;
import com.xuetangx.ui.SwitchButton;

/**
 * setting page.
 * @author gaoyansansheng@gmail.com
 *
 */
public class SettingTab implements OnClickListener {
	private TextView account;
	private TextView about;
	private RelativeLayout videoCache, videoHistory, networkModel, usageHelp, aboutMe;
	private SwitchButton networkSwitch;
	private Context context;
	private View parentView;
	public SettingTab(Context c, View v) {
		context = c;
		parentView = v;
		findView();
		setViewMessage();
		setLayoutListener();
	}
	public void findView() {
		account = (TextView)parentView.findViewById(R.id.setting_account_setting);
		about = (TextView)parentView.findViewById(R.id.setting_account_about);
		videoCache = (RelativeLayout)parentView.findViewById(R.id.setting_video_cache);
		videoHistory = (RelativeLayout)parentView.findViewById(R.id.setting_video_history);
		networkModel = (RelativeLayout)parentView.findViewById(R.id.setting_network_model);
		usageHelp = (RelativeLayout)parentView.findViewById(R.id.setting_usage_help);
		aboutMe = (RelativeLayout)parentView.findViewById(R.id.setting_about_me);
		networkSwitch = (SwitchButton)networkModel.findViewById(R.id.setting_item_switch_right_icon);
	}
	public void setViewMessage() {
		account.setText(parentView.getResources().getString(R.string.account_setting));
		about.setText(parentView.getResources().getString(R.string.about));
		setViewText((TextView)videoCache.findViewById(R.id.setting_item_text), R.string.video_cache);
		setViewText((TextView)videoHistory.findViewById(R.id.setting_item_text), R.string.video_history);
		setViewText((TextView)networkModel.findViewById(R.id.setting_item_switch_text), R.string.video_network_mobile);
		setViewText((TextView)aboutMe.findViewById(R.id.setting_item_text), R.string.about_me);
		setViewText((TextView)usageHelp.findViewById(R.id.setting_item_text), R.string.usage_help);
		
		setImageView((ImageView)videoCache.findViewById(R.id.setting_item_left_icon), R.drawable.tab_setting_videocache);
		setImageView((ImageView)videoHistory.findViewById(R.id.setting_item_left_icon), R.drawable.tab_setting_history);
		setImageView((ImageView)networkModel.findViewById(R.id.setting_item_switch_left_icon), R.drawable.tab_setting_wifi);
		//setImageView((ImageView)videoCache.findViewById(R.id.setting_item_left_icon), R.drawable.tab_setting_videocache);
		//setImageView((ImageView)videoCache.findViewById(R.id.setting_item_left_icon), R.drawable.tab_setting_videocache);
		
	}
	public void setImageView(ImageView v, int id) {
		v.setImageResource(id);
	}
	public void setViewText(TextView v, int id) {
		v.setText(parentView.getResources().getString(id));
	}
	public void setLayoutListener() {
		videoCache.setOnClickListener(this);
		videoHistory.setOnClickListener(this);
		networkModel.setOnClickListener(this);
		usageHelp.setOnClickListener(this);
		aboutMe.setOnClickListener(this);
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()) {
		case R.id.setting_video_cache:
			break;
		case R.id.setting_video_history:
			break;
		case R.id.setting_network_model:
			break;
		case R.id.setting_about_me:
			Intent intent = new Intent(context,AboutActivity.class);
			intent.putExtra("title", context.getResources().getString(R.string.about_me));
			intent.putExtra("startPage", context.getResources().getString(R.string.about_me_url));
			context.startActivity(intent);
			break;
		case R.id.setting_usage_help:
			Intent usage = new Intent(context,AboutActivity.class);
			usage.putExtra("title", context.getResources().getString(R.string.usage_help));
			usage.putExtra("startPage", context.getResources().getString(R.string.usage_help_url));
			context.startActivity(usage);
			break;
		}
	}
}
