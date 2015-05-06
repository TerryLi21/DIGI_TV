package com.linkin.mtv.player;

/**
 * @desc
 * @author liminwei
 * @since 2015-5-6 下午2:10:45
 */
public interface ISinglePlayer {

	void play(String playUrl, int mode);

	void stop();

	void release();

	public interface OnSinglePlayerListener {
		public void onStartDownload();

		public void onError(long time, int per, int speed);

		public void onAfterDownload(long time, int per, int sped);

		public void onChange(long time, int per, int speed);

		public void onUrlChange(String url, long time);
	}

	void setOnSinglePlayerListener(OnSinglePlayerListener l);
}
