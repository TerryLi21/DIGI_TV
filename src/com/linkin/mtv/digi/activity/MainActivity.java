package com.linkin.mtv.digi.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.linkin.mtv.Global;
import com.linkin.mtv.digi.R;
import com.linkin.mtv.digi.adapter.MainViewPagerAdapter;
import com.linkin.mtv.digi.fragment.FreeFragment;
import com.linkin.mtv.digi.fragment.PaidFragment;
import com.linkin.mtv.digi.fragment.SettingFragment;
import com.linkin.mtv.digi.view.SolaimanTextView;
import com.linkin.mtv.service.DataUdateService;
import com.linkin.mtv.service.EpgUdateService;
import com.linkin.mtv.service.NotiMessageUpdateService;
import com.linkin.mtv.util.DialogManager;
import com.linkin.mtv.util.DialogManager.AlertDialogClickListen;

public class MainActivity extends FragmentActivity implements
		OnPageChangeListener, OnCheckedChangeListener {

	private int mScreenWidth;

	private MainViewPagerAdapter mMainViewPagerAdapter;
	private ViewPager vpFrag;
	private View viewBottomLine;
	private RadioGroup rgTabs;
	private SolaimanTextView tvTitle;
	private NotiMessageReceiver mNotiMessageReceiver;
	private ImageView ivRedPoint;
	private DialogManager mDialogManager;

	private FreeFragment mFreeFragment;
	private PaidFragment mPaidFragment;
	private SettingFragment mSettingFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 控制屏幕保持高亮
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();
		mScreenWidth = dm.widthPixels;
		mDialogManager = new DialogManager(this);
		initView();
		initService();
		initReceiver();
	}

	private void initView() {
		viewBottomLine = findViewById(R.id.view_bottom_line);
		ViewGroup.LayoutParams lps = viewBottomLine.getLayoutParams();
		lps.width = (int) (mScreenWidth / 3);
		viewBottomLine.setLayoutParams(lps);
		ivRedPoint = (ImageView) findViewById(R.id.iv_activity_main_red_point);

		mMainViewPagerAdapter = new MainViewPagerAdapter(
				getSupportFragmentManager());
		vpFrag = (ViewPager) findViewById(R.id.vp_frag);

		List<Fragment> list = new ArrayList<Fragment>();
		mFreeFragment = new FreeFragment();
		mPaidFragment = new PaidFragment();
		mSettingFragment = new SettingFragment();
		list.add(mFreeFragment);
		list.add(mPaidFragment);
		list.add(mSettingFragment);
		mMainViewPagerAdapter.setFragments(list);
		vpFrag.setAdapter(mMainViewPagerAdapter);
		vpFrag.setOnPageChangeListener(this);
		rgTabs = (RadioGroup) findViewById(R.id.rg_tab);
		rgTabs.setOnCheckedChangeListener(this);

		tvTitle = (SolaimanTextView) findViewById(R.id.tv_activity_main_title);
	}

	private void initService() {
		startService(new Intent(this, DataUdateService.class));// 开获取channel的服务
		startService(new Intent(this, EpgUdateService.class));// 开获取epg的服务
		startService(new Intent(this, NotiMessageUpdateService.class));// 开获取信息服务
	}

	private void initReceiver() {
		mNotiMessageReceiver = new NotiMessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Global.ACTION_MESSAGE_UPDATE);
		registerReceiver(mNotiMessageReceiver, filter);
	}

	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.iv_activity_main_bell:// 右上角的响铃
			ivRedPoint.setVisibility(View.GONE);
			Intent intent = new Intent(this, NotiMessageActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@SuppressLint("NewApi")
	@Override
	public void onPageScrolled(int position, float offset, int arg2) {
		int x = (int) ((position + offset) * mScreenWidth / 3);
		viewBottomLine.setX(x);
	}

	@Override
	public void onPageSelected(int pos) {
		RadioButton rb = (RadioButton) rgTabs.getChildAt(pos * 2);
		if (!rb.isChecked()) {
			rb.setChecked(true);
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
		int pos = 0;
		for (int i = 0; i < radioGroup.getChildCount(); i = i + 2) {
			if (radioGroup.getChildAt(i).getId() == checkedId) {
				pos = i / 2;// 这里要除以二是因为中间夹杂了imageView为第1和第3个控件（从0开始计算）
				break;
			}
		}
		if (pos != vpFrag.getCurrentItem()) {
			vpFrag.setCurrentItem(pos);
		}

		switch (pos) {
		case 0:
			tvTitle.setText(getResources().getString(R.string.free));
			viewBottomLine.setBackgroundResource(R.color.tab1);
			mFreeFragment.updateDate();// 滑到该页面时，更新数据
			break;
		case 1:
			tvTitle.setText(getResources().getString(R.string.paid));
			viewBottomLine.setBackgroundResource(R.color.tab2);
			mPaidFragment.updateDate();// 滑到该页面时，更新数据
			break;
		case 2:
			tvTitle.setText(getResources().getString(R.string.account));
			viewBottomLine.setBackgroundResource(R.color.tab3);
			mSettingFragment.updateData();// 滑到该页面时，更新数据
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopService(new Intent(this, DataUdateService.class));
		stopService(new Intent(this, EpgUdateService.class));// 开获取epg的服务
		stopService(new Intent(this, NotiMessageUpdateService.class));// 开获取epg的服务
		unregisterReceiver(mNotiMessageReceiver);
	}

	private class NotiMessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			ivRedPoint.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			mDialogManager.createAlertDialog("Really?", "Are you sure?",
					getResources().getString(R.string.OK), "cancel",
					new AlertDialogClickListen() {

						@Override
						public void positiveButton(DialogInterface dialog,
								int which) {
							finish();
						}

						@Override
						public void negativeButton(DialogInterface dialog,
								int which) {
							dialog.dismiss();
						}
					});
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
