package com.linkin.mtv.player;

import java.util.Random;

import android.content.Context;
import android.os.Handler;

/**
 * @desc
 * @author liminwei
 * @since 2015-5-6 下午2:26:04
 */
public class SinglePlayer implements ISinglePlayer {

	private OnSinglePlayerListener mSinglePlayerListener;
	private Context mContext;
	private static int MSG_FLV = 3;
	private int countTime;
	private static final long DELAY_TIME = 1000;
	private String mPlayUrl;

	public SinglePlayer(Context context) {
		mContext = context;
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == MSG_FLV) {
				handleMeg();
			}

		}

		private void handleMeg() {
			Random r = new Random();
			countTime += r.nextInt(40);
			if (countTime < 100) {
				mSinglePlayerListener.onChange(1000, countTime, 20);
				mHandler.sendEmptyMessageDelayed(MSG_FLV, DELAY_TIME);
				return;
			}

			mSinglePlayerListener.onAfterDownload(1000, countTime, 20);

			if (mSinglePlayerListener != null) {
				mSinglePlayerListener
						.onUrlChange(
								"http://live.aishang.ctlcdn.com/hls/ch196_10240048/ch196_10240048forsec.m3u8",
								0);
			}
		}
	};

	private void removeMsg() {
		while (mHandler.hasMessages(MSG_FLV)) {
			mHandler.removeMessages(MSG_FLV);
		}
	}

	@Override
	public void play(String playUrl, int mode) {

		this.mPlayUrl = playUrl;
		removeMsg();
		mHandler.sendEmptyMessage(MSG_FLV);

		if (mSinglePlayerListener != null) {
			mSinglePlayerListener.onStartDownload();
		}
	}

	@Override
	public void stop() {

	}

	@Override
	public void release() {

	}

	@Override
	public void setOnSinglePlayerListener(OnSinglePlayerListener l) {
		mSinglePlayerListener = l;
	}

}
