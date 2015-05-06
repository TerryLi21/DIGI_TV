package com.linkin.mtv.digi.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import com.linkin.mtv.digi.activity.MainActivity;

/**
 * @desc
 * @author liminwei
 * @since 2015-5-5 下午5:28:07
 */
public class LoginHelperImpl implements ILoginHelper {

	private Activity mActivity;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			toMainActivity();
		};
	};

	@Override
	public void login(Activity activity) {
		mActivity = activity;
		mHandler.sendEmptyMessageDelayed(0, 2000);
	}

	@Override
	public void addListener() {

	}

	@Override
	public void setReceiver() {
	}

	@Override
	public void toMainActivity() {
		Intent intent = new Intent(mActivity, MainActivity.class);
		mActivity.startActivity(intent);
		mActivity.finish();
	}

}
