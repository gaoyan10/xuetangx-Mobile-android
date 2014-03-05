package com.xuetangx.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.xuetangx.R;
import com.xuetangx.core.analyzer.AllCourseAnalyzer;
import com.xuetangx.core.connect.ResponseMessage;
import com.xuetangx.sqlite.CourseDBManager;
import com.xuetangx.ui.ClearEditText;
import com.xuetangx.util.Utils;

public class FindCourseTab extends CourseTab {
	private ClearEditText search;
	private ListView courses;
	private ProgressBar loading;

	public FindCourseTab(Context c, View p, int version) {
		context = c;
		parent = p;
		this.height = 6;
		findViews();
		initialData();
		courses.setAdapter(this);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setData(ArrayList data) {
		this.data = data;
		this.notifyDataSetChanged();
	}

	public void initialData() {
		CourseDBManager db = null;
		try {
			db = new CourseDBManager(context);
			db.getDatabase();
			data = (ArrayList<HashMap<String, Object>>) db
					.queryCourseList(Utils.getUserName());
		} catch (Exception e) {
			data = new ArrayList<HashMap<String, Object>>();
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.closeDB();
			}
		}
	}

	private FindCourseTab(Context c, View p) {
		super(c, p);
	}

	private void findViews() {
		search = (ClearEditText) parent.findViewById(R.id.tab_search_input);
		courses = (ListView) parent.findViewById(R.id.tab_search_course_list);
		loading = (ProgressBar) parent
				.findViewById(R.id.tab_search_title_progress);
	}

	@Override
	public void setHolderView(ViewHolder holder, int index) {
		viewSetImage(holder.image,
				(String) data.get(index).get("course_image_url"));
		holder.courseName.setText((String) data.get(index).get("display_name"));
		holder.couseStartTime.setText((String) data.get(index).get("start"));
		holder.enter.setText(context.getResources().getString(
				R.string.read_introduce));
		holder.update.setText(context.getResources().getString(
				R.string.register_course));
		Drawable updateImage = context.getResources().getDrawable(
				R.drawable.course_update);
		// / 这一步必须要做,否则不会显示.
		updateImage.setBounds(0, 0, Utils.dip2px(context, 20),
				Utils.dip2px(context, 20));
		holder.update.setCompoundDrawables(updateImage, null, null, null);
		Drawable enterImage = context.getResources().getDrawable(
				R.drawable.enter_course);
		// / 这一步必须要做,否则不会显示.
		enterImage.setBounds(0, 0, Utils.dip2px(context, 20),
				Utils.dip2px(context, 20));
		holder.enter.setCompoundDrawables(enterImage, null, null, null);
	}

	@Override
	public void enterCourse(int index) {

	}

	@Override
	public void updateorRegister(int index) {

	}

	public void getNewCourse() {
		loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				AllCourseAnalyzer analyzer = new AllCourseAnalyzer(context,
						Utils.getUserName());
				ResponseMessage response = analyzer.connect();
				Object obj = analyzer.analyseJson(response.message,
						response.code);
				Message msg = findHandler.obtainMessage();
				if (obj instanceof List) {
					msg.obj = obj;
					msg.what = 0;
				} else {
					msg.what = (Integer) obj;
				}
				findHandler.sendMessage(msg);
			}
		}.start();
	}

	private Handler findHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			loading.setVisibility(View.GONE);
			if (msg.what == 0) {
				setData((ArrayList) msg.obj);
			}
		}
	};
	public void search(View view) {
		if (search.getVisibility() == View.GONE) {
			search.setVisibility(View.VISIBLE);
		}else {
			search.setVisibility(View.GONE);
		}
		//search.setVisibility(View.VISIBLE);
	}
}
