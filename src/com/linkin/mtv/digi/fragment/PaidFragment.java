package com.linkin.mtv.digi.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.linkin.mtv.DataManager;
import com.linkin.mtv.Global;
import com.linkin.mtv.data.Channel;
import com.linkin.mtv.digi.PreferenceManager;
import com.linkin.mtv.digi.R;
import com.linkin.mtv.digi.activity.AuthActivity;
import com.linkin.mtv.digi.activity.PlayChannelActivity;
import com.linkin.mtv.digi.adapter.ChannelGridAdapter;

/**
 * @desc
 * @author liminwei
 * @since 2015-4-22 上午10:05:03
 */
@SuppressLint("NewApi")
public class PaidFragment extends Fragment {

	private GridView gvMain;
	private LinearLayout llTip;
	private ChannelGridAdapter adatper;
	private updateReceiver mUpdateReceiver;
	private Button btnCharge;
	private ArrayList<Channel> channels;
	private PreferenceManager mPreferenceManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUpdateReceiver = new updateReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Global.ACTION_DATA_UPDATE);
		getActivity().registerReceiver(mUpdateReceiver, filter);
		mPreferenceManager = new PreferenceManager(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		RelativeLayout rlMain = (RelativeLayout) inflater.inflate(
				R.layout.fragment_paid, null);
		initView(rlMain);
		setListener();
		return rlMain;
	}

	private void initView(View parent) {
		gvMain = (GridView) parent.findViewById(R.id.gv_fragment_paid);
		llTip = (LinearLayout) parent.findViewById(R.id.ll_fragment_paid_tip);
		btnCharge = (Button) llTip.findViewById(R.id.btn_fragment_paid_charge);
		channels = (ArrayList<Channel>) DataManager.getInstance().getPaidList();
		adatper = new ChannelGridAdapter(getActivity(), channels);

		gvMain.setAdapter(adatper);
	}

	private void setListener() {
		gvMain.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Channel ch = DataManager.getInstance().getPaidList()
				// .get(position);
				// if (PlanManager.getInstance().isChannelInPlan(
				// String.valueOf(ch.getId()))) {
				// }
				if (mPreferenceManager.hasPurchased()) {
					Intent intent = new Intent(getActivity(),
							PlayChannelActivity.class);
					Bundle bu = new Bundle();
					bu.putSerializable("channels", channels);
					bu.putInt("position", position);
					bu.putBoolean("isFree", false);
					intent.putExtras(bu);
					getActivity().startActivity(intent);
				} else {
					llTip.setVisibility(View.VISIBLE);
				}
			}
		});

		btnCharge.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				llTip.setVisibility(View.GONE);
				Intent intent = new Intent(getActivity(), AuthActivity.class);
				getActivity().startActivity(intent);
			}
		});

		llTip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				llTip.setVisibility(View.GONE);
			}
		});
	}

	/**
	 * 更新该页面的数据
	 */
	public void updateDate() {
		channels = (ArrayList<Channel>) DataManager.getInstance().getPaidList();
		adatper.update(channels);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(mUpdateReceiver);
	}

	private class updateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			channels = (ArrayList<Channel>) DataManager.getInstance()
					.getPaidList();
			adatper.update(channels);
		}
	}
}
