package com.xuetangx.gui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.xuetangx.R;
import com.xuetangx.ui.ExpandListViewAdapter;

public class CourseActivity extends Activity {
	private TextView courseTitle;
	private ExpandableListView chapter;
	private ExpandListViewAdapter adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course);
		courseTitle = (TextView) findViewById(R.id.activity_course_title_text);
		chapter = (ExpandableListView)findViewById(R.id.activity_course_listview);
		Bundle title = getIntent().getExtras();
		if(title != null) {
			if(title.getString("title") != null) {
				courseTitle.setText(title.getString("title"));
			}
		}
		adapter = new ExpandListViewAdapter(this);
		adapter.getTestData();
		chapter.setAdapter(adapter);
	}
	public void backButton(View view) {
		this.finish();
	}
}
