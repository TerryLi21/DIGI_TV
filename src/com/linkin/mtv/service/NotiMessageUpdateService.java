package com.linkin.mtv.service;

import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import com.linkin.mtv.NotiMessageManager;
import com.linkin.mtv.digi.helper.ConfigHelper;
import com.linkin.mtv.util.SynHtmlUtil;

/**
 * @desc
 * @author liminwei
 * @since 2015-4-24 下午2:05:00
 */
public class NotiMessageUpdateService extends NetworkStateService {
	private static final int MSG_UPDATE = 1;
	private boolean isRunning;
	private NotiMessageManager mMsgManager;

	@Override
	public void onCreate() {
		super.onCreate();
		isRunning = true;
		mMsgManager = NotiMessageManager.getInstance();
		sendEmptyBackgroundMessage(MSG_UPDATE);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onNetworkState(boolean state) {

	}

	@Override
	protected void handleBackgroundMessage(Message msg) {
		if (isRunning && msg.what == MSG_UPDATE) {
			update();
			sendEmptyBackgroundMessageDelayed(MSG_UPDATE, 5 * 60 * 1000); // 定时更新
		}
	}

	private synchronized void update() {
		// http://79.143.186.63:8080/channel/stb/getMessage.htm?agentId=0&sn=51000014&fromId=0
		String url = getUrl();
		Log.i("test", "msg url = " + url);
		String result = SynHtmlUtil.get(url);
		Log.i("test", "msg result = " + result);
		mMsgManager.update(result);
	}

	private String getUrl() {
		StringBuilder sb = new StringBuilder(ConfigHelper.getWebUrl());
		sb.append("/channel/stb/getMessage.htm?");
		sb.append("agentId=").append(0).append("&");
		sb.append("sn=").append(ConfigHelper.getSN()).append("&");
		sb.append("fromId=").append(mMsgManager.getLastMsgPosition());
		return sb.toString();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		isRunning = false;
	}
}
