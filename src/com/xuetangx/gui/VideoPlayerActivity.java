package com.xuetangx.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamwin.player.CCPlayer;
import com.xuetangx.R;
import com.xuetangx.core.connect.NetConnector;
import com.xuetangx.ui.VideoListAdapter;
import com.xuetangx.util.ConstantUtils;
import com.xuetangx.util.SDUtils;
import com.xuetangx.util.SrtObject;
import com.xuetangx.util.Utils;

public class VideoPlayerActivity extends Activity {
	private boolean isLock = false;
	private RelativeLayout top, middle, bottom, left;
	private ImageButton middleSwitch, bottomSwitch, lock;
	private SeekBar voice, progress;
	private TextView passSecond, totalSecond, srtText;
	private GridView videoList;
	private CCPlayer player;
	private List<HashMap<String, Object>> videoData = null;
	private AudioManager audiomanage;
	private VideoListAdapter videoAdapter;
	private boolean isHidden = false;
	private boolean isStart = true;
	private int currentPosition = 0;
	private int newPosition = 0;
	private int currentVideoIndex = 0;
	private int duration;
	private final int HIDE = 0;
	private final int SHOW = 1;
	private final int PROGRESS_CHANGE = 2;
	private final int LANGUAGE_EN = 3;
	private final int LANGUAGE_ZH = 4;
	private static boolean mDragging = false;
	private boolean srtZhDownload = false;
	private boolean srtEnDownload = false;
	private boolean isSrtReady = false;
	private String srtEnUrl = "";
	private String srtZhUrl = "";
	private SrtObject srt;
	private Handler playHandler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			switch(message.what) {
			case HIDE:
				top.setVisibility(View.GONE);
				middle.setVisibility(View.GONE);
				bottom.setVisibility(View.GONE);
				left.setVisibility(View.GONE);
				voice.setVisibility(View.GONE);
				isHidden = true;
				break;
			case PROGRESS_CHANGE:
				if(!mDragging) {
					setProgress();
					sendEmptyMessageDelayed(PROGRESS_CHANGE, 100);
				}
				break;
			case LANGUAGE_EN:
				srtEnDownload = createSrt(currentVideoIndex);
				break;
			case LANGUAGE_ZH:
				srtZhDownload = createSrt(currentVideoIndex);
				break;
			}
			/*if (message.what == HIDE) {
				top.setVisibility(View.GONE);
				middle.setVisibility(View.GONE);
				bottom.setVisibility(View.GONE);
				left.setVisibility(View.GONE);
				voice.setVisibility(View.GONE);
				isHidden = true;
			} else {
				if (message.what == PROGRESS_CHANGE && !mDragging) {
					setProgress();
					sendEmptyMessageDelayed(PROGRESS_CHANGE, 100);
				}
			}*/
			super.handleMessage(message);
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
					Toast.LENGTH_SHORT).show();
		} else {
			videoData = (ArrayList<HashMap<String, Object>>) b;
			videoAdapter = new VideoListAdapter(this);
			videoList.setAdapter(videoAdapter);
			player.setVideoPath(((String[])videoData.get(0).get("source"))[0].toString());
			currentVideoIndex = 0;
			loadSrt(0);
			player.requestFocus();
			// 开始播放
			player.start();
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
		progress.setMax(1000);
		voice = (SeekBar) findViewById(R.id.player_voice_bar);
		audiomanage = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		voice.setMax(audiomanage.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		passSecond = (TextView) findViewById(R.id.player_pass_second);
		totalSecond = (TextView) findViewById(R.id.player_pass_total);
		videoList = (GridView) findViewById(R.id.player_gridview);
		player = (CCPlayer) findViewById(R.id.player_videoview);
		srtText = (TextView) findViewById(R.id.player_srt_text);
		srt = new SrtObject();
	}

	public void setListener() {
		player.setOnTouchListener(mTouchListener);
		videoList.setOnItemClickListener(videoListener);
		voice.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
				// TODO Auto-generated method stub
				audiomanage.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_PLAY_SOUND);
                voice.setProgress(audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC));  //获取当前值  
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		player.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				player.stopPlayback();
				new AlertDialog.Builder(VideoPlayerActivity.this)
				.setTitle("对不起")
				.setMessage("视频播放失败，请稍候重试")
				.setPositiveButton("知道了",
						new AlertDialog.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								player.stopPlayback();
							}

						}).setCancelable(false).show();
				return false;
			}
			
		});
		player.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer media) {
				// TODO Auto-generated method stub
				duration = media.getDuration();
				setFormatTime(totalSecond, duration);
				isStart = true;
				playHandler.sendEmptyMessage(PROGRESS_CHANGE);
				Message msg = playHandler.obtainMessage(HIDE);
				playHandler.removeMessages(HIDE);
				playHandler.sendMessageDelayed(msg, 1000);
			}
			
		});
		player.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer arg0) {
				// TODO Auto-generated method stub
					//该视频播放完成。
			}
			
		});
		player.setOnSeekCompleteListener(new OnSeekCompleteListener() {

			@Override
			public void onSeekComplete(MediaPlayer mp) {
				// TODO Auto-generated method stub
				if (isStart) {
					player.start();
				}
				playHandler.sendEmptyMessage(PROGRESS_CHANGE);
			}
			
		});
		progress.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if(!fromUser) {
					return;
				}
				newPosition = (duration * progress) / 1000;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				mDragging = true;
				playHandler.removeMessages(PROGRESS_CHANGE);
				playHandler.removeMessages(HIDE);
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				mDragging = false;
				player.pause();
				player.seekTo((int) newPosition);
				Message msg = playHandler.obtainMessage(HIDE);
				playHandler.removeMessages(HIDE);
				playHandler.sendMessageDelayed(msg, 5000l);
				
			}
			
		});
	}
	public void setProgress() {
		int i = player.getCurrentPosition();
		long pos = 1000l * i / duration;
		progress.setProgress((int) pos);
		int j = player.getBufferPercentage();
		setFormatTime(passSecond, i);
		progress.setSecondaryProgress(j * 10);
		if (srtZhDownload || srtEnDownload) {
			String s = srt.findSrt(i);
			if (s.length() > 0) {
				srtText.setBackgroundColor(0x44444444);
			}else {
				srtText.setBackgroundColor(0x00444444);
			}
			srtText.setText(s);
		}
	}
	public void setLayoutVisible() {
		if (!isLock) {
			if (!isHidden) {
				top.setVisibility(View.GONE);
				middle.setVisibility(View.GONE);
				bottom.setVisibility(View.GONE);
				left.setVisibility(View.GONE);
				playHandler.removeMessages(HIDE);
				isHidden = true;
			} else {
				top.setVisibility(View.VISIBLE);
				middle.setVisibility(View.VISIBLE);
				bottom.setVisibility(View.VISIBLE);
				left.setVisibility(View.VISIBLE);
				Message msg = playHandler.obtainMessage(HIDE);
				playHandler.removeMessages(HIDE);
				playHandler.sendMessageDelayed(msg, 2000);
				isHidden = false;
			}
		} else {
			if (!isHidden) {
				left.setVisibility(View.GONE);
				isHidden = true;
			} else {
				left.setVisibility(View.VISIBLE);
				isHidden = false;
			}
		}
	}
	/*
	 * 已经废弃.
	 */
	public void hideMenu() {
	/*	Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = playHandler.obtainMessage();
				msg.what = 0;
				playHandler.sendMessage(msg);
			}

		}, 1500l);*/
		
	}

	/**
	 * 监听按下事件
	 */
	private OnTouchListener mTouchListener = new OnTouchListener() {

		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
			//	isHidden = !isHidden;
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
		if (voice.getVisibility() == View.GONE) {
			voice.setVisibility(View.VISIBLE);
			voice.setProgress(audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC));
		}else {
			voice.setVisibility(View.GONE);
		}
	}

	public void download(View view) {
		
	}

	public void playerSwitch(View view) {
		if(player.isPlaying()) {
			middleSwitch.setImageResource(R.drawable.player_middle_start);;
			bottomSwitch.setImageResource(R.drawable.player_start);
			currentPosition = player.getCurrentPosition();
			player.pause();
			playHandler.removeMessages(PROGRESS_CHANGE);
			isStart = false;
		}else {
			middleSwitch.setImageResource(R.drawable.player_middle_pause);;
			bottomSwitch.setImageResource(R.drawable.player_pause);
			player.seekTo(currentPosition);
			player.start();
			isStart = true;
		}
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
	private void setFormatTime(TextView textView, int time) {
		time /= 1000;
		int minute = time / 60;
		int hour = minute / 60;
		int second = time % 60;
		minute %= 60;
		textView.setText(String.format("%02d:%02d:%02d", hour, minute, second));
	}
	public boolean createSrt(int loc) {
		String lan = Utils.getLanguage();
		String[] source = (String[])videoData.get(loc).get("source");
		if(lan.equals("zh") || lan.equals("zh_CH") || lan.equals("zh_HK") || lan.equals("zh_TW")) {
			return srt.analyseSrtFile(ConstantUtils.VIDEO_SRT_PATH, source[1].hashCode() + "");
		}else {
			return srt.analyseSrtFile(ConstantUtils.VIDEO_SRT_PATH, source[2].hashCode() + "");
		}
	}
	public void loadSrt(int index) {
		String[] source = (String[])videoData.get(index).get("source");
		if (!source[1].equals("null") && source[1].length() > 0) {
			if(SDUtils.checkFile(ConstantUtils.VIDEO_SRT_PATH, source[1].hashCode() + "")){
				playHandler.sendEmptyMessage(LANGUAGE_ZH);
			}else{
				final String u = source[1];
				if (!u.contains(ConstantUtils.URL)) {
					if(u.charAt(0) == '/') {
						srtZhUrl = ConstantUtils.URL + u.substring(1, u.length());
					}else {
						srtZhUrl = ConstantUtils.URL + u;
					}
					
				}else {
					srtZhUrl = u;
				}
				new Thread() {
					@Override
					public void run() {
						
						boolean suc = NetConnector.getInstance().httpDownloadFile(srtZhUrl, SDUtils.getFile(ConstantUtils.VIDEO_SRT_PATH, u.hashCode() + ""));
						if (suc) {
							playHandler.sendEmptyMessage(LANGUAGE_ZH);
						}
					}
				}.start();
				
			}
		}
		if (!source[2].equals("null") && source[2].length() > 0) {
			if(SDUtils.checkFile(ConstantUtils.VIDEO_SRT_PATH, source[2].hashCode() + "")){
				playHandler.sendEmptyMessage(LANGUAGE_EN);
			}else{
				final String u = source[2];
				if (!u.contains(ConstantUtils.URL)) {
					if(u.charAt(0) == '/') {
						srtEnUrl = ConstantUtils.URL + u.substring(1, u.length());
					}else {
						srtEnUrl = ConstantUtils.URL + u;
					}
					srtEnUrl = ConstantUtils.URL + u;
				}else {
					srtEnUrl = u;
				}
				new Thread() {
					@Override
					public void run() {
						boolean suc = NetConnector.getInstance().httpDownloadFile(srtEnUrl, SDUtils.getFile(ConstantUtils.VIDEO_SRT_PATH, u.hashCode() + ""));
						if (suc) {
							playHandler.sendEmptyMessage(LANGUAGE_EN);
						}
					}
				}.start();
				
			}
		}
	}
}
