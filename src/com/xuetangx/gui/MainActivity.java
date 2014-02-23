package com.xuetangx.gui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xuetangx.R;
import com.xuetangx.core.analyzer.EnrollmentAnalyzer;
import com.xuetangx.core.connect.ResponseMessage;
import com.xuetangx.sqlite.CourseDBManager;
import com.xuetangx.util.ConstantUtils;
public class MainActivity extends Activity implements OnClickListener, OnPageChangeListener{
	private ViewPager pager; //切换
	private ImageView[] tabImage;//, tabSearch, tabSetting;
	private TextView[] tabText;//, tabSearchText, tabSettingText;
	private LinearLayout tabCourseLayout, tabSearchLayout, tabSettingLayout;
	private int currentIndex = 0;
	private int[] normalImage;
	private int[] pressImage;
	private SettingTab setting;
	private CourseTab courseAdapter;
	private ListView course;
	private ProgressBar courseProgress, searchProgress;
	private CourseDBManager db;
	private long mExitTime; 
	private Handler courseHandler = new Handler() {
		public void handleMessage(Message msg) {
			courseProgress.setVisibility(View.GONE);
			if (msg.what == 0) {
				courseAdapter.setData((ArrayList)msg.obj);
				courseAdapter.isNewData = true;
				courseAdapter.notifyDataSetChanged();
			}else{
				if(msg.what == -3) {
					//登录已经过期，这种情况应该不会出现。
				}
				Toast.makeText(MainActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		pager = (ViewPager) findViewById(R.id.main_pager);
		pager.setOnPageChangeListener(this);
		
		tabImage = new ImageView[3];
		tabImage[0] = (ImageView) findViewById(R.id.main_bottom_course);
		tabImage[1] = (ImageView) findViewById(R.id.main_bottom_search);
		tabImage[2] = (ImageView) findViewById(R.id.main_bottom_setting);
		tabImage[0].setOnClickListener(this);
		tabImage[1].setOnClickListener(this);
		tabImage[2].setOnClickListener(this);
		
		tabText = new TextView[3];
		tabText[0] = (TextView) findViewById(R.id.main_bottom_course_text);
		tabText[1] = (TextView) findViewById(R.id.main_bottom_search_text);
		tabText[2] = (TextView) findViewById(R.id.main_bottom_setting_text);
		
		tabCourseLayout = (LinearLayout) findViewById(R.id.main_bottom_course_layout);
		tabSearchLayout = (LinearLayout) findViewById(R.id.main_bottom_search_layout);
		tabSettingLayout = (LinearLayout) findViewById(R.id.main_bottom_setting_layout);
		tabCourseLayout.setOnClickListener(this);
		tabSearchLayout.setOnClickListener(this);
		tabSettingLayout.setOnClickListener(this);
		
		getScreenMessage();
		//加载每个分页的内容.
		LayoutInflater tab = LayoutInflater.from(this);
		View view1 = tab.inflate(R.layout.tab_course, null);
		View view2 = tab.inflate(R.layout.tab_search, null);
		View view3 = tab.inflate(R.layout.tab_setting, null);
		
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		
        PagerAdapter mPagerAdapter = new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(views.get(position));
			}
	
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));
				return views.get(position);
			}
		};
		pager.setAdapter(mPagerAdapter);
		setting = new SettingTab(this,view3);
		courseAdapter = new CourseTab(this, view1);
		ArrayList data = (ArrayList)getIntent().getSerializableExtra("data");
		/*courseAdapter.getData();courseAdapter.getData();
		courseAdapter.getData();
		courseAdapter.getData();
		courseAdapter.getData();
		courseAdapter.getData();
		courseAdapter.getData();
		courseAdapter.getData();*/
		courseAdapter.setData(data);
		course = (ListView)view1.findViewById(R.id.course_listview);
		courseProgress = (ProgressBar)view1.findViewById(R.id.tab_course_title_progress);
		searchProgress = (ProgressBar)view2.findViewById(R.id.tab_search_title_progress);
		course.setAdapter(courseAdapter);
		
		db = new CourseDBManager(this);
		
		//refresh data.
		getNewEnrollment();
	}
	public void getScreenMessage() {
		Display display = getWindowManager().getDefaultDisplay();
		ConstantUtils.SCREENHEIGHT = display.getHeight();
		ConstantUtils.SCREENWIDTH = display.getWidth();
		normalImage = new int[3];
		normalImage[0] = R.drawable.tab_me_normal;
		normalImage[1] = R.drawable.tab_search_normal;
		normalImage[2] = R.drawable.tab_settings_normal;
		pressImage = new int[3];
		pressImage[0] = R.drawable.tab_me_pressed;
		pressImage[1] = R.drawable.tab_search_pressed;
		pressImage[2] = R.drawable.tab_settings_pressed;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void setCurrentIndex(int index) {
		currentIndex = index;
	}
	public void setTabChange(int toIndex) {
		tabText[toIndex].setTextColor(this.getResources().getColor(R.color.main_color));
		//tabImage[toIndex].setBackgroundResource(pressImage[toIndex]);
		tabImage[toIndex].setImageDrawable(this.getResources().getDrawable(pressImage[toIndex]));
		tabText[currentIndex].setTextColor(this.getResources().getColor(R.color.black_text));
		//tabImage[currentIndex].setBackgroundResource(normalImage[currentIndex]);
		tabImage[currentIndex].setImageDrawable(this.getResources().getDrawable(normalImage[currentIndex]));
		setCurrentIndex(toIndex);
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()) {
		case R.id.main_bottom_course_layout:
		case R.id.main_bottom_course:
			pager.setCurrentItem(0);
			break;
		case R.id.main_bottom_search_layout:
		case R.id.main_bottom_search:
			pager.setCurrentItem(1);
			break;
		case R.id.main_bottom_setting_layout:
		case R.id.main_bottom_setting:
			pager.setCurrentItem(2);
			break;
		}
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		setTabChange(arg0);
	}
	public void getNewEnrollment() {
		courseProgress.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				EnrollmentAnalyzer analyzer = new EnrollmentAnalyzer(MainActivity.this);
				ResponseMessage response = analyzer.connect();
				Object obj = analyzer.analyseJson(response.message, response.code);
				Message msg = courseHandler.obtainMessage();
				if(obj instanceof List) {
					msg.obj = obj;
					msg.what = 0;
				}else {
					msg.what = (Integer)obj;
				}
				courseHandler.sendMessage(msg);
			}
		}.start();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		db.closeDB();
	}
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
         if (keyCode == KeyEvent.KEYCODE_BACK) {
                 if ((System.currentTimeMillis() - mExitTime) > 3000) {
                        // Object mHelperUtils;
                         Toast.makeText(this, this.getResources().getString(R.string.exit_tip), Toast.LENGTH_SHORT).show();
                         mExitTime = System.currentTimeMillis();

                 } else {
                         finish();
                 }
                 return true;
         }
         return super.onKeyDown(keyCode, event);
 }

}
