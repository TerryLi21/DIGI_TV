/**
 * @desc 所有Service
 * 提供刷新ui的Hander
 * 
 * @author fire@ipmacro.com
 * @since 2014-12-25
 */
package com.linkin.mtv.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class BaseService extends Service{
	private final static String TAG = BaseService.class.getSimpleName();
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	protected Handler mUiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			handleUiMessage(msg);
		};
	};

	/**
	 * 处理更新UI任务
	 * 
	 * @param msg
	 */
	protected void handleUiMessage(Message msg) {
	}

	/**
	 * 发送UI更新操作
	 * 
	 * @param msg
	 */
	protected void sendUiMessage(Message msg) {
		mUiHandler.sendMessage(msg);
	}

	protected void sendUiMessageDelayed(Message msg, long delayMillis) {
		mUiHandler.sendMessageDelayed(msg, delayMillis);
	}

	/**
	 * 发送UI更新操作
	 * 
	 * @param what
	 */
	protected void sendEmptyUiMessage(int what) {
		mUiHandler.sendEmptyMessage(what);
	}

	/**
	 * 发送UI更新操作
	 * @param what
	 * @param delayMillis
	 */
	protected void sendEmptyUiMessageDelayed(int what, long delayMillis) {
		mUiHandler.sendEmptyMessageDelayed(what, delayMillis);
	}
	
	/**
	 * 删除 ui事件
	 * @param what
	 */
	protected void removeUiMessages(int what){
		mUiHandler.removeMessages(what);
	}
	
	/**
	 * 删除ui事件
	 * @param what
	 * @param object
	 */
	protected void removeUiMessages(int what,Object object){
		mUiHandler.removeMessages(what, object);
	}
}
