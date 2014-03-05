package com.xuetangx.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.xuetangx.core.connect.NetConnector;
import com.xuetangx.core.connect.ResponseMessage;
import com.xuetangx.sqlite.CourseDBManager;
import com.xuetangx.util.ConstantUtils;
import com.xuetangx.util.SDUtils;
import com.xuetangx.util.Utils;

public class CourseTab extends BaseAdapter {
	public static boolean isNewData = false;
	public double height = 6;
	public double width = 2/3;
	public final class ViewHolder {
		public ImageView courseImage;
		public RelativeLayout image;
		public TextView courseName, couseStartTime;
		public TextView enter, update;
		public LinearLayout enterLayout, updateLayout;
	}
	private Handler imageHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				ImageObject obj = (ImageObject)msg.obj;
				obj.setImage();
			}
		}
	};
	protected ArrayList<HashMap<String, Object>> data;
	protected Context context;
	protected View parent;
	public CourseTab(Context c, View p) {
		context = c;
		parent = p;
		data = new ArrayList<HashMap<String, Object>>();
	}
	public CourseTab() {
		
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
				right.getLayoutParams().height =(int)( ConstantUtils.SCREENHEIGHT / height);
				holder.image.getLayoutParams().height =(int)( ConstantUtils.SCREENHEIGHT / height);
				holder.image.getLayoutParams().width = (int)(ConstantUtils.SCREENWIDTH / width);
			}
			view.setTag(holder);
			
		}else{
			holder = (ViewHolder)view.getTag();
		}
		setHolderView(holder, index);
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
				updateorRegister(finalIndex);
			}
			
		};
		holder.enter.setOnClickListener(enterListener);
		//holder.enterLayout.setOnClickListener(enterListener);
		holder.update.setOnClickListener(updateListener);
		//holder.updateLayout.setOnClickListener(updateListener);
		holder.image.setOnClickListener(enterListener);

		return view;
	}
	protected void setHolderView(ViewHolder holder, int index) {
		viewSetImage(holder.image, (String)data.get(index).get("course_image_url"));
		holder.courseName.setText((String)data.get(index).get("display_name"));
		holder.couseStartTime.setText((String)data.get(index).get("start"));
		holder.enter.setText(context.getResources().getString(R.string.enter_course));
		holder.update.setText(context.getResources().getString(R.string.enter_update));
		Drawable updateImage= context.getResources().getDrawable(R.drawable.course_update);  
		/// 这一步必须要做,否则不会显示.  
		updateImage.setBounds(0, 0, Utils.dip2px(context, 20),Utils.dip2px(context, 20));  
		holder.update.setCompoundDrawables(updateImage,null,null,null);  
		Drawable enterImage= context.getResources().getDrawable(R.drawable.enter_course);  
		/// 这一步必须要做,否则不会显示.  
		enterImage.setBounds(0, 0, Utils.dip2px(context, 20),Utils.dip2px(context, 20));  
		holder.enter.setCompoundDrawables(enterImage,null,null,null);
	}
	protected void enterCourse(int index) {
		Toast.makeText(context, "enter course " + index, Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(context, CourseActivity.class);
		Bundle b = new Bundle();
		CourseDBManager db = new CourseDBManager(context, 0,ConstantUtils.COURSE_DATA_DETAIL);
		HashMap<String, Object> courseData = db.queryOneCourseData((String)data.get(index).get("course_id"));
		db.closeDB();
		b.putString("title", data.get(index).get("display_name").toString());
		b.putString("course_id", data.get(index).get("course_id").toString());
		if(courseData != null) {
			b.putString("data",courseData.get("json").toString());
			b.putLong("time", (Long)courseData.get("time"));
		}
		intent.putExtras(b);
		context.startActivity(intent);
	}
	protected void updateorRegister(int index) {
		final int i = index;
		Intent intent = new Intent(context, UpdateActivity.class);
		String courseID = data.get(index).get("course_id").toString();
		String courseName = data.get(index).get("display_name").toString();
		Bundle b = new Bundle();
		b.putString("courseName", courseName);
		b.putString("courseID", courseID);
		String url = (String)data.get(index).get("course_image_url");
		File image = new File(SDUtils.getImageDir(context), url.hashCode() + "");
		if (image.exists() && image.length() > 0) {
			b.putString("image", image.getAbsolutePath());
		}
		intent.putExtras(b);
		context.startActivity(intent);
	} 
	public void viewSetImage(RelativeLayout view, String url) {
		File image = new File(SDUtils.getImageDir(context), url.hashCode() + "");
		if (image.exists() && image.length() > 0) {
			view.setBackgroundDrawable(Drawable.createFromPath(image.getAbsolutePath()));
		}else{
			view.setBackgroundResource(R.drawable.images_course_image);
			final String u = url;
			final RelativeLayout v = view;
			if (isNewData) {// new data
				new Thread() {
					public void run(){
						File imageFile = new File(SDUtils.getImageDir(context), u.hashCode() + "");
						String url = ConstantUtils.URL + u;
						if (u.charAt(0) == '/') {
							url = ConstantUtils.URL + u.substring(1, u.length());
						}
						boolean suc = NetConnector.getInstance().httpDownloadFile(url, imageFile);
						if (suc) {
							Message msg = imageHandler.obtainMessage();
							msg.what = 0;
							msg.obj = new ImageObject(v, imageFile);
						}
					}
				}.start();
			}
		}
	}
	private class ImageObject{
		View view;
		File image;
		public ImageObject(View v, File i) {
			view = v;
			image = i;
		}
		public void setImage() {
			if(image.exists()) {
				view.setBackgroundDrawable(Drawable.createFromPath(image.getAbsolutePath()));
			}
		}
	}

}

