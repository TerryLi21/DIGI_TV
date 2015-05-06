package com.linkin.mtv.digi.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.linkin.mtv.digi.R;
import com.linkin.mtv.digi.helper.ConfigHelper;
import com.linkin.mtv.digi.helper.ILoginHelper;
import com.linkin.mtv.digi.helper.LoginHelperImpl;
import com.linkin.mtv.util.DialogManager;
import com.linkin.mtv.util.NetworkUtil;

/**
 * @desc
 * @author liminwei
 * @since 2015-4-20 下午3:52:43
 */
public class LoginActivity extends Activity {

	private TextView tvTip;
	private DialogManager mDialogManager;
	private ILoginHelper mLoginHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		mDialogManager = new DialogManager(this);
		if (NetworkUtil.isConnect(this)) {
			initView();
			mLoginHelper = ConfigHelper.getLonginHeler();
			mLoginHelper.login(this);
		} else {
			mDialogManager.createAlertDialog(
					getResources().getString(R.string.check_network),
					getResources().getString(R.string.no_open), getResources()
							.getString(R.string.OK), null,
					new DialogManager.AlertDialogClickListen() {

						@Override
						public void positiveButton(DialogInterface dialog,
								int which) {
							startActivity(new Intent(Settings.ACTION_SETTINGS));
							finish();
						}

						@Override
						public void negativeButton(DialogInterface dialog,
								int which) {

						}
					});
		}
	}

	private void initView() {
		tvTip = (TextView) findViewById(R.id.tv_activity_login_tip);
		// 设置一行字里面其实有几个字的颜色不一样
		String str = "DIGI MTV";
		int fstart = str.indexOf("DIGI");
		int fend = fstart + "DIGI".length();
		SpannableStringBuilder style = new SpannableStringBuilder(str);
		style.setSpan(new ForegroundColorSpan(Color.GREEN), fstart, fend,
				Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		tvTip.setText(style);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
