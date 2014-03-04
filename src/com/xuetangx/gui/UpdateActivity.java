package com.xuetangx.gui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xuetangx.R;
import com.xuetangx.core.analyzer.UpdateAnalyzer;
import com.xuetangx.core.connect.ResponseMessage;
import com.xuetangx.sqlite.CourseDBManager;
import com.xuetangx.ui.UpdateAdapter;
import com.xuetangx.util.ConstantUtils;

public class UpdateActivity extends Activity {
	private TextView title;
	private ProgressBar loading;
	private ListView messageList;
	private RelativeLayout layout;
	private String courseName, courseID, image;
	private CourseDBManager db;
	private UpdateAdapter adapter;
	private UpdateAnalyzer analyzer;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_list);
		findViews();
		db = new CourseDBManager(this, 0, ConstantUtils.COURSE_DATA_DETAIL);
		adapter = new UpdateAdapter(this);
		messageList.setAdapter(adapter);
		analyzer = new UpdateAnalyzer(courseID, this);
		getUpdateData(10);
		getNewData(10);
	}
	private Handler updateHandler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			loading.setVisibility(View.GONE);
			if (message.what > 0) {
				getUpdateData(message.what);
				
			}else {
				Toast.makeText(UpdateActivity.this, UpdateActivity.this.getResources().getString(R.string.load_message_failed), Toast.LENGTH_SHORT).show();
			}
			
		}
	};
	private void getBundleData() {
		Bundle b = getIntent().getExtras();
		if (b != null) {
			courseName = b.getString("courseName");
			courseID = b.getString("courseID");
			image = b.getString("image");
			title.setText(courseName);
		}
		
	}
	public void findViews() {
		layout = (RelativeLayout) findViewById(R.id.update_title);
		title = (TextView) layout.findViewById(R.id.title_text);
		loading = (ProgressBar) layout.findViewById(R.id.title_progress);
		messageList = (ListView) findViewById(R.id.update_list_message);
		getBundleData();
	}
	public void backButton(View view) {
		this.finish();
	}
	public void refresh(View view) {
		loading.setVisibility(View.VISIBLE);
		getNewData(10);
	}
	public void getUpdateData(int limit) {
		db.getDatabase();
		adapter.setData(db.queryUpdateList(courseID, limit + ""), image);
	}
	public void getNewData(int limit) {
		final int l = limit;
		new Thread() {
			@Override
			public void run(){
				ResponseMessage msg = analyzer.connect();
				Object obj = analyzer.analyseJson(msg.message, msg.code);
				if (obj instanceof List) {
					ArrayList<ContentValues> data  = (ArrayList<ContentValues>)obj;
					db.getDatabase();
					db.refreshUpdate(data);
					updateHandler.sendEmptyMessage(l);
				}else {
					updateHandler.sendEmptyMessage(-1);
				}
			}
		}.start();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (db != null) {
			db.closeDB();
		}
	}
	
}
