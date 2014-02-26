package com.xuetangx.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuetangx.R;
import com.xuetangx.gui.VideoPlayerActivity;

public class ExpandListViewAdapter extends BaseExpandableListAdapter {
	private List<String> parentList;
	private List<List<VideoHolder>> childList;
	private Context context;
	private LayoutInflater mInflater;
	public ExpandListViewAdapter(Context c) {
		parentList = new ArrayList<String>();
		childList = new ArrayList<List<VideoHolder>>();
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
		holder.text.setText(childList.get(parent).get(child).title);
		holder.rightIcon.setImageResource(R.drawable.chepter_item_right_icon);
		holder.background.setBackgroundResource(R.drawable.chapter_item_style);
		final int p = parent;
		final int c = child;
		childView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				//Toast.makeText(context, childList.get(p).get(c).url.size() + "", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(context, VideoPlayerActivity.class);
				ArrayList<String[]> d = (ArrayList<String[]>)childList.get(p).get(c).url;
				ArrayList<HashMap<String,Object>> data = new ArrayList<HashMap<String, Object>>();
				for(int i = 0; i < d.size(); i ++) {
					HashMap<String, Object> item = new HashMap<String, Object>();
					item.put("source", d.get(i));
					item.put("video_location", "网络");
					data.add(item);
				}
				if(d.size() > 0) {
					intent.putExtra("data", data);
				}
				context.startActivity(intent);
			}
			
		});
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
	public void setData(List<String> parent, List<List<VideoHolder>> child) {
		this.parentList = parent;
		this.childList = child;
	}
	public boolean setData(String json) {
		List<String> parentList = new ArrayList<String>();
		List<List<VideoHolder>> childList = new ArrayList<List<VideoHolder>>();
		try {
			JSONObject obj = new JSONObject(json);
			JSONArray chapters = getJSONArray(obj,"children");
			for(int i = 0; i < chapters.length(); i ++){
				JSONArray seqs = analyJson(chapters.getJSONObject(i), parentList);
				List<VideoHolder> seqName = new ArrayList<VideoHolder>();
				if (seqs != null && seqs.length() > 0) {
					for(int j = 0; j < seqs.length(); j ++) {
						analyVideoJson(seqs.getJSONObject(j), seqName);
					}
					//childList.add(seqName);
				}
				childList.add(seqName);
			}
			this.parentList = parentList;
			this.childList = childList;
			return true;
		}catch(JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
	public JSONArray analyJson(JSONObject obj, List data) throws JSONException {
		data.add(obj.get("display_name").toString());
		return getJSONArray(obj, "children");
	}
	public JSONArray getJSONArray(JSONObject obj, String key) {
		try{
			return obj.getJSONArray(key);
		}catch(JSONException e) {
			return new JSONArray();
		}
	}
	public void analy(JSONObject obj, List data) throws JSONException {

		JSONArray array = getJSONArray(obj, "children");
		for (int i = 0; i < array.length(); i++) {
			JSONObject children = array.getJSONObject(i);
			String[] video = new String[3];
			if (children.getString("location").contains("/video")) {
				video[0] = children.getString("source");
				video[1] = children.getString("track_zh");
				video[2] = children.getString("track_en");
				data.add(video);
			}
			analy(children, data);
		}
	}
	public void analyVideoJson(JSONObject obj, List<VideoHolder> data) throws JSONException {
		VideoHolder holder = new VideoHolder();
		holder.title = obj.get("display_name").toString();
		analy(obj, holder.url);
		data.add(holder);
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
	private class VideoHolder{
		public String title;
		public List<String[]> url; //string[] 0 source, 1 track_zh, 2 track_en
		public VideoHolder(String t, List<String[]> u) {
			title = t;
			url = u;
		}
		public VideoHolder(){
			title = "";
			url = new ArrayList<String[]>();
		}
	}

}
