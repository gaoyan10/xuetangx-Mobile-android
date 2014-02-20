package com.xuetangx.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xuetangx.R;
import com.xuetangx.util.ConstantUtils;

public class CourseTab extends BaseAdapter {
	public final class ViewHolder {
		public ImageView courseImage;
		public RelativeLayout image;
		public TextView courseName, couseStartTime;
		public TextView enter, update;
		public LinearLayout enterLayout, updateLayout;
	}
	private ArrayList<Map<String, Object>> data;
	private Context context;
	private View parent;
	public CourseTab(Context c, View p) {
		context = c;
		parent = p;
		data = new ArrayList<Map<String, Object>>();
	}
	/**
	 * this is just for test UI.
	 */
	public void getData() {
		HashMap map = new HashMap<String ,Object>();
		map.put("background", context.getResources().getDrawable(R.drawable.images_course_image));
		map.put("coursename","中国建筑史");
		map.put("starttime","课程已开始，2013年11月");
		map.put("enter", "查看课程");
		map.put("update", "20131102\n课程更新");
		data.add(map);
	}
	public void setData(ArrayList data) {
		this.data = data;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int index, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final int finalIndex = index;
		ViewHolder holder = null;
		if(view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.listview_course, null);
			holder.courseImage = (ImageView)view.findViewById(R.id.item_course_icon);
			holder.image = (RelativeLayout)view.findViewById(R.id.item_left_layout);
			holder.courseName = (TextView)view.findViewById(R.id.item_course_message_name);
			holder.couseStartTime = (TextView)view.findViewById(R.id.item_course_message_starttime);
			holder.enter = (TextView)view.findViewById(R.id.item_course_enter_text);
			holder.update = (TextView)view.findViewById(R.id.item_course_update_text);
			holder.enterLayout = (LinearLayout)view.findViewById(R.id.item_course_enter);
			holder.updateLayout = (LinearLayout)view.findViewById(R.id.item_course_update);
			if(ConstantUtils.SCREENHEIGHT > 0) {
				LinearLayout right = (LinearLayout)view.findViewById(R.id.item_right_layout);
				right.getLayoutParams().height = ConstantUtils.SCREENHEIGHT / 5;
				holder.image.getLayoutParams().height = ConstantUtils.SCREENHEIGHT / 5;
			}
			view.setTag(holder);
			
		}else{
			holder = (ViewHolder)view.getTag();
		}
		holder.image.setBackgroundDrawable((Drawable)data.get(index).get("background"));
		holder.courseName.setText((String)data.get(index).get("coursename"));
		holder.couseStartTime.setText((String)data.get(index).get("starttime"));
		holder.enter.setText((String)data.get(index).get("enter"));
		holder.update.setText((String)data.get(index).get("update"));
		OnClickListener enterListener = new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				enterCourse(finalIndex);
			}
			
		};
		OnClickListener updateListener = new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				updateMessage(finalIndex);
			}
			
		};
		holder.enter.setOnClickListener(enterListener);
		//holder.enterLayout.setOnClickListener(enterListener);
		holder.update.setOnClickListener(updateListener);
		//holder.updateLayout.setOnClickListener(updateListener);
		holder.image.setOnClickListener(enterListener);

		return view;
	}
	
	public void enterCourse(int index) {
		Toast.makeText(context, "enter course " + index, Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(context, CourseActivity.class);
		Bundle b = new Bundle();
		b.putString("title", "中国建筑史");
		intent.putExtras(b);
		context.startActivity(intent);
	}
	public void updateMessage(int index) {
		Toast.makeText(context, "update course" + index, Toast.LENGTH_SHORT).show();
	} 

}

