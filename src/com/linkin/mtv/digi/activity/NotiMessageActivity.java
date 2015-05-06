package com.linkin.mtv.digi.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.linkin.mtv.Global;
import com.linkin.mtv.database.NotiMessageHelper;
import com.linkin.mtv.digi.R;
import com.linkin.mtv.digi.adapter.NotiMessageAdapter;

/**
 * @desc
 * @author liminwei
 * @since 2015-4-24 下午3:51:47
 */
public class NotiMessageActivity extends Activity {

	private ListView lvMain;
	private NotiMessageAdapter adapter;
	private NotiMessageHelper mMsgHelper;
	private NotiMessageReceiver mNotiMessageReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_noti_message);
		// 控制屏幕保持高亮
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		initView();
		initReceiver();
	}

	private void initView() {
		lvMain = (ListView) findViewById(R.id.lv_activity_noti_message_list);
		mMsgHelper = new NotiMessageHelper(this);
		adapter = new NotiMessageAdapter(this, mMsgHelper.getAll());
		lvMain.setAdapter(adapter);
	}

	private void initReceiver() {
		mNotiMessageReceiver = new NotiMessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Global.ACTION_MESSAGE_UPDATE);
		registerReceiver(mNotiMessageReceiver, filter);
	}

	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.iv_activity_noti_message_back:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mNotiMessageReceiver);
	}

	private class NotiMessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			adapter.update(mMsgHelper.getAll());
		}
	}

}
