package com.xuetangx.gui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xuetangx.R;
import com.xuetangx.core.analyzer.CourseAnalyzer;
import com.xuetangx.core.connect.ResponseMessage;
import com.xuetangx.ui.ExpandListViewAdapter;

public class CourseActivity extends Activity {
	private TextView courseTitle;
	private ExpandableListView chapter;
	private ExpandListViewAdapter adapter;
	private String courseID;
	private ProgressBar loading;
	private String courseName;
	private Long time = System.currentTimeMillis();
	private Handler courseHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			loading.setVisibility(View.GONE);
			if (msg.what == 0) {
				adapter.notifyDataSetChanged();
			}else {
				Toast.makeText(CourseActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();;
			}
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course);
		courseTitle = (TextView) findViewById(R.id.activity_course_title_text);
		chapter = (ExpandableListView)findViewById(R.id.activity_course_listview);
		loading = (ProgressBar)findViewById(R.id.activity_course_title_progress);
		adapter = new ExpandListViewAdapter(this);
		getIntentData();
		//adapter = new ExpandListViewAdapter(this,courseName);
		//adapter.getTestData();
		chapter.setAdapter(adapter);
		getNewCourseData();
	}
	public void backButton(View view) {
		this.finish();
	}
	public void getIntentData() {
		Bundle title = getIntent().getExtras();	
		if(title != null && title.getString("course_id") != null) {
			courseID = title.getString("course_id");
			courseName = title.getString("title");
			if(courseName != null) {
				courseTitle.setText(courseName);
				adapter.setCourseName(courseName);
			}else {
				courseName = "";
			}
			String json = title.getString("data");
			if(json != null) {
				adapter.setData(json);
			}
			time = title.getLong("time");
		}else{
			Toast.makeText(this, "数据获取中", Toast.LENGTH_SHORT).show();
		}
	}
	public void getNewCourseData() {
		loading.setVisibility(View.VISIBLE);
		if (System.currentTimeMillis() - time > (1000 * 60 * 20)) { //最多20分钟刷新一次.
			new Thread() {
				public void run() {
					CourseAnalyzer analyzer = new CourseAnalyzer(courseID, CourseActivity.this);
					ResponseMessage mes = analyzer.connect();
					Object data = analyzer.analyseJson(mes.message, mes.code);
					Message msg = courseHandler.obtainMessage();
					if (data instanceof String) {
						msg.what = -1;
						boolean suc = adapter.setData(data.toString());
						if (suc) {
							msg.what = 0;
						}
					}else{
						msg.what = (Integer)data;
					}
					courseHandler.sendMessage(msg);
				}
			}.start();
		}else {
			loading.setVisibility(View.GONE);
		}
	}
	/*public void addListener() {
		chapter.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
            	ViewHolder(ViewHolder)adapter.getChild(groupPosition, childPosition);
                return false;
            }
		});
	}*/
}
