package com.xuetangx.ui;

import java.util.ArrayList;
import java.util.List;

import com.xuetangx.R;
import com.xuetangx.util.ConstantUtils;
import com.xuetangx.util.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class ExpandListViewAdapter extends BaseExpandableListAdapter {
	private List<String> parentList;
	private List<List<String>> childList;
	private Context context;
	private LayoutInflater mInflater;
	public ExpandListViewAdapter(Context c) {
		parentList = new ArrayList<String>();
		childList = new ArrayList<List<String>>();
		context = c;
		mInflater = LayoutInflater.from(context);
	}
	class ViewHolder {
		TextView text;
		ImageView rightIcon;
		RelativeLayout background;
	}
	@Override
	public Object getChild(int parent, int child) {
		// TODO Auto-generated method stub
		return childList.get(parent).get(child);
	}

	@Override
	public long getChildId(int parent, int child) {
		// TODO Auto-generated method stub
		return child;
	}

	@Override
	public View getChildView(int parent, int child, boolean isLastChild, View childView,
			ViewGroup parentView) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(childView == null) {
			childView = mInflater.inflate(R.layout.listview_expand_item, null);
			holder = new ViewHolder();
			holder.text = (TextView) childView.findViewById(R.id.extend_item_text);
			holder.background = (RelativeLayout) childView.findViewById(R.id.expand_list_item);
			holder.rightIcon = (ImageView)childView.findViewById(R.id.extend_item_right_icon);
			childView.setTag(holder);
			
		}else {
			holder = (ViewHolder)childView.getTag();
		}
		holder.text.setText(childList.get(parent).get(child));
		holder.rightIcon.setImageResource(R.drawable.chepter_item_right_icon);
		holder.background.setBackgroundResource(R.drawable.chapter_item_style);
		return childView;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return childList.get(arg0).size();
	}

	@Override
	public Object getGroup(int location) {
		// TODO Auto-generated method stub
		return parentList.get(location);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return parentList.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getGroupView(int index, boolean isExpand, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(view == null) {
			view = mInflater.inflate(R.layout.listview_expand_item, null);
			holder = new ViewHolder();
			holder.text = (TextView) view.findViewById(R.id.extend_item_text);
			holder.background = (RelativeLayout) view.findViewById(R.id.expand_list_item);
			holder.rightIcon = (ImageView)view.findViewById(R.id.extend_item_right_icon);
			view.setTag(holder);
			
		}else {
			holder = (ViewHolder)view.getTag();
		}
		holder.text.setText(parentList.get(index));
		if(isExpand) {
			holder.text.setTextColor(context.getResources().getColor(R.color.white_color));
			holder.rightIcon.setImageResource(R.drawable.course_item_right_icon);
			holder.rightIcon.setVisibility(View.VISIBLE);
			holder.background.setBackgroundColor(context.getResources().getColor(R.color.main_color));
		}else {
			holder.text.setTextColor(context.getResources().getColor(R.color.black_text));
			holder.rightIcon.setVisibility(View.GONE);
			holder.background.setBackgroundResource(R.drawable.setting_item_background);
		}
		return view;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}
	public void getTestData() {
		parentList.add("1.中国古代建筑思想概述");
		parentList.add("2.史前及夏商时期的中国建筑");
		parentList.add("3.秦汉时期的中国建筑");
		ArrayList list1 = new ArrayList<String>();
		ArrayList list2 = new ArrayList<String>();
		ArrayList list3 = new ArrayList<String>();
		list1.add("中国古代文明与城市");
		list1.add("中国古代文明与城市");
		list2.add("中国古代文明与城市");
		list2.add("中国古代文明与城市");
		list3.add("中国古代文明与城市");
		list3.add("中国古代文明与城市");
		childList.add(list1);
		childList.add(list2);
		childList.add(list3);
	}

}
