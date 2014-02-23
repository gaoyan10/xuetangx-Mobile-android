package com.xuetangx.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamwin.player.CCPlayer;
import com.xuetangx.R;
import com.xuetangx.ui.VideoListAdapter;

public class VideoPlayerActivity extends Activity {
	private boolean isLock = false;
	private RelativeLayout top, middle, bottom, left;
	private ImageButton middleSwitch, bottomSwitch, lock;
	private SeekBar voice, progress;
	private TextView passSecond, totalSecond;
	private GridView videoList;
	private CCPlayer player;
	private List<HashMap<String, Object>> videoData = null;
	private VideoListAdapter videoAdapter;
	private boolean isHidden = false;
	private Handler playHandler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			if (message.what == 0) {
				top.setVisibility(View.GONE);
				middle.setVisibility(View.GONE);
				bottom.setVisibility(View.GONE);
				left.setVisibility(View.GONE);
			} else {

			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		findView();
		getData();
		hideMenu();
		setListener();

	}

	public void getData() {
		Object b = getIntent().getSerializableExtra("data");
		if (b == null) {
			Toast.makeText(this,
					this.getResources().getString(R.string.no_video),
					Toast.LENGTH_SHORT);
		} else {
			videoData = (ArrayList<HashMap<String, Object>>) b;
			videoAdapter = new VideoListAdapter(this);
			videoList.setAdapter(videoAdapter);
		}
	}

	public void findView() {
		top = (RelativeLayout) findViewById(R.id.player_top_layout);
		middle = (RelativeLayout) findViewById(R.id.player_middle_layout);
		bottom = (RelativeLayout) findViewById(R.id.player_bottom_layout);
		left = (RelativeLayout) findViewById(R.id.player_left_layout);
		middleSwitch = (ImageButton) findViewById(R.id.player_middle_switch);
		bottomSwitch = (ImageButton) findViewById(R.id.player_switch);
		lock = (ImageButton) findViewById(R.id.player_middle_lock);
		progress = (SeekBar) findViewById(R.id.player_seek_bar);
		voice = (SeekBar) findViewById(R.id.player_voice_bar);
		passSecond = (TextView) findViewById(R.id.player_pass_second);
		totalSecond = (TextView) findViewById(R.id.player_pass_total);
		videoList = (GridView) findViewById(R.id.player_gridview);
		player = (CCPlayer) findViewById(R.id.player_videoview);
	}

	public void setListener() {
		player.setOnTouchListener(mTouchListener);
		videoList.setOnItemClickListener(videoListener);
	}

	public void setLayoutVisible() {
		if (!isLock) {
			if (!isHidden) {
				top.setVisibility(View.GONE);
				middle.setVisibility(View.GONE);
				bottom.setVisibility(View.GONE);
				left.setVisibility(View.GONE);
			} else {
				top.setVisibility(View.VISIBLE);
				middle.setVisibility(View.VISIBLE);
				bottom.setVisibility(View.VISIBLE);
				left.setVisibility(View.VISIBLE);

			}
		} else {
			if (!isHidden) {
				left.setVisibility(View.GONE);
			} else {
				left.setVisibility(View.VISIBLE);
			}
		}
	}

	public void hideMenu() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = playHandler.obtainMessage();
				msg.what = 0;
				playHandler.sendMessage(msg);
			}

		}, 1500l);
	}

	/**
	 * 监听按下事件
	 */
	private OnTouchListener mTouchListener = new OnTouchListener() {

		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				isHidden = !isHidden;
				setLayoutVisible();
			}
			return false;
		}
	};

	public void hideLayout() {

	}

	public void showLayout() {

	}

	public void back(View view) {
		this.finish();
	}

	public void voice(View view) {

	}

	public void download(View view) {

	}

	public void playerSwitch(View view) {

	}

	public void lock(View view) {
		if (!isLock) {
			lock.setImageResource(R.drawable.player_unlock);
			isHidden = false;
			setLayoutVisible();
		} else {
			lock.setImageResource(R.drawable.player_lock);
		}
		isLock = !isLock;
	}

	private OnItemClickListener videoListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> newadapter, View view, int location,
				long arg3) {
			// TODO Auto-generated method stub
			if (videoAdapter != null) {
				videoAdapter.setCurrentIndex(location);
				videoAdapter.notifyDataSetChanged();
			}

		}
	};
}
