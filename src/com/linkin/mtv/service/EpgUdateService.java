/**
 * @desc
 * 
 * @author fire@ipmacro.com
 * @since 2015-4-22
 */
package com.linkin.mtv.service;

import java.util.Calendar;

import android.os.Message;

import com.linkin.mtv.EpgManager;
import com.linkin.mtv.digi.helper.ConfigHelper;
import com.linkin.mtv.util.SynHtmlUtil;

public class EpgUdateService extends NetworkStateService {
	private static final int MSG_UPDATE = 1;
	Boolean isRunning = false;
	private EpgManager mEpgManager;

	private static final int DAY = 24 * 60 * 60;

	@Override
	public void onCreate() {
		super.onCreate();
		isRunning = true;
		mEpgManager = EpgManager.getInstance();
		sendEmptyBackgroundMessage(MSG_UPDATE);
	}

	@Override
	public void onNetworkState(boolean state) {

	}

	@Override
	protected void handleBackgroundMessage(Message msg) {
		if (isRunning && msg.what == MSG_UPDATE) {
			boolean bo = update();
			if (bo) { // 如果更新成功
				Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
				int hour = c.get(Calendar.HOUR_OF_DAY);
				int minute = c.get(Calendar.MINUTE);
				int second = c.get(Calendar.SECOND);

				int time = DAY - hour * 60 * 60 - minute * 60 - second; // 剩余时间
				sendEmptyBackgroundMessageDelayed(MSG_UPDATE, time * 1000); // 定时更新
			} else {
				sendEmptyBackgroundMessageDelayed(MSG_UPDATE, 5 * 60 * 1000); // 定时更新
			}
		}
	}

	private synchronized boolean update() {
		String url = ConfigHelper.getWebUrl()
				+ "/channel_resource/live/program.html";
		String result = SynHtmlUtil.get(url);
		// Log.i("test", "epg url = " + url);
		// Log.i("test", "epg result = " + result);
		return mEpgManager.update(result);
	}
}
