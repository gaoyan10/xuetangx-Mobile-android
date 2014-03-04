package com.xuetangx.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuetangx.R;
import com.xuetangx.gui.UpdateDetailActivity;

public class UpdateAdapter extends BaseAdapter {
	private List<HashMap<String, String>> data;
	private Context context;
	private String imagePath;
	public UpdateAdapter(Context context) {
		this.context = context;
		data = new ArrayList<HashMap<String, String>>();
	}
	public void setData(List data, String imagePath) {
		this.data = data;
		this.imagePath = imagePath;
		this.notifyDataSetChanged();
	}
	private class ViewHolder{
		public TextView time;
		public TextView content;
		public ImageView icon;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int index, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.listview_update_item, null);
			holder.content = (TextView) view.findViewById(R.id.item_message_text);
			holder.icon = (ImageView) view.findViewById(R.id.item_message_icon);
			holder.time = (TextView) view.findViewById(R.id.item_message_time);
			view.setTag(holder);
		}else {
			holder = (ViewHolder)view.getTag();
		}
		final String c = data.get(index).get("content").toString();
		String content = Html.fromHtml("<html>" + c + "</html>").toString();
		final String date = data.get(index).get("date").toString();
		holder.time.setText(date);
		if (content.length() > 150) {
			content = content.substring(0, 150) + "..." + context.getResources().getString(R.string.click_for_more);
			final int i = index;
			holder.content.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					Bundle b = new Bundle();
					b.putString("title", date);
					b.putString("content", c);
					Intent intent = new Intent(context, UpdateDetailActivity.class);
					intent.putExtras(b);
					context.startActivity(intent);
					
				}
				
			});
		}
		holder.content.setText(content);
		if (imagePath != null) {
			holder.icon.setImageDrawable(Drawable.createFromPath(imagePath));
		}else {
			holder.icon.setImageResource(R.drawable.images_course_image);
		}
		return view;
	}
}
