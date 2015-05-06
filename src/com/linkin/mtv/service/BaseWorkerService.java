/**
 * @desc
 * 
 * @author fire@ipmacro.com
 * @since 2014-12-25
 */
package com.linkin.mtv.service;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

public class BaseWorkerService extends BaseService{
	
	protected HandlerThread mHandlerThread;
	protected Handler mBackgroundHandler;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mHandlerThread = new HandlerThread("activity worker:" + getClass().getSimpleName());
		mHandlerThread.start();
		mBackgroundHandler = new Handler(mHandlerThread.getLooper()) {
			public void handleMessage(android.os.Message msg) {
				handleBackgroundMessage(msg);
			};
		};
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mBackgroundHandler != null && mBackgroundHandler.getLooper() != null) {
			mBackgroundHandler.getLooper().quit();
		}
		mBackgroundHandler = null;
	}
	
	/**
	 * 处理后台操作
	 */
	protected void handleBackgroundMessage(Message msg) {
	}

	/**
	 * 发送后台操作
	 * 
	 * @param msg
	 */
	protected void sendBackgroundMessage(Message msg) {
		if (mBackgroundHandler != null ) {
			mBackgroundHandler.sendMessage(msg);
		}
	}

	protected void sendBackgroundMessageDelayed(Message msg, long delayMillis) {
		if (mBackgroundHandler != null) {
			mBackgroundHandler.sendMessageDelayed(msg, delayMillis);
		}
	}

	/**
	 * 发送后台操作
	 * 
	 * @param what
	 */
	protected void sendEmptyBackgroundMessage(int what) {
		if (mBackgroundHandler != null) {
			mBackgroundHandler.sendEmptyMessage(what);
		}
	}

	/**
	 * 发送后台操作
	 * 
	 * @param what
	 * @param delayMillis
	 */
	protected void sendEmptyBackgroundMessageDelayed(int what, long delayMillis) {
		if (mBackgroundHandler != null) {
			mBackgroundHandler.sendEmptyMessageDelayed(what, delayMillis);
		}
	}

	/**
	 * 删除 ui事件
	 * 
	 * @param what
	 */
	protected void removeBackgroundMessages(int what) {
		if (mBackgroundHandler != null) {
			mBackgroundHandler.removeMessages(what);
		}
	}

	/**
	 * 删除ui事件
	 * 
	 * @param what
	 * @param object
	 */
	protected void removeBackgroundMessages(int what, Object object) {
		if (mBackgroundHandler != null) {
			mBackgroundHandler.removeMessages(what, object);
		}
	}
}
