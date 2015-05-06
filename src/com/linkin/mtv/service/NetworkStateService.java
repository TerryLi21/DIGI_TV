/**
 * @desc  需要监听网络状态的服务
 * 如果注册了网络广播,马上注册后来发送广播,查看当前网络状态
 * 
 * @author fire@ipmacro.com
 * @since 2014-12-25
 */
package com.linkin.mtv.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

public abstract class NetworkStateService extends BaseWorkerService {

	private NewtorkBroadcastReceiver mNewtorkBroadcastReceiver;

	@Override
	public void onCreate() {
		super.onCreate();

		mNewtorkBroadcastReceiver = new NewtorkBroadcastReceiver();
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mNewtorkBroadcastReceiver, filter);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mNewtorkBroadcastReceiver);
	}

	public class NewtorkBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals(
					ConnectivityManager.CONNECTIVITY_ACTION)) {
				ConnectivityManager connectivityManager = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo info = connectivityManager.getActiveNetworkInfo();
				if (info != null && info.isAvailable()) {
					onNetworkState(true);
				} else {
					onNetworkState(false);
				}
			}
		}
	}

	public abstract void onNetworkState(boolean state);
}
