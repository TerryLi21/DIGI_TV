package com.linkin.mtv.widget;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.net.Uri;
import android.os.Build;
import android.os.Parcel;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class VideoView extends SurfaceView {
	private static final String RK3028_SDKA = "rk3028sdk";
	private static final String GL7029_SDKA = "Full AOSP on Leopard SOC";
	static final boolean DEBUG = false;

	public static String TAG = "VideoView";
	// settable by the client
	private Uri mUri;
	private Map<String, String> mHeaders;

	// all possible internal states
	private static final int STATE_ERROR = -1;
	private static final int STATE_IDLE = 0;
	private static final int STATE_PREPARING = 1;
	private static final int STATE_PREPARED = 2;
	private static final int STATE_PLAYING = 3;
	private static final int STATE_PAUSED = 4;
	private static final int STATE_PLAYBACK_COMPLETED = 5;

	private static final int MAX_WIDTH = 1280;
	private static final int MAX_HEIGHT = 720;

	// mCurrentState is a VideoView object's current state.
	// mTargetState is the state that a method caller intends to reach.
	// For instance, regardless the VideoView object's current state,
	// calling pause() intends to bring the object to a target state
	// of STATE_PAUSED.
	private int mCurrentState = STATE_IDLE;
	private int mTargetState = STATE_IDLE;

	// All the stuff we need for playing and showing a video
	private SurfaceHolder mSurfaceHolder = null;
	private MediaPlayer mMediaPlayer = null;
	private int mVideoWidth;
	private int mVideoHeight;
	private int mSurfaceWidth;
	private int mSurfaceHeight;

	private OnCompletionListener mOnCompletionListener;
	private MediaPlayer.OnPreparedListener mOnPreparedListener;
	private int mCurrentBufferPercentage;
	private OnErrorListener mOnErrorListener;
	private OnBufferingUpdateListener mOnBufferingUpdateListener;
	private int mSeekWhenPrepared; // recording the seek position while
									// preparing
	private boolean mCanPause;
	private boolean mCanSeekBack;
	private boolean mCanSeekForward;
	private boolean mHasCallSetReSelection; // 缓冲过程中是否调用过重新选源逻辑
	private boolean mIsUseLetv = false;
	private OnPlayListener mOnPlayListener;

	private Context mContext;
	private VideoViewInfo mVvInfo;
	int paddingTop = 0;

	public static interface OnBufferStateListener {
		public boolean onStop();

		public void onStart();
	}

	private OnBufferStateListener onBufferStateListener = null;

	public VideoView(Context context) {
		super(context);
		mContext = context;
		initVideoView();
	}

	public VideoView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		mContext = context;
		initVideoView();
	}

	public VideoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initVideoView();
	}

	@Override
	public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
		super.onInitializeAccessibilityEvent(event);
		event.setClassName(VideoView.class.getName());
	}

	@Override
	public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
		super.onInitializeAccessibilityNodeInfo(info);
		info.setClassName(VideoView.class.getName());
	}

	public int resolveAdjustedSize(int desiredSize, int measureSpec) {
		int result = desiredSize;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		switch (specMode) {
		case MeasureSpec.UNSPECIFIED:
			/*
			 * Parent says we can be as big as we want. Just don't be larger
			 * than max size imposed on ourselves.
			 */
			result = desiredSize;
			break;

		case MeasureSpec.AT_MOST:
			/*
			 * Parent says we can be as big as we want, up to specSize. Don't be
			 * larger than specSize, and don't be larger than the max size
			 * imposed on ourselves.
			 */
			result = Math.min(desiredSize, specSize);
			break;

		case MeasureSpec.EXACTLY:
			// No choice. Do what we are told.
			result = specSize;
			break;
		}
		return result;
	}

	private void initVideoView() {
		mVideoWidth = 0;
		mVideoHeight = 0;
		getHolder().addCallback(mSHCallback);
		// setZOrderOnTop(true);
		// setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		getHolder().setFormat(PixelFormat.OPAQUE);
		mCurrentState = STATE_IDLE;
		mTargetState = STATE_IDLE;
	}

	public void setVideoPath(String path) {
		if (!(path == null || path.trim().length() == 0)) {
			setVideoURI(Uri.parse(path));
		} else {

		}
	}

	public void setVideoURI(Uri uri) {
		setVideoURI(uri, null);
	}

	public void setVideoURI(Uri uri, Map<String, String> headers) {
		mUri = uri;
		mHeaders = headers;
		mSeekWhenPrepared = 0;
		openVideo();
		requestLayout();
		invalidate();
	}

	public Uri getPlayUri() {
		return mUri;
	}

	public void stopPlayback() {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
			mCurrentState = STATE_IDLE;
			mTargetState = STATE_IDLE;
		}
	}

	long addUrlTime = 0;

	private void openVideo() {
		addUrlTime = System.currentTimeMillis();
		if (mUri == null || mSurfaceHolder == null) {
			return;
		}

		release(false);
		try {
			mVvInfo = new VideoViewInfo();
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setOnPreparedListener(mPreparedListener);
			mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
			mMediaPlayer.setOnCompletionListener(mCompletionListener);
			mMediaPlayer.setOnErrorListener(mErrorListener);
			mMediaPlayer.setOnInfoListener(mOnInfoListener);
			mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
			mCurrentBufferPercentage = 0;
			mMediaPlayer.setDisplay(mSurfaceHolder);
			mMediaPlayer.setDataSource(mContext, mUri, mHeaders);

			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			// mMediaPlayer.setScreenOnWhilePlaying(true);
			mMediaPlayer.prepareAsync();
			// we don't set the target state here either, but preserve the
			// target state that was there before.
			mCurrentState = STATE_PREPARING;

		} catch (IOException ex) {
			Log.w(TAG, "Unable to open content: " + mUri, ex);
			mCurrentState = STATE_ERROR;
			mTargetState = STATE_ERROR;
			return;
		} catch (IllegalArgumentException ex) {
			Log.w(TAG, "Unable to open content: " + mUri, ex);
			mCurrentState = STATE_ERROR;
			mTargetState = STATE_ERROR;
			return;
		}
	}

	boolean needFixedSize = false;

	public void setNeedFixedSize() {
		needFixedSize = true;
	}

	MediaPlayer.OnVideoSizeChangedListener mSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
		public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
			mVideoWidth = mp.getVideoWidth();
			mVideoHeight = mp.getVideoHeight();
			if (mVideoWidth != 0 && mVideoHeight != 0 && needFixedSize) {
				getHolder().setFixedSize(mVideoWidth, mVideoHeight);
				requestLayout();
				needFixedSize = false;
			}
			if (DEBUG) {
				Log.d(TAG, "mSizeChangedListener: " + mVideoWidth + "  "
						+ mVideoHeight + ":needFixedSize>" + needFixedSize);
			}
			if (mOnVideoViewListener != null && mVvInfo != null) {
				mOnVideoViewListener.onVideoSizeChanged(width, height,
						mVvInfo.getPlayTime());
			}
		}
	};

	MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
		public void onPrepared(MediaPlayer mp) {
			mCurrentState = STATE_PREPARED;

			mCanPause = mCanSeekBack = mCanSeekForward = false;

			if (mOnPreparedListener != null) {
				mOnPreparedListener.onPrepared(mMediaPlayer);
			}
			mVideoWidth = mp.getVideoWidth();
			mVideoHeight = mp.getVideoHeight();

			int seekToPosition = mSeekWhenPrepared; // mSeekWhenPrepared may be
													// changed after seekTo()
													// call
			if (seekToPosition != 0) {
				seekTo(seekToPosition);
			}
			if (DEBUG) {
				Log.i(TAG, "mVideoHeight:" + mVideoHeight + ":mVideoWidth:"
						+ mVideoWidth + (mTargetState == STATE_PLAYING));
				Log.i(TAG, "mTargetState:" + mTargetState + "  STATE_PLAYING:"
						+ STATE_PLAYING);
			}
			start();

			/*
			 * if (mVideoWidth != 0 && mVideoHeight != 0) { // Log.i("@@@@",
			 * "video size: " + mVideoWidth +"/"+ // mVideoHeight);
			 * getHolder().setFixedSize(mVideoWidth, mVideoHeight); if
			 * (mSurfaceWidth == mVideoWidth && mSurfaceHeight == mVideoHeight)
			 * { // We didn't actually change the size (it was already at the //
			 * size // we need), so we won't get a "surface changed" callback,
			 * // so // start the video here instead of in the callback. if
			 * (mTargetState == STATE_PLAYING) { start(); } else if
			 * (!isPlaying() && (seekToPosition != 0 || getCurrentPosition() >
			 * 0)) { } } } else { // We don't know the video size yet, but
			 * should start anyway. // The video size might be reported to us
			 * later.
			 * 
			 * start(); if (mTargetState == STATE_PLAYING) {
			 * 
			 * } }
			 */
		}
	};

	private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
		public void onCompletion(MediaPlayer mp) {
			if (DEBUG) {
				Log.i(TAG, "mCompletionListener");
			}
			mCurrentState = STATE_PLAYBACK_COMPLETED;
			mTargetState = STATE_PLAYBACK_COMPLETED;
			if (mOnCompletionListener != null) {
				mOnCompletionListener.onCompletion(mMediaPlayer);
			}
		}
	};

	private MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener() {
		public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
			if (DEBUG) {
				Log.d(TAG, "OnErrorListener: " + framework_err + "," + impl_err);
			}
			mCurrentState = STATE_ERROR;
			mTargetState = STATE_ERROR;

			Log.d(TAG, "OnErrorListener: framework_err,"
					+ (onBufferStateListener != null));
			// if (onBufferStateListener != null && mVvInfo != null)
			// onBufferStateListener.onStop();

			/* If an error handler has been supplied, use it and finish. */
			// if (mOnErrorListener != null) {
			// if (mOnErrorListener.onError(mMediaPlayer, framework_err,
			// impl_err)) {
			// return true;
			// }
			// }
			//
			return true;
		}
	};

	public void setOnBufferingUpdateListener(OnBufferingUpdateListener l) {
		mOnBufferingUpdateListener = l;
	}

	private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
		public void onBufferingUpdate(MediaPlayer mp, int percent) {
			mCurrentBufferPercentage = percent;
			if (mOnBufferingUpdateListener != null) {
				mOnBufferingUpdateListener.onBufferingUpdate(mp, percent);
			}
		}
	};

	/**
	 * Register a callback to be invoked when the media file is loaded and ready
	 * to go.
	 * 
	 * @param l
	 *            The callback that will be run
	 */
	public void setOnPreparedListener(MediaPlayer.OnPreparedListener l) {
		mOnPreparedListener = l;
	}

	/**
	 * Register a callback to be invoked when the end of a media file has been
	 * reached during playback.
	 * 
	 * @param l
	 *            The callback that will be run
	 */
	public void setOnCompletionListener(OnCompletionListener l) {
		mOnCompletionListener = l;
		if (DEBUG) {
			Log.d(TAG, "mOnCompletionListener: ");
		}
	}

	/**
	 * Register a callback to be invoked when an error occurs during playback or
	 * setup. If no listener is specified, or if the listener returned false,
	 * VideoView will inform the user of any errors.
	 * 
	 * @param l
	 *            The callback that will be run
	 */
	public void setOnErrorListener(OnErrorListener l) {
		mOnErrorListener = l;
	}

	/**
	 * Register a callback to be invoked when an informational event occurs
	 * during playback or setup.
	 * 
	 * @param l
	 *            The callback that will be run
	 */
	// public void setOnInfoListener(OnInfoListener l) {
	// mOnInfoListener = l;
	// }

	int bufferTimes = 0;
	long startBufferTimes = 0;

	public void clearVideoInfo() {
		mVvInfo = null;
	}

	OnInfoListener mOnInfoListener = new OnInfoListener() {
		@Override
		public boolean onInfo(MediaPlayer mp, int what, int extra) {

			if (mOnStateChangeListener != null && mVvInfo != null) {
				mOnStateChangeListener
						.onInfoChange(what, mVvInfo.getPlayTime());
			}

			if (mVvInfo == null || !mVvInfo.check701(what)) {
				return false;
			}

			if (mOnStateChangeListener != null && mVvInfo != null) { // 有效状态值改变
				mOnStateChangeListener.onStateChange(what,
						mVvInfo.getPlayTime());
			}

			if (what == 701) { // 从播放进入what的log
				if (bufferTimes == 0) {
					startBufferTimes = SystemClock.uptimeMillis();
				}
				bufferTimes++;
				long curTime = SystemClock.uptimeMillis();
				if (curTime - startBufferTimes <= 30 * 1000) { // 第一次缓冲,到出现这次缓冲的时间间隔
					if (bufferTimes >= 3) { // 重新选源
						if (onBufferStateListener != null) {
							onBufferStateListener.onStop();
						}
						bufferTimes = 0;
					}
				} else {
					bufferTimes = 1;
				}
			}

			return false;
		}
	};

	SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {
		public void surfaceChanged(SurfaceHolder holder, int format, int w,
				int h) {
			mSurfaceWidth = w;
			mSurfaceHeight = h;
			boolean isValidState = (mTargetState == STATE_PLAYING);
			boolean hasValidSize = (mVideoWidth == w && mVideoHeight == h);
			if (mMediaPlayer != null && isValidState && hasValidSize) {
				if (mSeekWhenPrepared != 0) {
					seekTo(mSeekWhenPrepared);
				}
				start();
			}
			if (mOnVideoViewListener != null) {
				mOnVideoViewListener.surfaceChanged(w, h);
			}
		}

		public void surfaceCreated(SurfaceHolder holder) {
			mSurfaceHolder = holder;
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// after we return from this we can't use the surface any more
			mSurfaceHolder = null;
			release(true);
		}
	};

	/*
	 * release the media player in any state
	 */
	private void release(boolean cleartargetstate) {

		if (mMediaPlayer != null) {

			long cur = SystemClock.uptimeMillis();

			mMediaPlayer.reset();
			mMediaPlayer.release();

			mMediaPlayer = null;
			mCurrentState = STATE_IDLE;
			if (cleartargetstate) {
				mTargetState = STATE_IDLE;
			}

			if (mOnVideoViewListener != null) {
				mOnVideoViewListener.onReleaseTime(SystemClock.uptimeMillis()
						- cur);
			}
		}
		bufferTimes = 0;
		mHasCallSetReSelection = false;

	}

	public void start() {
		if (isInPlaybackState()) {
			mMediaPlayer.start();
			mCurrentState = STATE_PLAYING;

			long now = System.currentTimeMillis();
			if (mOnPlayListener != null) {
				mOnPlayListener.preparedTime(now - addUrlTime);
			}
			initDiffSystem();
		}
		mTargetState = STATE_PLAYING;
	}

	private void initDiffSystem() {
		if (RK3028_SDKA.equals(android.os.Build.MODEL)) {
			Class m = mMediaPlayer.getClass();
			Method setParemeter;
			try {
				setParemeter = m.getDeclaredMethod("setParameter", int.class,
						Parcel.class);

				final int KEY_PARAMETER_SET_TIME_MIN_UPDATA = 1252;
				Parcel data1252 = Parcel.obtain();
				data1252.writeInt(1);
				setParemeter.invoke(mMediaPlayer,
						KEY_PARAMETER_SET_TIME_MIN_UPDATA, data1252);

				final int KEY_PARAMETER_SET_TIME_MAX_UPDATA = 1253;
				Parcel data1253 = Parcel.obtain();
				data1253.writeInt(5);
				setParemeter.invoke(mMediaPlayer,
						KEY_PARAMETER_SET_TIME_MAX_UPDATA, data1253);

				final int KEY_PARAMETER_SET_DATA_MIN_UPDATA = 1254;
				Parcel data1254 = Parcel.obtain();
				data1254.writeInt(2);
				setParemeter.invoke(mMediaPlayer,
						KEY_PARAMETER_SET_DATA_MIN_UPDATA, data1254);

				final int KEY_PARAMETER_SET_DATA_MAX_UPDATA = 1255;
				Parcel data1255 = Parcel.obtain();
				data1255.writeInt(8);
				setParemeter.invoke(mMediaPlayer,
						KEY_PARAMETER_SET_DATA_MAX_UPDATA, data1255);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void pause() {
		if (isInPlaybackState()) {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.pause();
				mCurrentState = STATE_PAUSED;
			}
		}
		mTargetState = STATE_PAUSED;
	}

	public void suspend() {
		release(false);
	}

	public void resume() {
		openVideo();
	}

	public int getCurrentPosition() {

		if (mMediaPlayer != null) {
			try {
				return mMediaPlayer.getCurrentPosition();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	public void seekTo(int msec) {
		if (isInPlaybackState()) {
			mMediaPlayer.seekTo(msec);
			mSeekWhenPrepared = 0;
		} else {
			mSeekWhenPrepared = msec;
		}
	}

	public boolean isPlaying() {
		return isInPlaybackState() && mMediaPlayer.isPlaying();
	}

	public int getBufferPercentage() {
		if (mMediaPlayer != null) {
			return mCurrentBufferPercentage;
		}
		return 0;
	}

	private boolean isInPlaybackState() {
		return (mMediaPlayer != null && mCurrentState != STATE_ERROR
				&& mCurrentState != STATE_IDLE && mCurrentState != STATE_PREPARING);
	}

	public boolean canPause() {
		return mCanPause;
	}

	public boolean canSeekBackward() {
		return mCanSeekBack;
	}

	public boolean canSeekForward() {
		return mCanSeekForward;
	}

	public void setOnBufferStateListener(OnBufferStateListener l) {
		this.onBufferStateListener = l;
	}

	public MediaPlayer getMediaPlayer() {
		return mMediaPlayer;
	}

	public interface OnPlayListener {
		public void preparedTime(long time);
	}

	public void setOnPlayListener(OnPlayListener onPlayListener) {
		this.mOnPlayListener = onPlayListener;
	}

	OnStateChangeListener mOnStateChangeListener;

	public interface OnStateChangeListener {
		public void onStateChange(int state, long time);

		public void onInfoChange(int state, long time);
	}

	public void setOnStateChangeListener(
			OnStateChangeListener onStateChangeListener) {
		this.mOnStateChangeListener = onStateChangeListener;
	}

	public void setUseLetv(boolean isUseLetv) {
		mIsUseLetv = isUseLetv;
	}

	public boolean getUseLetv() {
		return mIsUseLetv;
	}

	OnVideoViewListener mOnVideoViewListener;

	public interface OnVideoViewListener {
		public void surfaceChanged(int w, int h);

		public void onVideoSizeChanged(int w, int h, long time);

		public void onReleaseTime(long time);
	}

	public void setOnVideoViewListener(OnVideoViewListener onVideoViewListener) {
		mOnVideoViewListener = onVideoViewListener;
	}

	public int getTimes701() {
		if (mVvInfo != null) {
			return mVvInfo.getTimes701();
		}
		return 0;
	}
}
