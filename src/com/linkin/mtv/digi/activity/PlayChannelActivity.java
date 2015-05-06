package com.linkin.mtv.digi.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.linkin.mtv.EpgManager;
import com.linkin.mtv.Global;
import com.linkin.mtv.data.Channel;
import com.linkin.mtv.data.Epg;
import com.linkin.mtv.digi.R;
import com.linkin.mtv.digi.helper.ConfigHelper;
import com.linkin.mtv.digi.view.SolaimanTextView;
import com.linkin.mtv.digi.view.VerticalSeekBar;
import com.linkin.mtv.digi.view.VerticalSeekBar.OnSeekBarChangeListener;
import com.linkin.mtv.player.ISinglePlayer.OnSinglePlayerListener;
import com.linkin.mtv.player.ISinglePlayer;
import com.linkin.mtv.player.SinglePlayer;
import com.linkin.mtv.util.AudioUtil;
import com.linkin.mtv.util.BrightnessUtil;
import com.linkin.mtv.util.ScreenUtil;
import com.linkin.mtv.widget.VideoView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @desc
 * @author liminwei
 * @since 2015-4-22 下午7:04:56
 */
public class PlayChannelActivity extends Activity {

	private ArrayList<Channel> mChannelList;
	private int mChannelPos;
	private boolean isFromFreeChannel;// 是否是从免费频道进来

	private ISinglePlayer mSinglePlayer;
	private VideoView vvMain;

	private SolaimanTextView tvTitle, tvProgress, tvEpg, tvEpgTime;
	private ImageView ivBack, ivPlay;
	private LinearLayout llProgress, llChooseChannel, llVolume, llBrightness;
	private HorizontalScrollView hsvChooseChannel;
	private VerticalSeekBar vsbAudio, vsbBrightness;
	private RelativeLayout rlPlay;
	private String parse_url;

	private boolean isPlaying, isFullScreen;
	private EpgUpdateReceiver mEpgUpdateReceiver;
	String tag = "PlayChannelActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(tag, "onCreate");
		setContentView(R.layout.activity_play_channel);
		// 控制屏幕保持高亮
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getDataFromIntent();
		initView();
		initPlayer();
		setListener();
		setReceiver();
	}

	@SuppressWarnings("unchecked")
	private void getDataFromIntent() {
		Bundle bu = getIntent().getExtras();
		mChannelList = (ArrayList<Channel>) bu.getSerializable("channels");
		mChannelPos = bu.getInt("position");
		isFromFreeChannel = bu.getBoolean("isFree");
	}

	private void initView() {
		vvMain = (VideoView) findViewById(R.id.vv_play);

		tvTitle = (SolaimanTextView) findViewById(R.id.tv_activity_play_channel_title);
		tvProgress = (SolaimanTextView) findViewById(R.id.tv_activity_play_channle_progress);
		tvEpg = (SolaimanTextView) findViewById(R.id.tv_activity_play_channle_epg);
		tvEpgTime = (SolaimanTextView) findViewById(R.id.tv_activity_play_channle_epg_time);
		ivBack = (ImageView) findViewById(R.id.iv_activity_play_channel_back);
		ivPlay = (ImageView) findViewById(R.id.iv_activity_play_channle_play);

		llProgress = (LinearLayout) findViewById(R.id.ll_activity_play_channle_progress);
		llChooseChannel = (LinearLayout) findViewById(R.id.ll_activity_play_channel_choose_channel);
		llVolume = (LinearLayout) findViewById(R.id.ll_activity_play_channel_volume);
		llBrightness = (LinearLayout) findViewById(R.id.ll_activity_play_channel_brightness);

		hsvChooseChannel = (HorizontalScrollView) findViewById(R.id.hsv_activity_play_channel_choose_channel);

		vsbAudio = (VerticalSeekBar) findViewById(R.id.vsb_activity_play_channel_audio);
		vsbBrightness = (VerticalSeekBar) findViewById(R.id.vsb_activity_play_channel_brightness);

		rlPlay = (RelativeLayout) findViewById(R.id.rl_activity_play_channle_play);
		tvTitle.setText(mChannelList.get(mChannelPos).getName());

		setEpgUI();
		setPlayingScreenSize();// 设置播放界面的布局
		addChannelsToLinear();
		setAudioSeekbar();// 设置当前音量seekbar位置
		setBrightnessSeekbar();

		ivPlay.setImageResource(android.R.drawable.ic_media_pause);
		isPlaying = true;

	}

	// 把频道加入到linearlayout显示
	private void addChannelsToLinear() {
		if (mChannelList == null) {
			return;
		}
		int[] screen = ScreenUtil.getScreenSize(this);
		LinearLayout.LayoutParams imageViewLp = new LinearLayout.LayoutParams(
				(int) (screen[0] * 0.25), (int) (screen[0] * 0.25));
		imageViewLp.setMargins(20, 20, 20, 20);
		// 显示图片的配置
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		for (int i = 0; i < mChannelList.size(); i++) {
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(imageViewLp);
			ImageLoader.getInstance().displayImage(
					ConfigHelper.getWebUrl() + mChannelList.get(i).getLogo(),
					iv, options);
			iv.setTag(mChannelList.get(i));// 记录这个图片对应的是哪个频道
			iv.setOnClickListener(channelChooseListen);

			llChooseChannel.addView(iv);
		}

		hsvChooseChannel.setBackgroundColor(0x66aaaaaa);// 设置频道栏的背景颜色
		// 音量条的layout
		RelativeLayout.LayoutParams seekbarAudioLp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		seekbarAudioLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		seekbarAudioLp.setMargins((int) (screen[1] * 0.05),
				(int) (screen[0] * 0.1), 0, (int) (screen[0] * 0.3) + 20);
		llVolume.setLayoutParams(seekbarAudioLp);
		// 亮度条的layout
		RelativeLayout.LayoutParams seekbarBrightLp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		seekbarBrightLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		seekbarBrightLp.setMargins(0, (int) (screen[0] * 0.1),
				(int) (screen[1] * 0.05), (int) (screen[0] * 0.3) + 20);
		llBrightness.setLayoutParams(seekbarBrightLp);
	}

	// 设置播放界面的布局
	private void setPlayingScreenSize() {

		int[] screen = ScreenUtil.getScreenSize(this);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				(int) (screen[1] * 0.35));
		lp.setMargins(20, (int) (screen[1] * 0.1), 20, 0);
		rlPlay.setLayoutParams(lp);
	}

	// 更新节目显示信息
	private void setEpgUI() {
		Epg curEpg = EpgManager.getInstance().getCurEpg(
				String.valueOf(mChannelList.get(mChannelPos).getId()));
		if (curEpg == null) {
			tvEpg.setText(getResources().getString(R.string.no_program_info));
			tvEpgTime.setText(getResources()
					.getString(R.string.no_program_info));
		} else {
			tvEpg.setText(curEpg.getName());
			String beginTime = curEpg.getBeginTime();
			String endTime = curEpg.getEndTime();
			beginTime = beginTime.substring(beginTime.indexOf(" ") + 1,
					beginTime.lastIndexOf(":"));
			endTime = endTime.substring(endTime.indexOf(" ") + 1,
					endTime.lastIndexOf(":"));
			tvEpgTime.setText(beginTime + "-" + endTime);
		}
	}

	/**
	 * 点击频道图片监听
	 */
	private OnClickListener channelChooseListen = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				mSinglePlayer.stop();
				Channel ch = (Channel) v.getTag();

				parse_url = ConfigHelper.AESDecrypt(ch.getPlayUrl());
				Log.i("test", "play url = " + parse_url);
				mSinglePlayer.play(parse_url, 0);
				hsvChooseChannel.setVisibility(View.GONE);
				llVolume.setVisibility(View.GONE);
				llBrightness.setVisibility(View.GONE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	// 设置audio SeekBar的位置
	private void setAudioSeekbar() {

		int curVolumn = AudioUtil.getCurrentAudio(this);
		int maxVolumn = AudioUtil.getMaxAudio(this);
		vsbAudio.setMax(maxVolumn);
		vsbAudio.setProgress(curVolumn);
	}

	// 设置brightness SeekBar的位置

	private void setBrightnessSeekbar() {
		int curBright = BrightnessUtil.getScreenBrightness(this);
		int maxValue = BrightnessUtil.getMaxBrightness();
		vsbBrightness.setMax(maxValue);
		vsbBrightness.setProgress(curBright);
	}

	// 设置亮度
	private void setBrightnessValue(int value) {
		BrightnessUtil.setBrightness(this, value);
	}

	// 设置当前音量
	private void setAudioVolumn(int curSeekPos) {
		AudioUtil.setCurrentAudio(this, curSeekPos);
	}

	private void initPlayer() {
		mSinglePlayer = ConfigHelper.getSinglePlayer(this);
		mSinglePlayer.setOnSinglePlayerListener(singlePlayerListener);
	}

	private void setListener() {
		ivBack.setOnClickListener(new OnClickListener() {
			// 返回
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ivPlay.setOnClickListener(new OnClickListener() {
			// 点击播放
			@Override
			public void onClick(View v) {

				try {
					if (isPlaying) {
						ivPlay.setImageResource(android.R.drawable.ic_media_play);
						isPlaying = false;
					} else {
						ivPlay.setImageResource(android.R.drawable.ic_media_pause);
						isPlaying = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		});

		vvMain.setOnTouchListener(new OnTouchListener() {

			int downX;
			int downY;

			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {

				case MotionEvent.ACTION_DOWN:
					downX = (int) event.getX();
					downY = (int) event.getY();

					return true;
				case MotionEvent.ACTION_UP:

					if (isFullScreen) {
						int x = (int) event.getX();
						int y = (int) event.getY();

						if (Math.abs(downX - x) > 20
								|| Math.abs(downY - y) > 20) {
							return false;
						}

						if (hsvChooseChannel.getVisibility() == View.VISIBLE) {
							llVolume.setVisibility(View.GONE);
							llBrightness.setVisibility(View.GONE);
							hsvChooseChannel.setVisibility(View.GONE);
						} else {
							if (y > ScreenUtil
									.getScreenSize(PlayChannelActivity.this)[1] * 0.8) {
								llVolume.setVisibility(View.VISIBLE);
								llBrightness.setVisibility(View.VISIBLE);
								hsvChooseChannel.setVisibility(View.VISIBLE);
							}
						}

					} else {
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					}
					return true;

				default:
					break;
				}
				return false;
			}
		});

		vsbAudio.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(VerticalSeekBar vBar) {

			}

			@Override
			public void onStartTrackingTouch(VerticalSeekBar vBar) {

			}

			@Override
			public void onProgressChanged(VerticalSeekBar vBar, int progress,
					boolean fromUser) {
				setAudioVolumn(progress);
			}
		});

		vsbBrightness.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(VerticalSeekBar vBar) {

			}

			@Override
			public void onStartTrackingTouch(VerticalSeekBar vBar) {

			}

			@Override
			public void onProgressChanged(VerticalSeekBar vBar, int progress,
					boolean fromUser) {
				setBrightnessValue(progress);
			}
		});
	}

	private void setReceiver() {
		mEpgUpdateReceiver = new EpgUpdateReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Global.ACTION_EPG_UPDATE);
		registerReceiver(mEpgUpdateReceiver, filter);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i(tag, "onStart");
		try {
			parse_url = ConfigHelper.AESDecrypt(mChannelList.get(mChannelPos)
					.getPlayUrl());
			mSinglePlayer.play(parse_url, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i(tag, "onStop");
		mSinglePlayer.release();
		vvMain.suspend();
		llProgress.setVisibility(View.GONE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_DOWN:

		case KeyEvent.KEYCODE_VOLUME_UP:
			if (isFullScreen) {
				setAudioSeekbar();// 无论音量调高或调低，都是刷新音量条
			}
			return true;

		case KeyEvent.KEYCODE_BACK:
			if (isFullScreen) {
				llVolume.setVisibility(View.GONE);
				llBrightness.setVisibility(View.GONE);
				hsvChooseChannel.setVisibility(View.GONE);
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				return true;
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(tag, "onDestroy");
		unregisterReceiver(mEpgUpdateReceiver);
	}

	private OnSinglePlayerListener singlePlayerListener = new OnSinglePlayerListener() {

		@Override
		public void onUrlChange(String url, long time) {
			Log.i(tag, "play url = " + url);
			vvMain.setVideoPath(url);
		}

		@Override
		public void onStartDownload() {
			llProgress.setVisibility(View.VISIBLE);
		}

		@Override
		public void onError(long time, int per, int speed) {
		}

		@Override
		public void onChange(long time, int per, int speed) {
			tvProgress.setText(per + "%");
		}

		@Override
		public void onAfterDownload(long time, int per, int sped) {
			llProgress.setVisibility(View.GONE);
		}
	};

	public void onConfigurationChanged(
			android.content.res.Configuration newConfig) {

		if (android.content.res.Configuration.ORIENTATION_LANDSCAPE == newConfig.orientation) {
			// 设置横屏
			Log.i(tag, "SCREEN_ORIENTATION_LANDSCAPE");
			RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			rlPlay.setLayoutParams(lp2);
			rlPlay.setPadding(0, 0, 0, 0);
			isFullScreen = true;// 全屏
		} else if (android.content.res.Configuration.ORIENTATION_PORTRAIT == newConfig.orientation) {
			// 设置竖屏
			Log.i(tag, "SCREEN_ORIENTATION_PORTRAIT");
			setPlayingScreenSize();
			isFullScreen = false;// 不全屏
		}
		Log.i(tag, "new config = " + newConfig.orientation);
		super.onConfigurationChanged(newConfig);
	};

	/**
	 * 监听epg服务有数据更新
	 */
	private class EpgUpdateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			setEpgUI();// 节目单更后，更新界面显示界面
		}
	}

}
