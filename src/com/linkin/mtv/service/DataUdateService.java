/**
 * @desc
 * 
 * @author fire@ipmacro.com
 * @since 2015-4-22
 */
package com.linkin.mtv.service;

import android.os.Message;
import android.util.Log;

import com.linkin.mtv.DataManager;
import com.linkin.mtv.digi.helper.ConfigHelper;
import com.linkin.mtv.util.SynHtmlUtil;

public class DataUdateService extends NetworkStateService {
	private static final int MSG_UPDATE = 1;
	Boolean isRunning = false;
	private DataManager mDataManager;

	@Override
	public void onCreate() {
		super.onCreate();
		isRunning = true;
		mDataManager = DataManager.getInstance();
		sendEmptyBackgroundMessage(MSG_UPDATE);
	}

	@Override
	public void onNetworkState(boolean state) {

	}

	@Override
	protected void handleBackgroundMessage(Message msg) {
		if (isRunning && msg.what == MSG_UPDATE) {
			update();
			sendEmptyBackgroundMessageDelayed(MSG_UPDATE, 10 * 60 * 1000); // 定时时分钟更新套餐
		}
	}

	private synchronized void update() {
		String url = ConfigHelper.getDataUrl();
		String result = SynHtmlUtil.get(url);
		mDataManager.update(result);
	}
}
