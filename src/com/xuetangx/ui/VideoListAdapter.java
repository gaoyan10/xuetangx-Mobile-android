package com.xuetangx.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuetangx.R;

public class VideoListAdapter extends BaseAdapter {
	public static boolean isNewData = false;
	public final class ViewHolder {
		public ImageView image;
		public TextView title;
	}
	private ArrayList<Map<String, Object>> data;
	private Context context;
	private int currentIndex;
	public VideoListAdapter(Context c) {
		context = c;
		data = new ArrayList<Map<String, Object>>();
		currentIndex = 0;
	}
	public void setCurrentIndex(int i) {
		currentIndex = i;
	}
	public void setData(List data) {
		this.data = ( ArrayList<Map<String, Object>>)data;
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
			view = LayoutInflater.from(context).inflate(R.layout.gridview_item, null);
			holder.image = (ImageView)view.findViewById(R.id.item_image);
			holder.title = (TextView)view.findViewById(R.id.item_text);
			view.setTag(holder);
			
		}else{
			holder = (ViewHolder)view.getTag();
		}
		if(index == currentIndex) {
			holder.image.setImageResource(R.drawable.player_video_play);
		}else{
			holder.image.setImageResource(R.drawable.player_video_next);
		}
		holder.title.setText(data.get(index).get("video_location").toString());
		return view;
	}
}

