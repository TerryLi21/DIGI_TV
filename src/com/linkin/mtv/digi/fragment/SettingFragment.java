package com.linkin.mtv.digi.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.linkin.mtv.digi.PreferenceManager;
import com.linkin.mtv.digi.R;
import com.linkin.mtv.digi.adapter.PlanGridAdapter;
import com.linkin.mtv.digi.helper.ConfigHelper;
import com.linkin.mtv.digi.view.SolaimanTextView;
import com.linkin.mtv.util.DateUtil;
import com.linkin.mtv.util.StringUtil;

/**
 * @desc
 * @author liminwei
 * @since 2015-4-22 上午10:05:03
 */
@SuppressLint("NewApi")
public class SettingFragment extends Fragment {

	private com.linkin.mtv.digi.view.SolaimanTextView tvSn, tvCall, tvVersion;
	private Button btnLogout, btnCall;
	private GridView gvPlan;
	private PlanGridAdapter planAdapter;
	private ArrayList<String> planList;
	private PreferenceManager mPreferenceManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LinearLayout llMain = (LinearLayout) inflater.inflate(
				R.layout.fragment_setting, null);
		mPreferenceManager = new PreferenceManager(getActivity());
		initView(llMain);
		setListener();

		return llMain;
	}

	private void initView(View view) {
		gvPlan = (GridView) view.findViewById(R.id.gv_fragment_setting_plan);
		tvSn = (SolaimanTextView) view
				.findViewById(R.id.tv_fragment_setting_sn);
		tvCall = (SolaimanTextView) view
				.findViewById(R.id.tv_fragment_setting_call);
		tvVersion = (SolaimanTextView) view
				.findViewById(R.id.tv_fragment_setting_version);
		btnLogout = (Button) view
				.findViewById(R.id.btn_fragment_setting_logout);
		btnCall = (Button) view.findViewById(R.id.btn_fragment_setting_call);

		tvSn.setText("" + ConfigHelper.getSN());
		tvCall.setText(mPreferenceManager.getPhoneNumber());
		tvVersion.setText("Bangla  " + getVersionName());

		planList = new ArrayList<String>();
		planAdapter = new PlanGridAdapter(getActivity(), planList);
		gvPlan.setAdapter(planAdapter);

	}

	private void setListener() {
		btnLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPreferenceManager.setLoggedIn(false);
				mPreferenceManager.setAccessToken("");
				mPreferenceManager.setPhoneNumber("");
				mPreferenceManager.setHasRights(false);
				mPreferenceManager.setUserID("");
				mPreferenceManager.setValidity("");
				mPreferenceManager.setPurchased(false);
				getActivity().finish();
			}
		});
		btnCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String number = tvCall.getText().toString().trim();
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ number));
				getActivity().startActivity(intent);
			}
		});
	}

	private void setDate() {
		String validity = mPreferenceManager.getValidity();
		String day = "";
		Log.i("test", "validity = " + validity);
		if (!StringUtil.isBlank(validity)) {
			validity = validity.substring(0, validity.indexOf("T"));
			Log.i("test", "validity = " + validity);
			day = "" + DateUtil.calculateFromTodayDistance(validity);
		}
		planList.clear();// 先清除，再插入
		planList.add("UserId" + "\n" + mPreferenceManager.getUserId());
		planList.add("Remain day" + "\n" + day);
		planAdapter.notifyDataSetChanged();
	}

	/**
	 * 更新该页面的数据
	 */
	public void updateData() {
		setDate();
		tvCall.setText(mPreferenceManager.getPhoneNumber());
		planAdapter.notifyDataSetChanged();
	}

	// 获取版本号
	private String getVersionName() {
		try {
			PackageManager packageManager = getActivity().getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(getActivity()
					.getPackageName(), 0);
			String version = packInfo.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
