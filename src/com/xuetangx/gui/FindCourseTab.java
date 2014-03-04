package com.xuetangx.gui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuetangx.R;
import com.xuetangx.gui.CourseTab.ViewHolder;
import com.xuetangx.ui.ClearEditText;
import com.xuetangx.util.ConstantUtils;
import com.xuetangx.util.Utils;

public class FindCourseTab extends CourseTab{
	private Context context;
	private View parentView;
	private ClearEditText search;
	private ListView courses;
	public FindCourseTab(Context c, View p, int version) {
		context = c;
		parentView = p;
		this.height = 6;
		findViews();
		// TODO Auto-generated constructor stub
	}
	private FindCourseTab(Context c, View p) {
		super(c,p);
	}
	private void findViews() {
		search = (ClearEditText) parentView.findViewById(R.id.tab_search_input);
		courses = (ListView) parentView.findViewById(R.id.tab_search_course_list);
	}
	@Override
	public void setHolderView(ViewHolder holder, int index) {
		viewSetImage(holder.image, (String)data.get(index).get("course_image_url"));
		holder.courseName.setText((String)data.get(index).get("display_name"));
		holder.couseStartTime.setText((String)data.get(index).get("start"));
		holder.enter.setText(context.getResources().getString(R.string.read_introduce));
		holder.update.setText(context.getResources().getString(R.string.register_course));
		Drawable updateImage= context.getResources().getDrawable(R.drawable.course_update);  
		/// 这一步必须要做,否则不会显示.  
		updateImage.setBounds(0, 0, Utils.dip2px(context, 20),Utils.dip2px(context, 20));  
		holder.update.setCompoundDrawables(updateImage,null,null,null);  
		Drawable enterImage= context.getResources().getDrawable(R.drawable.enter_course);  
		/// 这一步必须要做,否则不会显示.  
		enterImage.setBounds(0, 0, Utils.dip2px(context, 20),Utils.dip2px(context, 20));  
		holder.enter.setCompoundDrawables(enterImage,null,null,null);
	}
	@Override
	public void enterCourse(int index) {
		
	}
	@Override 
	public void updateorRegister(int index) {
		
	}
	

}
